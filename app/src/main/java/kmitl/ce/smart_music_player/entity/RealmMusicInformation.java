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
    private String title;
    private byte[] thumnail;
    private String artist;
    private String year;
    private String genre;
    private String album;
    private String path;
    private String fileName;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumnail(byte[] thumnail) {
        this.thumnail = thumnail;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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

    public String getTitle() {
        return title;
    }

    public byte[] getThumnail() {
        return thumnail;
    }

    public String getArtist() {
        return artist;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
}
