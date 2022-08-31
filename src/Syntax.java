public class Syntax
{
    public static final char CONCAT = '◦';
    public static final char KLEENE = '*';
    public static final char UNION = '|';
    public static final char OPEN_BRACKET = '(';
    public static final char CLOSE_BRACKET = ')';

    public static final char BACKSLASH = '\\';
    public static final char DOT = '.';
    public static final char EPSILON = 'ε';
    public static final char PHI = 'ϕ';
    public static final char HASH = '#';

    public static String get_transition_label(char ch)
    {
        if(ch==DOT)return "dot";
        else if(ch==EPSILON)return "epsilon";
        else return "";
    }
    public static int get_priority(char ch)
    {
        if(ch==KLEENE)return 3;
        else if(ch==CONCAT)return 2;
        else if(ch==UNION)return 1;
        else return -1;
    }
    public static boolean is_operator(char ch)
    {
        return ch==KLEENE||ch==UNION||ch==CONCAT||ch==OPEN_BRACKET||ch==CLOSE_BRACKET;
    }
    public static boolean is_reserved_symbol(char ch)
    {
        return ch==DOT||ch==EPSILON||ch==PHI;
    }

}
