package finite_state_machine;

public class Transition
{
    public String input;
    public State end;
    public State start;
    public Transition()
    {
        input = "epsilon";
        end = null;
        start = null;
    }
    public Transition(String s, State start, State end)
    {
        this.input = s;
        this.start = start;
        this.end = end;
    }
    public Transition(Transition t)
    {
        input = new String(t.input);
        end = t.end;
        start = t.start;
    }
}
