package finite_state_machine;

import java.util.ArrayList;
import java.util.List;

public class FSM
{
    public List<State> states = new ArrayList<>();
    public List<State> start = new ArrayList<>();
    public List<State> accepting = new ArrayList<>();
    public void print()
    {
        System.out.println("id\t| transitions (input->id)");
        for(int i=0;i<states.size();i++)
        {
            State s = states.get(i);
            System.out.print(+s.id+"\t| ");
            for(int j=0;j<s.transitions.size();j++)System.out.print(s.transitions.get(j).input+"->"+s.transitions.get(j).end.id+"\t");
            System.out.println();
        }
        System.out.print("Starting state id(s):");
        for(int i=0;i<start.size();i++)System.out.print(" "+start.get(i).id);
        System.out.print("\nAccepting state id(s):");
        for(int i=0;i<accepting.size();i++)System.out.print(" "+accepting.get(i).id);
        System.out.println();
    }
}