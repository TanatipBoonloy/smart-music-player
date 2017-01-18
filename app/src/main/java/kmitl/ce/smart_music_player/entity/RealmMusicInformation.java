package kmitl.ce.smart_music_player.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jo on 10/20/2016.
 */
public class RealmMusicInformation extends RealmObject {
    @PrimaryKey
    private int id;

    private String name;
//    private String path;
//    private Boolean like;
//    private Boolean listened;
    private Integer duration;
//    private Integer playedTime;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public void setLike(Boolean like) {
//        this.like = like;
//    }
//
//    public void setListened(Boolean listened) {
//        this.listened = listened;
//    }
//
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
//
//    public void setPlayed(Integer playedTime) {
//        this.playedTime = playedTime;
//    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public String getPath() {
//        return path;
//    }
//
//    public Boolean getLike() {
//        return like;
//    }
//
//    public Boolean getListened() {
//        return listened;
//    }
//
    public Integer getDuration() {
        return duration;
    }
//
//    public Integer getPlayedTime() {
//        return playedTime;
//    }
}
