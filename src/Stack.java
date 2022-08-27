import java.lang.reflect.Array;

public class Stack<T>
{
    private T arr[];
    private int top;
    public Stack(Class<T> c, int size)
    {
        this.arr = (T[]) Array.newInstance(c,size);
        top = -1;
    }
    public boolean push(T t)
    {
        top++;
        if(top>=arr.length)return false;
        arr[top] = t;
        return true;
    }
    public boolean pop()
    {
        if(top==-1)return false;
        top--;
        return true;
    }
}
