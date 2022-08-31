package finite_state_machine;

import java.util.ArrayList;
import java.util.List;

public class State
{
    public int id;
    public List<Transition> transitions = new ArrayList<>();
    public State(int id)
    {
        this.id = id;
    }
    public void add_epsilon_transition_to(State x)
    {
        transitions.add(new Transition("epsilon",this,x));
    }
    public void add_transition_to(String s,State x)
    {
        transitions.add(new Transition(s,this,x));
    }
}
