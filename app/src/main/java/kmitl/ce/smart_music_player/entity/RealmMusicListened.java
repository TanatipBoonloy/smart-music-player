package kmitl.ce.smart_music_player.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jo on 1/11/2017.
 */

public class RealmMusicListened extends RealmObject {
    @PrimaryKey
    private long id;

    private RealmMusicInformation realmMusicInformation;
    private Integer playTimed;

    public void setId(long id) {
        this.id = id;
    }

    public void setRealmMusicInformation(RealmMusicInformation realmMusicInformation) {
        this.realmMusicInformation = realmMusicInformation;
    }

    public void setPlayTimed(Integer playTimed) {
        this.playTimed = playTimed;
    }

    public long getId() {
        return id;
    }

    public RealmMusicInformation getRealmMusicInformation() {
        return realmMusicInformation;
    }

    public Integer getPlayTimed() {
        return playTimed;
    }
}
