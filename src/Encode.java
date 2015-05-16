
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
        nodes.add(0, new Node('\u0000', 1));          // Add our EOF node

        for (int i = 0; i < nodes.size(); i++) {
            System.out.println("node char: " + nodes.get(i).getName() + ", frequency: " + nodes.get(i).getValue());
        }

        // Generate Huffman tree, and return its nodes
        nodes = treeGeneration(nodes);

        printTree(nodes.get(0), 0);
        System.out.println("\n");

        // Retrieve leaf nodes, and set their codes
        List<Node> huffmanNodes = traverseToFindHuffmanCodes(new ArrayList<Node>(), nodes.get(0), "", 0);


        for (int i = 0; i < huffmanNodes.size(); i++) {
            System.out.println("node char: " + huffmanNodes.get(i).getName() + ", code: " + huffmanNodes.get(i).getCode());
        }

        System.out.println();

        // Sort the Nodes by our custom compareTo method
        Collections.sort(huffmanNodes);

        for (int i = 0; i < huffmanNodes.size(); i++) {
            System.out.println("node char: " + huffmanNodes.get(i).getName() + ", code: " + huffmanNodes.get(i).getCode());
        }

        // Generate Canonical Codes
        generateCanonicalCodes(huffmanNodes);

        System.out.println();

        for (int i = 0; i < huffmanNodes.size(); i++) {
            System.out.println("node char: " + huffmanNodes.get(i).getName() + ", code: " + huffmanNodes.get(i).getFinalCode() + ", frequency: " + huffmanNodes.get(i).getValue());
        }

        // Store for effective lookup
        Map<Character, String> charToCodes = new HashMap<Character, String>();
        storeCodes(charToCodes, nodes);

        // Write it to the file!
        writeToFile(charToCodes, nodes, sourcefile, "test.huf");
    }

    /**
     * Writes the canonical tree and the coded data to an output file
     *
     * @param map   the map of characters to Huffman codes
     * @param nodes the list of nodes in the huffman tree
     * @param sourcefile    the source file
     * @param outputfile    the file to write to
     */
    private static void writeToFile (Map<Character, String> map, List<Node> nodes, String sourcefile, String outputfile) {

        // Write the canonical tree
        // Get a Writer
        Writer writer = null;

        try {
            // Get our BufferedWriter all nice and set up
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile), "utf-8"));
            writer.write();


        } catch (IOException ex) {
            System.out.println("ERROR in writing to the file");


        } finally {
            try {
                writer.close();
            } catch (Exception ex) {

            }
        }

        Charset charset = Charset.defaultCharset();

        // Construct the methods to retrieve the file's information
        try (InputStream in = new FileInputStream(sourcefile)) {

            Reader reader = new InputStreamReader(in, charset);

            int data;
            char character;

            // Iterate over the file
            while ((data = reader.read()) != -1) {
                character = (char) data;


            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading in information");
        }

    }

    /**
     * Stores the node's character and their final code in a Map
     */
    private static void storeCodes (Map<Character, String> map, List<Node> nodes) {

        // Iterate over each node
        for (Node node : nodes) {
            map.put(node.getName(), node.getFinalCode());
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

            System.out.println("first node: " + smallest.getName() + ", second: " + secondSmallest.getName() + " value: " + smallest.getValue() + secondSmallest.getValue());


            Node newNode = new Node(smallest.getValue() + secondSmallest.getValue());
            newNode.setLeft(smallest);
            newNode.setRight(secondSmallest);

            // Add to the list
            nodes.add(newNode);
        }

        return nodes;
    }


    /**
     * Generates the canonical Huffman codes from a List of nodes
     *
     * @param huffmanNodes  the lists of nodes to generate canonical Huffman codes
     */
    private static void generateCanonicalCodes (List<Node> huffmanNodes) {

        int code = 0;
        String stringCode = "";
        int previousCodeLength = huffmanNodes.get(0).getCode().length();

        for (int i = 0; i < huffmanNodes.size(); i++) {
            // Convert the code to binary
            stringCode = Integer.toBinaryString(code);

            // Concat 0's to the front until it's the correct length
            for (; stringCode.length() < previousCodeLength ;) {
                stringCode = "0" + stringCode;
            }

            // Handle if this code is shorter than the previous
            if (stringCode.length() > huffmanNodes.get(i).getCode().length()) {
                // Splice the current code string
                stringCode = stringCode.substring(0, huffmanNodes.get(i).getCode().length());

                int binaryCount = 1;
                code = 0;

                // Adjust the code for the next node
                for (int j = stringCode.length() - 1; j >= 0; j--) {

                    // If the string code is 1 at a location, add it's binary equivalent
                    if (Integer.parseInt(Character.toString(stringCode.charAt(j))) == 1) {
                        code = code + binaryCount;
                    }

                    binaryCount = binaryCount * 2;
                }

            }

            // Update the node
            huffmanNodes.get(i).setFinalCode(stringCode);

            // Update the previous code length
            previousCodeLength = huffmanNodes.get(i).getCode().length();

            // Increment the code amount
            code++;
        }


    }

    /**
     * Finds the bit codes of each leaf in the Huffman tree
     *
     * @param nodeList      the list of nodes
     * @param node          the node being evaluated
     * @param currentCode   the current BitSet code for the Huffman tree
     * @return
     */
    private static List<Node> traverseToFindHuffmanCodes (List<Node> nodeList, Node node, String currentCode, int depth) {

        if (node.getLeft() == null && node.getRight() == null) {

            // An odd base case where there was only one node:
            if (depth == 0) {
                nodeList.add(node);
            } else {
                node.setCode(currentCode);
                nodeList.add(node);
            }

        } else {
            depth++;

            if (node.getLeft() != null) {
                traverseToFindHuffmanCodes(nodeList, node.getLeft(), currentCode + "0", depth);;
            }

            if (node.getRight() != null){
                traverseToFindHuffmanCodes(nodeList, node.getRight(), currentCode + "1", depth);
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


    /**
     * A helper method to print out a Huffman tree
     *
     * @param node  The current node
     * @param depth The current depth
     */
    private static void printTree (Node node, int depth) {
        System.out.println();
        for (int i = depth; i > 0; i--) {
            System.out.print("  ");
        }
        System.out.print(node.getName());

        if (node.getLeft() != null && node.getRight() != null) {
            depth++;

            if (node.getLeft() != null) {
                printTree(node.getLeft(), depth);;

            }

            if (node.getRight() != null){
                printTree(node.getRight(), depth);
            }
        }
    }

    private static int greatestDepth (Node node, int depth) {
        depth++;

        if (node.getLeft() != null && node.getRight() != null) {
            int depth1 = 0;
            int depth2 = 0;

            // Get depth of left side of node's children
            if (node.getLeft() != null) {
                depth1 = greatestDepth(node.getLeft(), depth);;

            }

            // Get depth of the right side of node's children
            if (node.getRight() != null){
                depth2 = greatestDepth(node.getRight(), depth);
            }

            // Find the greatest depth
            if (depth1 > depth2) {
                if (depth < depth1) {
                    depth = depth1;
                }
            } else {
                if (depth < depth2) {
                    depth = depth2;
                }
            }
        }

        return depth;
    }

}
