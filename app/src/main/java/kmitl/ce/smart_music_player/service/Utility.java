package kmitl.ce.smart_music_player.service;

/**
 * Created by Administrator on September 27,2016.
 */

public class Utility {

    public static String subStringTitle(String str, int type) {
        if (type == 0) {//mRecycle
            if (str.length() > 22) {
                return str.substring(0, 22) + "...";
            } else return str;
        } else if (type == 1) {//MusicPlayingFragment
            if (str.length() > 29) {
                return str.substring(0, 29) + "...";
            } else return str;
        } else if (type == 2) {//MusicPlayingFragment
            if (str.length() > 50) {
                return str.substring(0, 50) + "...";
            } else return str;
        }
        return null;
    }
}
