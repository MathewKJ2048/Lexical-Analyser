import finite_state_machine.*;

import java.util.ArrayList;
import java.util.List;

public class Epsilon_NFA_generator
{
    List<State> states;
    int count = 0;
    public Epsilon_NFA_generator()
    {
        states = new ArrayList<>();
    }
    public List<State> generate(Node root) // returns a start_state and an end_state
    {
        if(root==null)return null;
        char v = root.value;
        boolean e = root.is_escaped;
        final State start = new State(count++);
        final State end = new State(count++);
        states.add(start);
        states.add(end);
        if(root.left==null && root.right==null) // leaf node
        {
            if(!e && v==Syntax.DOT)start.add_transition_to("dot",end);
            else if(!e && v==Syntax.EPSILON)start.add_epsilon_transition_to(end);
            //else if(!e && v==Syntax.PHI){start=null;end=null;}
            else start.add_transition_to(v+"",end);
        }
        else //internal node
        {
            if(v == Syntax.CONCAT)
            {
                List<State> l = generate(root.left);
                List<State> r = generate(root.right);
                start.add_epsilon_transition_to(l.get(0));
                r.get(1).add_epsilon_transition_to(end);
                l.get(1).add_epsilon_transition_to(r.get(0));
            }
            else if(v == Syntax.KLEENE)
            {
                List<State> l = generate(root.left);
                start.add_epsilon_transition_to(l.get(0));
                l.get(1).add_epsilon_transition_to(end);
                end.add_epsilon_transition_to(start);
                start.add_epsilon_transition_to(end);
            }
            else if(v == Syntax.UNION)
            {
                List<State> l = generate(root.left);
                List<State> r = generate(root.right);
                start.add_epsilon_transition_to(l.get(0));
                start.add_epsilon_transition_to(r.get(0));
                l.get(0).add_epsilon_transition_to(end);
                r.get(0).add_epsilon_transition_to(end);
            }
        }
        List<State> l = new ArrayList<>();
        l.add(start);
        l.add(end);
        return l;
    }
    public static FSM get_epsilon_NFA(Node root)
    {
        Epsilon_NFA_generator g = new Epsilon_NFA_generator();
        FSM e_nfa = new FSM();
        List<State> l = g.generate(root);
        e_nfa.start.add(l.get(0));
        e_nfa.accepting.add(l.get(1)); // all accepting states merged into a single one
        e_nfa.states.addAll(g.states);
        return e_nfa;
    }
}
