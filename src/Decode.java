import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin Pietrow on 4/20/15.
 */
public class Decode {
    public static void main (String[] args) {
        // String sourcefile = args[0];

        // Map<Character, String> canonicalCodes = new HashMap<Character, String>(); // How many of each character in the file

        // canonicalCodes = readInInput(canonicalCodes, sourcefile);   // Populate with character counts


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
     * @param   canonicalCodes    A blank map to insert characters into
     * @param   sourcefile  The file to pull information from
     * @return              The map of characters with how many times they appear in the file
     *
    private static Map<Character, String> readInInput(Map<Character, String> canonicalCodes, String sourcefile) {

        Charset charset = Charset.defaultCharset();

        // Construct the methods to retrieve the file's information
        try (InputStream in = new FileInputStream(sourcefile)) {

            Reader reader = new InputStreamReader(in, charset);

            int data;
            char character;
            int numChars = 0;

            // Read in the canonical info
            // Get number of characters
            if ((data = reader.read()) != -1) {
                numChars = data;
            }

            for (int i = 0; i < numChars; i++) {
                // Character
                canonicalCodes.put((char) reader.read(), reader.read());

            }

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
    } */
}
