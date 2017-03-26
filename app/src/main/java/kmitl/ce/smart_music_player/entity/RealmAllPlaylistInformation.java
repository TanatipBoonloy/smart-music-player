package kmitl.ce.smart_music_player.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dell on 25/3/2560.
 */

public class RealmAllPlaylistInformation extends RealmObject{

    @PrimaryKey
    private int id;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    private String[] playlists;
//
//    public String[] getPlaylists() {
//        return playlists;
//    }
//
//    public void setPlaylists(String[] playlists) {
//        this.playlists = playlists;
//    }
}
