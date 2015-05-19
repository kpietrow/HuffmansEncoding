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
        StringBuilder sb = readInInput(nodes, canonicalCodes, sourcefile);   // Populate with character info

        // Generate the new file
        generateNewFile(nodes, canonicalCodes, sb, "test.txt");

    }

    /**
     * Generates the decoded text file
     *
     * @param nodes     the huffman nodes
     * @param canonicalCodes    the map of canonical codes
     * @param sb            the StringBuilder which contains the file to decode
     * @param outputfile    the file to print to
     */
    private static void generateNewFile (List<HuffmanNode> nodes, Map<String, Character> canonicalCodes, StringBuilder sb, String outputfile) {
        StringBuilder currentString = new StringBuilder();
        DataOutputStream writer = null;

        String eof = "";

        // Find the code for EOF
        for (String key : canonicalCodes.keySet()) {
            if (canonicalCodes.get(key) == '\u0000') {
                eof = key;
            }
        }

        try {
            // Get our writer set up
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputfile)));

            // Loop through the file text
            for (int i = 0; i < sb.length(); i++) {
                currentString.append(sb.charAt(i));

                // If we have a match to a canonical code...
                if (canonicalCodes.containsKey(currentString.toString())) {
                    System.out.println("hey!");

                    // If that code is the EOF character
                    if (currentString.toString().equals(eof)) {
                        System.out.println("end");
                        // Halt execution
                        writer.flush();
                        writer.close();
                        return;
                    }

                    // Write it to the new file
                    writer.write((int) canonicalCodes.get(currentString.toString()));

                    // Clear the current string
                    currentString.delete(0, currentString.length());
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while printing file");
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (Exception e) {
                // Hopefully we never get here
            }
        }
    }

    /**
     * Returns a map of characters mapped to how many times they appear in the input file.
     *
     * @param   nodes    A list to insert the character nodes into
     * @param   canonicalCodes  A map to store the canonical codes
     * @param   sourcefile  The file to pull information from
     */
    private static StringBuilder readInInput (List<HuffmanNode> nodes, Map<String, Character> canonicalCodes, String sourcefile) {

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
            while (true) {
                int bite = (int) reader.readByte();
                String code = Integer.toBinaryString(bite);

                // Handle 2's complement
                if (code.length() > 8) {
                    code = code.substring(code.length() - 8, code.length());
                }

                sb.append(code);

                if (bite == 0) {
                    sb.append("0000000");
                }
            }

        } catch (EOFException eof) {
            // This is the expected error
        } catch (Exception e) {
            System.out.println("Error reading in information");
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                // Nothing
            }
        }

        return sb;

    }

    /**
     * Generates the canonical codes of the nodes read in from the file
     *
     * @param  huffmanNodes the nodes
     * @param  canonicalCodes   the map of codes to be generated
     */
    private static void generateCanonicalCodes (List<HuffmanNode> huffmanNodes, Map<String, Character> canonicalCodes) {
        int code = 0;
        String stringCode = "";
        int previousCodeLength = huffmanNodes.get(0).getCodeLength();

        // Generate codes
        for (int i = 0; i < huffmanNodes.size(); i++) {
            // Convert the code to binary
            stringCode = Integer.toBinaryString(code);

            // Concat 0's to the front until it's the correct length
            for (; stringCode.length() < previousCodeLength ;) {
                stringCode = "0" + stringCode;
            }

            // Handle if this code is shorter than the previous
            if (stringCode.length() > huffmanNodes.get(i).getCodeLength()) {
                // Splice the current code string
                stringCode = stringCode.substring(0, huffmanNodes.get(i).getCodeLength());

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
            huffmanNodes.get(i).setCode(stringCode);

            // Update the previous code length
            previousCodeLength = huffmanNodes.get(i).getCode().length();

            // Add to the map
            canonicalCodes.put(huffmanNodes.get(i).getCode(), huffmanNodes.get(i).getCharacter());

            // Increment the code amount
            code++;
        }

        for (int i = 0; i < huffmanNodes.size(); i++) {
            System.out.println("node char: " + huffmanNodes.get(i).getCharacter() + ", code: " + huffmanNodes.get(i).getCode() + ", code length: " + huffmanNodes.get(i).getCodeLength());
        }

    }


}
