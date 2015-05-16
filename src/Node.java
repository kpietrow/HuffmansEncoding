import java.util.BitSet;
import java.util.Comparator;

/**
 * Created by Kevin Pietrow on 4/22/15.
 */
public class Node implements Comparable<Node> {
    Node left = null;  // The two possible children
    Node right = null;
    Character name = '-';
    int value = 0;
    String code = "";
    String finalCode = "";


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

    public int compareTo (Node n) {
        int length1 = this.getCode().length();
        int length2 = n.getCode().length();
        int i = length1 > length2 ? -1 : length1 < length2 ? 1 : 0;

        if (i != 0)
            return i;

        i = this.getName().compareTo(n.getName());
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
    public String getCode () {
        return code;
    }

    /**
     * Sets the value of the node's bit code
     *
     * @param newCode   the value to set it to
     */
    public void setCode (String newCode) {
        code = newCode;
    }

    /**
     * Set the value of the node's final code
     *
     * @param code  the value of the final code
     */
    public void setFinalCode (String code) { finalCode = code; }

    /**
     * Get the value of the node's final code
     *
     * @return finalCode  the value of the final code
     */
    public String getFinalCode () { return finalCode; }
}
