package kmitl.ce.smart_music_player.service;

/**
 * Created by Administrator on September 27,2016.
 */

public class Utility {

    public static String subStringTitle(String str, int type) {
        if (type == 0) {//mRecycle
            if (str.length() > 28) {
                return str.substring(0, 28) + "...";
            } else return str;
        } else if (type == 1) {//MusicPlayingFragment
            if (str.length() > 29) {
                return str.substring(0, 29) + "...";
            } else return str;
        } else if (type == 2) {//MusicPlayingFragment
            if(str!=null){
                if (str.length() > 30) {
                    return str.substring(0, 30) + "...";
                } else return str;
            }
            return "null";
        }
        return null;
    }
}
