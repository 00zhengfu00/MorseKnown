package usst.knowmorse.morsecode;

import java.util.HashMap;

/**
 * 功能：对莫尔斯码进行译码出字符
 * @author cunbao
 * @version  v1.0
 * @time    20160515
 * @修改时间
 * @修改人
 */
public class Decoder {

	public static final String INVALID_CHAR_MESSAGE = "[?]";

    private HashMap<String, Character> map = new HashMap<>();

    public Decoder(){
       map = new Mapper().getMorseToCharMap();
    }

    /**
     * @param string String we need decoded from Morse.
     * @return The decoded version of the given string.
     */
    public String decode(String string) {

        string = string.trim();

        String[] words = string.split(C.WORD_SEPERATOR);

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < words.length; i++) {

            String word = words[i];

            word = word.trim();

            String[] characters = word.split(String.valueOf(C.CHARACTER_SEPERATOR));

            for(int j = 0; j < characters.length; j++) {
	            Character c = map.get(characters[j]);

	            //Check if we found a valid match for our morse code sequence, if not say it is invalid
	            if(c != null) builder.append(c);
	            else builder.append(INVALID_CHAR_MESSAGE);
            }
            builder.append(" ");
        }
        return builder.toString();
    }


    /**
     * Created by cunbao on 2016/4/13.
     */
    /**
     * 图像处理类
     * @author cunbao
     *@param
     * w1:取一正方形识别区域   其左上点的横坐标
     * w2:其右上点的横坐标
     * h1:其左（右）上点的纵坐标
     * h2:其左（右）下点的纵坐标
     *   revised by cunbao 20160523
     */

        private static int decodeYUV420SPtoBrghtinessSum(byte[] yuv420sp, int w1,int h1,int w2,int h2,int width, int height) {
            if (yuv420sp == null) return 0;

            final int frameSize = width * height;

            int sum = 0; int brightness=0;
            for (int j = h1, yp = 0; j < h2; j++) {
                int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
                for (int i = w1; i < w2; i++, yp++) {
                    int y = (0xff & yuv420sp[yp]) - 16;
                    if (y < 0) y = 0;
                    if ((i & 1) == 0) {
                        v = (0xff & yuv420sp[uvp++]) - 128;
                        u = (0xff & yuv420sp[uvp++]) - 128;
                    }
                  /*  int y1192 = 1192 * y;
                    int r = (y1192 + 1634 * v);
                    int g = (y1192 - 833 * v - 400 * u);
                    int b = (y1192 + 2066 * u);

                    if (r < 0) r = 0;
                    else if (r > 262143) r = 262143;
                    if (g < 0) g = 0;
                    else if (g > 262143) g = 262143;
                    if (b < 0) b = 0;
                    else if (b > 262143) b = 262143;

                    int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                    int red = (pixel >> 16) & 0xff;
                    sum += red;*/
                    brightness+=y; //( 299 * r + 587 * g + 114 * b)/1000;
                }
            }





          /*  for (int j = 0, yp = 0; j < height; j++) {
                int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
                for (int i = 0; i < width; i++, yp++) {
                    int y = (0xff & yuv420sp[yp]) - 16;
                    if (y < 0) y = 0;
                    if ((i & 1) == 0) {
                        v = (0xff & yuv420sp[uvp++]) - 128;
                        u = (0xff & yuv420sp[uvp++]) - 128;
                    }
                    int y1192 = 1192 * y;
                    int r = (y1192 + 1634 * v);
                    int g = (y1192 - 833 * v - 400 * u);
                    int b = (y1192 + 2066 * u);

                    if (r < 0) r = 0;
                    else if (r > 262143) r = 262143;
                    if (g < 0) g = 0;
                    else if (g > 262143) g = 262143;
                    if (b < 0) b = 0;
                    else if (b > 262143) b = 262143;

                    int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                    int red = (pixel >> 16) & 0xff;
                    sum += red;
                    brightness+=y; //( 299 * r + 587 * g + 114 * b)/1000;
                }
            }*/
            return brightness;
        }

        /**
         * 外部调用
         * amount of red in the image. Note: returns 0 if the byte array is NULL.
         *
         * @param yuv420sp
         *            Byte array representing a yuv420sp image
         * @param width
         *            Width of the image.
         * @param height
         *            Height of the image.
         * @return int representing the average amount of red in the image.
         */
        public static int decodeYUV420SPtoBrightnessAvg(byte[] yuv420sp, int w1,int h1,int w2,int h2,int width, int height) {
            if (yuv420sp == null) return 0;

            final int frameSize = width * height;

            int sum = decodeYUV420SPtoBrghtinessSum(yuv420sp,w1,h1,w2,h2, width, height);
            return (sum / frameSize);
        }








}
