import finite_state_machine.FSM;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        String separator = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
        try
        {
            Scanner sc = new Scanner(System.in);

                System.out.println("Enter regex:");
                String regex = sc.nextLine();
                System.out.println(separator);

                Node tree = new Node(Syntax_Tree.get_syntax_tree(regex));
                System.out.println("Syntax tree:"+tree.contents());
                System.out.println(separator);

                System.out.println("Epsilon_NFA:");
                Epsilon_NFA_generator.get_epsilon_NFA(tree).print();
                System.out.println(separator);

                NFA_generator.Positions comp_tree = NFA_generator.compute_tree(tree);
                System.out.println("Computed values of firstpos, lastpos, nullable and followpos:");
                NFA_generator.print_wrapped_tree(comp_tree);
                System.out.println(separator);

                System.out.println("NFA:");
                FSM nfa = NFA_generator.generate_NFA(comp_tree);
                nfa.print();
                System.out.println(separator);

                System.out.println("DFA:");
                FSM dfa = DFA.generate_DFA(nfa);
                dfa.print();
                System.out.println(separator);

                System.out.println("Regex: "+regex);
                System.out.println("Enter inputs (Enter exit to quit) :");
                while(true)
                {
                    String in = sc.nextLine();
                    if(in.equals("exit"))break;
                    System.out.println(DFA.simulate_DFA(dfa,in)?"Accept":"Reject");
                }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}