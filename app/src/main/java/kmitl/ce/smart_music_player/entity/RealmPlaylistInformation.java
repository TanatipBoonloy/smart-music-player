package kmitl.ce.smart_music_player.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dell on 25/3/2560.
 */

public class RealmPlaylistAllInformation extends RealmObject {

    @PrimaryKey
    private int id;

    private byte[] thumnail;
    private String[] playlists;
    private String[][] Songs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getThumnail() {
        return thumnail;
    }

    public void setThumnail(byte[] thumnail) {
        this.thumnail = thumnail;
    }

    public String[] getPlaylists() {
        return playlists;
    }

    public void setPlaylists(String[] playlists) {
        this.playlists = playlists;
    }

    public String[][] getSongs() {
        return Songs;
    }

    public void setSongs(String[][] songs) {
        Songs = songs;
    }


}
