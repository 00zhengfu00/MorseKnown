package usst.knowmorse.morsecode;

import android.app.Fragment;
import android.content.SharedPreferences;

/**
 * 功能：定义点化等常量
 * @author cunbao
 * @version  v1.0
 * @time    20160510
 * @修改时间 20160520
 * @修改人  cunbao
 */
public class C {

    // ONE SPACE
    public static final char CHARACTER_SEPERATOR = ' ';
    //THREE SPACES, NOT TAB.
    public static final String WORD_SEPERATOR = "/";

    public static final char WORD_SEPERATOR_PLACEHOLDER = '$';

    public static final char DOT = '.';

    public static final char DASH = '-';


    public static  int DOT_TIME_INTERVAL = 250;

    public static  int DASH_TIME_INTERVAL = DOT_TIME_INTERVAL*3;

    public static  int CHARACTER_SEPERATOR_TIME_INTERVAL = DOT_TIME_INTERVAL*3;

    public static  int WORD_SEPERATOR_TIME_INTERVAL = DOT_TIME_INTERVAL*7;

}
