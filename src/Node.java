public class Node
{
    public char value;
    public Node left;
    public Node right;
    public Node()
    {
        this.value=0;
        this.left=null;
        this.right=null;
    }
    public Node(char v)
    {
        this.value=v;
        this.left=null;
        this.right=null;
    }
    public Node(Node x)
    {
        this.value=x.value;
        this.left=x.left==null?null:new Node(x.left);
        this.right=x.right==null?null:new Node(x.right);
    }
    public String contents()
    {
        return "["+(left==null?"":left.contents())+value+(right==null?"":right.contents())+"]";
    }
}
