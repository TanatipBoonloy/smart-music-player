package kmitl.ce.smart_music_player.model;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicInformation {
    private String title;
    private String artist;
    private int length;
    private byte[] thumbnail;
    private String path;
    private String fileName;
    private String year;
    private String album;
    private String genre;
    private Integer duration;

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum() {

        return album;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getYear() {

        return year;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {

        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public byte[] getThumbnail() {

        return thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String author) {
        this.artist = author;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTitle() {

        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getLength() {
        return length;
    }
}
