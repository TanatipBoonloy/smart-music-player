package kmitl.ce.smart_music_player.entity;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dell on 25/3/2560.
 */

public class RealmPlaylistInformation extends RealmObject {

    @PrimaryKey
    private int id;
    private String playlistName;
//    private ArrayList<Integer> songs;
    private byte[] thumnail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }




    public byte[] getThumnail() {
        return thumnail;
    }

    public void setThumnail(byte[] thumnail) {
        this.thumnail = thumnail;
    }
}
