import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin Pietrow on 4/20/15.
 */
public class Decode {
    public static void main (String[] args) {
        String sourcefile = args[0];

        Map<String, Character> canonicalCodes = new HashMap<String, Character>();
        List<HuffmanNode> nodes = new ArrayList<HuffmanNode>();
        readInInput(nodes, canonicalCodes, sourcefile);   // Populate with character info


        /**
         * TODO:
         * Get a List of characters with code lengths
         * Sort the List and generate canonical codes in a manner similar to that of Encode.java
         * Store the results to a Map
         * Read in the data and convert to characters, and print
         */

    }

    /**
     * Returns a map of characters mapped to how many times they appear in the input file.
     *
     * @param   nodes    A list to insert the character nodes into
     * @param   canonicalCodes  A map to store the canonical codes
     * @param   sourcefile  The file to pull information from
     */
    private static void readInInput (List<HuffmanNode> nodes, Map<String, Character> canonicalCodes, String sourcefile) {

        Charset charset = Charset.defaultCharset();
        DataInputStream reader = null;
        StringBuilder sb = new StringBuilder();

        // Construct the methods to retrieve the file's information
        try {
            // Create our reader
            reader = new DataInputStream((new BufferedInputStream(new FileInputStream(sourcefile))));

            // Read in those canonical codes
            byte numCodes = reader.readByte();
            int number = numCodes & 0xFF;

            Character character;
            int codeLength;

            for (int i = 0; i < number; i++) {
                // Character
                character = (char) reader.readByte();
                // Code
                codeLength = reader.readByte();

                nodes.add(new HuffmanNode(character, codeLength));
            }

            generateCanonicalCodes(nodes, canonicalCodes);

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
     *
     */
    private static void generateCanonicalCodes (List<HuffmanNode> huffmanNodes, Map<String, Character> canonicalCodes) {
        int code = 0;
        String stringCode = "";
        int previousCodeLength = huffmanNodes.get(0).codeLength;

        // Generate codes
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


}
