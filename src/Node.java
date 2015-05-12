import java.util.BitSet;
import java.util.Comparator;

/**
 * Created by Kevin Pietrow on 4/22/15.
 */
public class Node implements Comparator<Node> {
    Node left = null;  // The two possible children
    Node right = null;
    Character name = '-';
    int value = 0;
    BitSet code = new BitSet();

    /**
     * For creating branch nodes
     *
     * @param valueOf   value of the branch node
     */
    public Node (int valueOf) {
        value = valueOf;
    }

    /**
     * For creating leaf nodes
     *
     * @param nameOf    name of the leaf node
     * @param valueOf   value of the leaf node
     */
    public Node (Character nameOf, int valueOf) {
        name = nameOf;
        value = valueOf;
    }

    public int compare (Node n, Node m) {
        int length1 = n.getCode().length();
        int length2 = m.getCode().length();
        int i = length1 > length2 ? 1 : length1 < length2 ? -1 : 0;

        if (i != 0)
            return i;

        i = n.getName().compareTo(m.getName());
        return i;
    }


    /**
     * Sets the left child node
     *
     * @param child the child
     */
    public void setLeft (Node child) {
        left = child;
    }

    /**
     * Sets the right child node
     *
     * @param child the child
     */
    public void setRight (Node child) {
        right = child;
    }

    /**
     * Returns the name of the node
     *
     * @return  the node's name
     */
    public Character getName () {
        return name;
    }

    /**
     * Returns the value of the node
     *
     * @return  the node's value
     */
    public int getValue () {
        return value;
    }

    /**
     * Returns the left child node
     *
     * @return the left child
     */
    public Node getLeft () {
        return left;
    }

    /**
     * Returns the right child node
     *
     * @return the right child
     */
    public Node getRight () {
        return right;
    }

    /**
     * Returns the bit code of the node
     *
     * @return  the bit code
     */
    public BitSet getCode () {
        return code;
    }

    /**
     * Sets the value of the nodes bit code
     *
     * @param newCode   the value to set it to
     */
    public void setCode (BitSet newCode) {
        code = newCode;
    }
}
