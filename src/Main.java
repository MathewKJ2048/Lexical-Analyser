import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {

        try
        {
            Scanner sc = new Scanner(System.in);
            while(true)
            {
                System.out.println("Enter regex:");
                String regex = sc.nextLine();
                if(regex.equals("exit"))break;
                //System.out.println("Dot inserted:"+Syntax_Tree.insert_dots(sc.next()));
                System.out.println(new Node(Syntax_Tree.get_syntax_tree(regex)).contents());}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void syntax_tree_test()
    {
        Scanner sc  = new Scanner(System.in);
        while(true)
        {
            System.out.println("Enter regex:");
            String regex = sc.next();
            if(regex.equals("quit"))break;
            regex = Syntax_Tree.insert_dots(regex);
            System.out.println("Explicit Regex:"+regex);
            Syntax_Tree l = new Syntax_Tree();
            for(int i=0;i<regex.length();i++)
            {
                char d = regex.charAt(i);
                if(Syntax_Tree.is_operator(d))l.add_operator(d);
                else l.add_character(d);
                System.out.println("----------------------------------------------------");
                System.out.print("top of operator stack is: "+(l.is_operator_stack_empty()?"":l.get_top_of_operator_stack()));
                System.out.println("top of tree stack is: "+(l.is_tree_stack_empty()?"":new Node(l.get_top_of_tree_stack()).contents()));
            }
            System.out.println("finished tree:");
            l.finish();
            System.out.println(new Node(l.get_top_of_tree_stack()).contents());
        }
        System.out.println("test complete");
    }
}
