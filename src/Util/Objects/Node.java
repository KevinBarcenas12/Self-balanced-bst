package Util.Objects;

public class Node {
    int[] key;
    Node[] child;
    int count;
    boolean leaf;

    public Node(double deg) {
        child   = new Node[(int) (deg * 2 + 3)];
        key     = new int[(int) (deg * 2 + 2)];
        count   = 0;
        leaf    = true;
    }

    public Node[] getChild() {
        return child;
    }

    public int getChildCount() {
        int count = 0;
        for (Node child : this.child) if (child != null) count++;
        return count;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        for (int i = 0; i < count; i++) str.append((i < count - 1) ? (key[i] + "|") : (key[i]));
        str.append("]");
        return str.toString();
    }
}