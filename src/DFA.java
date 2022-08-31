import finite_state_machine.*;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class DFA
{
    public static FSM generate_DFA(FSM nfa)
    {
        FSM dfa = new FSM();
        int ct=0;
        //define language = set of possible input symbols (according to regex)
        Set<String> alphabet = new HashSet<>();
        for(State s : nfa.states)for(Transition t : s.transitions)alphabet.add(t.input);

        class DFA_state
        {
            static int ct = 0;
            Set<State> nfa_states = new HashSet<>();
            boolean is_marked = false;
            State corresponding_state;
            DFA_state(int id)
            {
                corresponding_state = new State(id);
                dfa.states.add(corresponding_state);
            }
        }
        DFA_state start = new DFA_state(ct++);
        start.nfa_states.addAll(nfa.start);
        dfa.start.add(start.corresponding_state);
        Set<DFA_state> Dstates = new HashSet<>();
        Dstates.add(start);
        DFA_state current = start;
        while(current!=null)
        {
            current.is_marked=true;
            outer: for(String sym : alphabet)
            {
                Set<State> out = new HashSet<>();
                for(State s : current.nfa_states)for(Transition t : s.transitions)if(t.input.equals(sym))out.add(t.end);

                for(DFA_state ds : Dstates)if(ds.nfa_states.equals(out))
                {
                    current.corresponding_state.add_transition_to(sym,ds.corresponding_state);
                    continue outer;
                }
                // at this point, no state in Dtrans matches out, so it needs to be added
                DFA_state out_state = new DFA_state(ct++);
                out_state.nfa_states = out;
                current.corresponding_state.add_transition_to(sym,out_state.corresponding_state);
                Dstates.add(out_state);
            }
            current = null;
            for(DFA_state ds : Dstates)if(!ds.is_marked)
            {
                current = ds;
            }
        }
/*
        for(DFA_state ds : Dstates)
        {
            System.out.print("id:"+ds.corresponding_state.id+"|");
            for(State s : ds.nfa_states)System.out.print(" "+s.id);
            System.out.println();
        }
*/
        for(DFA_state ds : Dstates)for(State ns : ds.nfa_states)if(nfa.accepting.contains(ns))
            if(!dfa.accepting.contains(ds.corresponding_state))dfa.accepting.add(ds.corresponding_state);
        return dfa;
    }
    public static boolean simulate_DFA(FSM dfa, String input)
    {
        State current = dfa.start.get(0);
        outer: for(int i=0;i<input.length();i++)
        {
            char in = input.charAt(i);
            for(int j=0;j<current.transitions.size();j++)
            {
                Transition t = current.transitions.get(j);
                if(t.input.equals(""+in))
                {
                    current = t.end;
                    continue outer;
                }
            }
            for(int j=0;j<current.transitions.size();j++)
            {
                Transition t = current.transitions.get(j);
                if(t.input.equals(Syntax.get_transition_label(Syntax.DOT)))
                {
                    current = t.end;
                    continue outer;
                }
            }
            return false;
        }
        return dfa.accepting.contains(current);
    }
}
