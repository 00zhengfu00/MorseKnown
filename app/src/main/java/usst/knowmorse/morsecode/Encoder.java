package usst.knowmorse.morsecode;

import java.util.HashMap;

/**
 * class  编码
 * @author cunbao   <1063994955@qq.com>
 * @version  v2.0
 * @time    20160508
 * @修改时间 20160618
 * @修改人  cunbao
 */
public class Encoder {

    private HashMap<Character, String> charToMorseMap = new HashMap<>();

    public Encoder() {
       charToMorseMap = new Mapper().getCharToMorseMap();
    }


    /**
     * @param string String which we need encoded to morse.
     * @return The morse representation of given string.
     */
    public String encode(String string) {

        string = string.toLowerCase();

        String[] words = string.split(" ");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            char[] chars = word.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                String morse = charToMorseMap.get(c);
                builder.append(morse);
                //This is so there is not 4 spaces between words we don't want to append to end of last char in word
                if(j!= chars.length-1){
                    builder.append(C.CHARACTER_SEPERATOR);
                }
            }
            builder.append(C.WORD_SEPERATOR);
        }

        String withOutInvalidCharacters = builder.toString().replace("null", "");

        return withOutInvalidCharacters;
    }
}
