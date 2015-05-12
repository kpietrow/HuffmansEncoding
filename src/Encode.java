
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Kevin Pietrow on 4/22/15.
 */
public class Encode {
    public static void main (String[] args) {
        String sourcefile = args[0];
        // String targetfile = args[1];

        Map<Character, Integer> characterCount = new HashMap<Character, Integer>(); // How many of each character in the file

        characterCount = readInInput(characterCount, sourcefile);   // Populate with character counts

        List<Node> nodes = new LinkedList<Node>();  // To hold nodes of characters and values
        generateNodes(characterCount, nodes);   // Populate nodes

        /*
        for (Node node : nodes) {
            System.out.println(node.name + " " + node.value);
        }*/

        // Generate Huffman tree, and return its nodes
        nodes = treeGeneration(nodes);

        printTree(nodes.get(0), 0);
        System.out.println("\n");

        // Retrieve leaf nodes, and set their codes
        List<Node> huffmanNodes = traverseToFindHuffmanCodes(new ArrayList<Node>(), nodes.get(0), new BitSet(), 0);


        for (int i = 0; i < huffmanNodes.size(); i++) {
            System.out.println("node char: " + huffmanNodes.get(i).getName() + ", code: " + huffmanNodes.get(i).getCode());
        }


    }

    /**
     * Generates a Huffman tree
     *
     * @param nodes The list of nodes to generate from
     */
    private static List<Node> treeGeneration (List<Node> nodes) {

        double min = Double.MAX_VALUE;
        int index = -1;

        // Huffman algorithm
        while (nodes.size() >= 2) {
            // Find the smallest node
            for (Node node : nodes) {
                if (node.getValue() < min) {
                    index = nodes.lastIndexOf(node);
                    min = node.getValue();
                }
            }

            // Remove the smallest node
            Node smallest = nodes.remove(index);

            min = Double.MAX_VALUE; index = -1;

            // Find the second smallest node
            for (Node node : nodes) {
                if (node.getValue() < min) {
                    index = nodes.lastIndexOf(node);
                    min = node.getValue();
                }
            }

            // Construct the new node
            Node secondSmallest = nodes.remove(index);
            Node newNode = new Node(smallest.getValue() + secondSmallest.getValue());
            newNode.setLeft(smallest);
            newNode.setRight(secondSmallest);

            // Add to the list
            nodes.add(newNode);
        }

        return nodes;
    }

    private static void generateCanonicalCodes (Map<Node, Integer> lengthMap) {

        // Sort them!


    }

    /**
     * Finds the bit codes of each leaf in the Huffman tree
     *
     * @param nodeList      the list of nodes
     * @param node          the node being evaluated
     * @param currentCode   the current BitSet code for the Huffman tree
     * @return
     */
    private static List<Node> traverseToFindHuffmanCodes (List<Node> nodeList, Node node, BitSet currentCode, int depth) {

        if (node.getLeft() == null && node.getRight() == null) {

            // An odd base case where there was only one node:
            if (depth == 0) {
                nodeList.add(node);
            } else {
                node.setCode(currentCode.get(0, currentCode.size() - 1));
                nodeList.add(node);
            }

        } else {

            if (node.getLeft() != null) {
                currentCode.set(depth, false);
                traverseToFindHuffmanCodes(nodeList, node.getLeft(), currentCode, ++depth);;

            } if (node.getRight() != null){
                currentCode.set(depth, true);
                traverseToFindHuffmanCodes(nodeList, node.getRight(), currentCode, ++depth);
            }
        }

        return nodeList;

    }

    /**
     * Creates a list of nodes for tree generation
     *
     * @param characters    The map of characters to values
     * @param nodes         The to-be-created list of nodes
     */
    private static void generateNodes (Map<Character, Integer> characters, List<Node> nodes) {

        for (Character key : characters.keySet()) {
            nodes.add(new Node(key, characters.get(key)));
        }

        return;
    }

    /**
     * Returns a map of characters mapped to how many times they appear in the input file.
     *
     * @param   inputMap    A blank map to insert characters into
     * @param   sourcefile  The file to pull information from
     * @return              The map of characters with how many times they appear in the file
     */
    private static Map<Character, Integer> readInInput(Map<Character, Integer> inputMap, String sourcefile) {

        Charset charset = Charset.defaultCharset();

        // Construct the methods to retrieve the file's information
        try (InputStream in = new FileInputStream(sourcefile)) {

            Reader reader = new InputStreamReader(in, charset);

            int data;
            char character;

            // Iterate over the file
            while ((data = reader.read()) != -1) {
                character = (char) data;

                // If we have the character already, increment its value in the map
                if (inputMap.containsKey(character)) {
                    inputMap.put(character, inputMap.get(character) + 1);

                    // Else add it
                } else {
                    inputMap.put(character, 1);
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading in information");
        }

        return inputMap;
    }



    private static void printTree (Node node, int depth) {
        if (node.getLeft() == null && node.getRight() == null) {
            System.out.println();
            for (int i = depth; i > 0; i--) {
                System.out.print("  ");
            }
            System.out.print(node.getName());

        } else {

            if (node.getLeft() != null) {
                printTree(node.getLeft(), ++depth);;

            } if (node.getRight() != null){
                printTree(node.getRight(), ++depth);
            }
        }
    }

}
