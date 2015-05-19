/**
 * Created by Kevin Pietrow on 4/22/15.
 */
public class HuffmanNode {
    String code = "";
    Character character = null;
    int codeLength = 0;

    /**
     * The default constructor
     *
     * @param newChar
     * @param newCodeLength
     */
    public HuffmanNode (Character newChar, int newCodeLength) {
        character = newChar;
        codeLength = newCodeLength;
    }

    /**
     * Standard 'get' for the character attribute
     *
     * @return  the character attribute
     */
    public Character getCharacter () {
        return character;
    }

    /**
     * Standard 'get' for the codeLength attribute
     *
     * @return  the codeLength attribute
     */
    public int getCodeLength () {
        return codeLength;
    }

    /**
     * Standard 'get' for the code attribute
     *
     * @return  the code attribute
     */
    public String getCode () {
        return code;
    }

    /**
     * Standard 'set' for the code attribute
     *
     */
    public void setCode (String newCode) {
        code = newCode;
    }

    /**
     * Standard 'set' for the character attribute
     *
     */
    public void setCharacter (Character newChar) {
        character = newChar;
    }

    /**
     * Standard 'set' for the character attribute
     *
     */
    public void setCodeLength (int newLength) {
        codeLength = newLength;
    }
}
