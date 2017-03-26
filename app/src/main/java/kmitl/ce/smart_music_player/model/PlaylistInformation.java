package kmitl.ce.smart_music_player.model;

/**
 * Created by Dell on 24/3/2560.
 */

public class PlaylistInformation {
    private String playlistName;
    private String[] songs;
    private byte[] thumnail;

    public byte[] getThumnail() {
        return thumnail;
    }

    public void setThumnail(byte[] thumnail) {
        this.thumnail = thumnail;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String[] getSongs() {
        return songs;
    }

    public void setSongs(String[] songs) {
        this.songs = songs;
    }
}
