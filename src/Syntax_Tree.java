import java.util.Stack;

public class Syntax_Tree
{
    private Stack<Node> tree_stack = new Stack<Node>();
    private Stack<Character> operator_stack = new Stack<Character>();
    public static String insert_dots(String regex)
    {
        StringBuilder r = new StringBuilder();
        for(int i=0;i<regex.length()-1;i++)
        {
            char p = regex.charAt(i);
            char n = regex.charAt(i+1);
            if(p=='\\') // n has to be a character
            {
                r.append(p);
                if(i+2<regex.length())
                {
                    p = n;
                    r.append(p);
                    n = regex.charAt(i+2);
                    // now p has to be a character
                    if(!is_operator(n) || n=='(')r.append('.');
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
            if(is_operator(p))
            {
                if(p=='*' || p==')')
                {
                    if(!is_operator(n) || n=='(')r.append('.');
                }
            }
            else
            {
                if(!is_operator(n) || n=='(')r.append('.');
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
    public static int get_priority(char ch)
    {
        if(ch=='*')return 3;
        else if(ch=='.')return 2;
        else if(ch=='|')return 1;
        else return -1;
    }
    public static boolean is_operator(char ch)
    {
        return ch=='('||ch==')'||ch=='*'||ch=='|'||ch=='.';
    }
    public static int get_number(char ch)
    {
        if(ch=='*')return 1;
        else if(ch=='|'||ch=='.')return 2;
        return -1;
    }
    public void add_character(char ch)
    {
        tree_stack.push(new Node(ch));
    }
    public void add_operator(char op)
    {
        if(operator_stack.empty())
        {
            operator_stack.push(op);
            return;
        }
        char top = operator_stack.peek();
        if(op==')')
        {
            while(operator_stack.peek()!='(')pop_operator();
            operator_stack.pop();
        }
        else if(op == '(' || get_priority(op)>=get_priority(top))
        {
            operator_stack.push(op);
        }
        else
        {
            while(!operator_stack.empty()&&get_priority(op)<get_priority(operator_stack.peek()))pop_operator();
            operator_stack.push(op);
        }
    }
    private void pop_operator()
    {
        Node operator = new Node(operator_stack.peek());
        //System.out.println(operator.value+"has been popped");
        operator_stack.pop();
        if(operator.value == '*')
        {
            Node tree = new Node(tree_stack.peek());
            tree_stack.pop();
            operator.left = tree;
            tree_stack.push(operator);
        }
        else if(operator.value == '|' || operator.value == '.')
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
        regex = insert_dots(regex);
        for(int i=0;i<regex.length();i++)
        {
            char d = regex.charAt(i);
            if(d=='\\')
            {
                i++;
                try{d = regex.charAt(i);}catch(Exception e){throw new Exception("Escape syntax is incorrect");}
                s.add_character(d);
            }
            else
            {
                if(is_operator(d))s.add_operator(d);
                else s.add_character(d);
            }
        }
        s.finish();
        return s.get_top_of_tree_stack();
    }
}