package kmitl.ce.smart_music_player.model;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicInformation {
    private String title;
    private String author;
    private int length;
    private String thumbnail;

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {

        return thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTitle() {

        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getLength() {
        return length;
    }
}
