/**
 * Created by User on 5/18/15.
 */
public class HuffmanNode {
    String code = "";
    Character character = null;
    int codeLength = 0;

    public HuffmanNode (Character newChar, int newCodeLength) {
        character = newChar;
        codeLength = newCodeLength;
    }
}
