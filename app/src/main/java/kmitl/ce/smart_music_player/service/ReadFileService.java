package kmitl.ce.smart_music_player.service;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.model.MusicInformation;

/**
 * Created by Jo on 8/21/2016.
 */
public class ReadFileService {
    private String path;
    private List<MusicInformation> musicInformationList;

    public ReadFileService() {
        path = Environment.getExternalStorageDirectory().getPath() + "/Music/";
    }

    public List<MusicInformation> getAllMusicFile(List<RealmMusicInformation> realmMusicInformationList) {
        musicInformationList = new ArrayList<>();
        File externalFile = new File(path);
        scanDirectory(externalFile, realmMusicInformationList);

        return musicInformationList;
    }

    public void scanDirectory(File directory, List<RealmMusicInformation> realmMusicInformationList) {
        if (directory != null) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        scanDirectory(file, realmMusicInformationList);
                    }
                }
            } else {
                for (RealmMusicInformation rmif : realmMusicInformationList) {
                    System.out.println(rmif.getName() + "\n" + directory.getName().replace(".mp3", "") + "\n");
                    if (rmif.getName().equals(directory.getName().replace(".mp3", ""))) {
                        System.out.println("Add +++++++++++++++++++++++++++++++++++++++++++++");
                        addSongToList(directory, rmif.getId());
                    }
                }
            }
        }
    }

    public void addSongToList(File file, int index) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getPath());
        byte[] embededPic = mediaMetadataRetriever.getEmbeddedPicture();
        String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String year = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
        String genre = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mediaMetadataRetriever.release();

        MusicInformation musicInformation = new MusicInformation();
        musicInformation.setTitle(title);
        musicInformation.setThumbnail(embededPic);
        musicInformation.setArtist(artist);
        musicInformation.setYear(year);
        musicInformation.setGenre(genre);
        musicInformation.setAlbum(album);
        musicInformation.setDuration(Integer.parseInt(duration));
        musicInformation.setPath(file.getPath());
        musicInformation.setFileName(file.getName());
        musicInformation.setRealmIndex(index);

        musicInformationList.add(musicInformation);
    }
}
