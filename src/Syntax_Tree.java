import java.util.Stack;

public class Syntax_Tree
{
    private final Stack<Node> tree_stack = new Stack<Node>();
    private final Stack<Character> operator_stack = new Stack<Character>();
    public static String insert_concat(String regex)
    {
        StringBuilder r = new StringBuilder();
        for(int i=0;i<regex.length()-1;i++)
        {
            char p = regex.charAt(i);
            char n = regex.charAt(i+1);
            if(p==Syntax.BACKSLASH) // n has to be a character
            {
                r.append(p);
                if(i+2<regex.length())
                {
                    p = n;
                    r.append(p);
                    n = regex.charAt(i+2);
                    // now p has to be a character
                    if(!Syntax.is_operator(n) || n==Syntax.OPEN_BRACKET)r.append(Syntax.CONCAT);
                    i++;
                    continue;
                }
                else break;
            }
            r.append(p);
            /*
            ab -> a.b
            a*b -> a*.b
            )b -> ).b
            b( -> b.(
            )( -> ).(
            */
            if(Syntax.is_operator(p))
            {
                if(p==Syntax.KLEENE || p==Syntax.CLOSE_BRACKET)
                {
                    if(!Syntax.is_operator(n) || n==Syntax.OPEN_BRACKET)r.append(Syntax.CONCAT);
                }
            }
            else
            {
                if(!Syntax.is_operator(n) || n==Syntax.OPEN_BRACKET)r.append(Syntax.CONCAT);
            }
        }
        r.append(regex.charAt(regex.length()-1));
        return r.toString();
    }
    public Node get_tree()
    {
        return tree_stack.peek();
    }
    public boolean is_operator_stack_empty()
    {
        return operator_stack.empty();
    }
    public boolean is_tree_stack_empty()
    {
        return tree_stack.empty();
    }
    public char get_top_of_operator_stack()
    {
        return operator_stack.peek();
    }
    public Node get_top_of_tree_stack()
    {
        return tree_stack.peek();
    }
    
    public void add_character(char ch, boolean is_escape)
    {
        Node x = new Node(ch);
        x.is_escaped = is_escape;
        tree_stack.push(x);
    }
    public void add_operator(char op)
    {
        if(operator_stack.empty())
        {
            operator_stack.push(op);
            return;
        }
        char top = operator_stack.peek();
        if(op==Syntax.CLOSE_BRACKET)
        {
            while(operator_stack.peek()!=Syntax.OPEN_BRACKET)pop_operator();
            operator_stack.pop();
        }
        else if(op == Syntax.OPEN_BRACKET || Syntax.get_priority(op)>=Syntax.get_priority(top))
        {
            operator_stack.push(op);
        }
        else
        {
            while(!operator_stack.empty()&&Syntax.get_priority(op)<Syntax.get_priority(operator_stack.peek()))pop_operator();
            operator_stack.push(op);
        }
    }
    private void pop_operator()
    {
        Node operator = new Node(operator_stack.peek());
        //System.out.println(operator.value+"has been popped");
        operator_stack.pop();
        if(operator.value == Syntax.KLEENE)
        {
            Node tree = new Node(tree_stack.peek());
            tree_stack.pop();
            operator.left = tree;
            tree_stack.push(operator);
        }
        else if(operator.value == Syntax.UNION || operator.value == Syntax.CONCAT)
        {
            Node tree_right = new Node(tree_stack.peek());
            tree_stack.pop();
            Node tree_left = new Node(tree_stack.peek());
            tree_stack.pop();
            operator.left = tree_left;
            operator.right = tree_right;
            tree_stack.push(operator);
        }
    }
    public void finish()
    {
        while(!operator_stack.empty())pop_operator();
    }
    public static Node get_syntax_tree(String regex) throws Exception
    {
        Syntax_Tree s = new Syntax_Tree();
        regex = insert_concat(regex);
        for(int i=0;i<regex.length();i++)
        {
            char d = regex.charAt(i);
            if(d==Syntax.BACKSLASH)
            {
                i++;
                try{d = regex.charAt(i);}catch(Exception e){throw new Exception("Escape syntax is incorrect");}
                s.add_character(d, true);
            }
            else
            {
                if(Syntax.is_operator(d))s.add_operator(d);
                else s.add_character(d, false);
            }
        }
        s.finish();
        return s.get_top_of_tree_stack();
    }
}