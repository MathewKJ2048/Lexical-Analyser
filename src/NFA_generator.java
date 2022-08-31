import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import finite_state_machine.*;

public class NFA_generator
{
    static class Pos // stores all computed metadata for syntax tree
    {
        Set<Integer> firstpos = new HashSet<>();
        Set<Integer> lastpos = new HashSet<>();
        Set<Integer> followpos = new HashSet<>();
        boolean nullable;
    }
    static class Positions
    {
        Node x;
        int position = 0; // default value is 0 for internal nodes
        Positions left = null;
        Positions right = null;
        Pos p = new Pos();
    }
    static class Integer_pointer
    {
        int value;
    }
    public static Positions compute_tree(Node root)
    {
        Node hash = new Node();
        hash.value = Syntax.HASH;
        hash.is_escaped = false;
        Node new_root = new Node();
        new_root.value = Syntax.CONCAT;
        new_root.is_escaped = false;
        new_root.left = root;
        new_root.right = hash;
        Integer_pointer positions = new Integer_pointer(); // positions of leaf nodes
        positions.value=1;
        HashMap<Integer,Positions> lookup = new HashMap<>();
        Positions p_root = get_wrapped_tree(new_root,lookup,positions);
        set_nullable(p_root);
        set_firstpos(p_root);
        set_lastpos(p_root);
        set_follow_pos(p_root,lookup);
        return p_root;
    }
    public static Positions get_wrapped_tree(Node root, HashMap<Integer,Positions> lookup,Integer_pointer positions)
    {
        if(root == null)return null;
        Positions p = new Positions();
        p.x = root;
        p.left = get_wrapped_tree(root.left,lookup,positions);
        p.right = get_wrapped_tree(root.right,lookup,positions);
        if(root.left == null && root.right == null)
        {
            if(!p.x.is_escaped && p.x.value==Syntax.HASH)
            {
                p.position = -1;
            }
            else
            {
                p.position=positions.value;
                positions.value++;
            }
            lookup.put(p.position,p);
        }
        return p;
    }
    public static void set_nullable(Positions root)
    {
        if(root==null)return;
        set_nullable(root.left);
        set_nullable(root.right);
        if(root.x.right == null && root.x.left==null) // must be leaf
        {
            if(!root.x.is_escaped && root.x.value == Syntax.EPSILON)root.p.nullable=true;
            else root.p.nullable = false;
        }
        else if(root.x.right == null) // must be kleene
        {
            root.p.nullable = true;
        }
        else // concat or union
        {
            if(root.x.value == Syntax.CONCAT)root.p.nullable = root.left.p.nullable && root.right.p.nullable;
            else if(root.x.value == Syntax.UNION)root.p.nullable = root.left.p.nullable || root.right.p.nullable;
        }
    }
    public static void set_firstpos(Positions root)
    {
        if(root.right==null && root.left==null)// must be a leaf
        {
            //only add firstpos if the leaf is not epsilon
            if(!(!root.x.is_escaped && root.x.value == Syntax.EPSILON))root.p.firstpos.add(root.position);
        }
        else if(root.right==null) // must be kleene
        {
            set_firstpos(root.left);
            root.p.firstpos.addAll(root.left.p.firstpos);
        }
        else // union or concat
        {
            set_firstpos(root.left);
            set_firstpos(root.right);
            if(root.x.value == Syntax.UNION)
            {
                root.p.firstpos.addAll(root.left.p.firstpos);
                root.p.firstpos.addAll(root.right.p.firstpos);
            }
            else if(root.x.value == Syntax.CONCAT)
            {
                root.p.firstpos.addAll(root.left.p.firstpos);
                if(root.left.p.nullable)root.p.firstpos.addAll(root.right.p.firstpos);
            }
        }
    }
    public static void set_lastpos(Positions root)
    {
        if(root.right==null && root.left==null)// must be a leaf
        {
            //only add lastpos if the leaf is not epsilon
            if(!(!root.x.is_escaped && root.x.value == Syntax.EPSILON))root.p.lastpos.add(root.position);
        }
        else if(root.right==null) // must be kleene
        {
            set_lastpos(root.left);
            root.p.lastpos.addAll(root.left.p.lastpos);
        }
        else // union or concat
        {
            set_lastpos(root.left);
            set_lastpos(root.right);
            if(root.x.value == Syntax.UNION)
            {
                root.p.lastpos.addAll(root.left.p.lastpos);
                root.p.lastpos.addAll(root.right.p.lastpos);
            }
            else if(root.x.value == Syntax.CONCAT)
            {
                root.p.lastpos.addAll(root.right.p.lastpos);
                if(root.right.p.nullable)root.p.lastpos.addAll(root.left.p.lastpos);
            }
        }
    }
    public static void set_follow_pos(Positions root, HashMap<Integer,Positions> lookup)
    {
        if(root==null)return;
        if(!root.x.is_escaped && root.x.value==Syntax.KLEENE)
        {
            Object[] lp = root.p.lastpos.toArray();
            for(int k=0;k<lp.length;k++)
            {
                lookup.get(Integer.parseInt(lp[k].toString())).p.followpos.addAll(root.p.firstpos);
            }
        }
        else if(!root.x.is_escaped && root.x.value==Syntax.CONCAT)
        {
            Object[] lp = root.left.p.lastpos.toArray();
            for(int k=0;k<lp.length;k++)
            {
                int i=Integer.parseInt(lp[k].toString());
                lookup.get(i).p.followpos.addAll(root.right.p.firstpos);
            }
        }
        set_follow_pos(root.left, lookup);
        set_follow_pos(root.right, lookup);
    }
    public static void print_wrapped_tree(Positions root)
    {
        if(root==null)return;
        System.out.println("ID: "+root.position);
        System.out.println("character: "+root.x.value);
        System.out.println("Nullable: "+root.p.nullable);
        System.out.print("Firstpos:"+root.p.firstpos);
        System.out.print("\nLastpos:"+root.p.lastpos);
        System.out.print("\nFollowpos:"+root.p.followpos);
        System.out.println("\n-----------");
        print_wrapped_tree(root.left);
        print_wrapped_tree(root.right);
    }
    public static FSM generate_NFA(Positions root)
    {
        FSM nfa = new FSM();
        HashMap<Integer,State> state_lookup = new HashMap<>();
        set_states_of_NFA(root,nfa,state_lookup);
        set_transitions_of_NFA(root,nfa,state_lookup);
        for(int i : root.p.firstpos)nfa.start.add(state_lookup.get(i));
        nfa.accepting.add(state_lookup.get(-1));
        return nfa;
    }
    private static void set_states_of_NFA(Positions root, FSM dfa, HashMap<Integer,State> state_lookup)
    {
        if(root == null)return;
        if(root.right==null && root.left==null)
        {
            State x = new State(root.position);
            state_lookup.put(root.position,x);
            dfa.states.add(x);
        }
        else
        {
            set_states_of_NFA(root.left,dfa,state_lookup);
            set_states_of_NFA(root.right,dfa,state_lookup);
        }
    }
    private static void set_transitions_of_NFA(Positions root, FSM dfa, HashMap<Integer,State> state_lookup)
    {
        if(root == null)return;
        if(root.right==null && root.left==null) // leaf node
        {
            State current = state_lookup.get(root.position);
            String transition = root.x.value+"";
            if(!root.x.is_escaped && !Syntax.get_transition_label(root.x.value).equals(""))transition=Syntax.get_transition_label(root.x.value);
            for(int i : root.p.followpos)current.add_transition_to(transition,state_lookup.get(i));
        }
        else
        {
            set_transitions_of_NFA(root.left,dfa,state_lookup);
            set_transitions_of_NFA(root.right,dfa,state_lookup);
        }
    }
}
