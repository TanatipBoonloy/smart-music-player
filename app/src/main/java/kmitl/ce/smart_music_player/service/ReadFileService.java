package kmitl.ce.smart_music_player.service;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.model.MusicInformation;

/**
 * Created by Jo on 8/21/2016.
 */
public class ReadFileService {
    private String path;
//    private List<MusicInformation> musicInformationList;

    public ReadFileService() {
        path = Environment.getExternalStorageDirectory().getPath();
    }

//    public List<MusicInformation> getAllMusicFile(Realm realm) {
    public void getAllMusicFile(Realm realm) {
//        musicInformationList = new ArrayList<>();
//        File musicDirectory = new File(path);
        if(realm.where(RealmMusicInformation.class).count() > 0) {
            File musicDirectory = getMusicDirectory(this.path);
            scanDirectory(musicDirectory, realm);
        }

//        return musicInformationList;
    }

    private File getMusicDirectory(String path) {
        String[] directorys = path.split("/");
        File rootDirectory = null;
        for (String directory : directorys) {
            if (directory.length() > 0) {
                rootDirectory = new File(directory);
                break;
            }
        }
        return findMusicDirectory(rootDirectory);
    }

    private File findMusicDirectory(File directory) {
        if (directory.exists()) {
            if (directory.isDirectory()) {
                if (directory.getName().equals("ProjectMusic")) {
                    return directory;
                } else {
                    File[] directories = directory.listFiles();
                    if (directories != null && directories.length > 0) {
                        for (File eachDirectory : directories) {
                            File isMusicDirectory = findMusicDirectory(eachDirectory);
                            if (isMusicDirectory != null) {
                                return isMusicDirectory;
                            }
                        }
                        return null;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void scanDirectory(File directory, Realm realm) {
        if (directory != null) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        scanDirectory(file, realm);
                    }
                }
            } else {
                List<RealmMusicInformation> realmMusicInformationList = realm.where(RealmMusicInformation.class).findAll();
                for (RealmMusicInformation rmif : realmMusicInformationList) {
//                    System.out.println(rmif.getName() + "\n" + directory.getName().replace(".mp3", "") + "\n");
                    if (rmif.getName().equals(directory.getName().replace(".mp3", ""))) {
//                        System.out.println("Add +++++++++++++++++++++++++++++++++++++++++++++");
                        addSongToList(directory, rmif.getId(), realm);
                    }
                }
            }
        }
    }

    public void addSongToList(File file, int index, Realm realm) {
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

//        MusicInformation musicInformation = new MusicInformation();
//        musicInformation.setTitle(title);
//        musicInformation.setThumbnail(embededPic);
//        musicInformation.setArtist(artist);
//        musicInformation.setYear(year);
//        musicInformation.setGenre(genre);
//        musicInformation.setAlbum(album);
//        musicInformation.setDuration(Integer.parseInt(duration));
//        musicInformation.setPath(file.getPath());
//        musicInformation.setFileName(file.getName());
//        musicInformation.setRealmIndex(index);

        RealmMusicInformation realmObj = realm.where(RealmMusicInformation.class).equalTo("id", index).findFirst();
        realm.beginTransaction();
        realmObj.setTitle(title);
        realmObj.setThumnail(embededPic);
        realmObj.setArtist(artist);
        realmObj.setYear(year);
        realmObj.setGenre(genre);
        realmObj.setAlbum(album);

        try{
            realmObj.setDuration(Integer.parseInt(duration));
        }catch (Exception ex){
            System.out.println("Read file service error "+ex.toString());
        }
        realmObj.setPath(file.getPath());
        realmObj.setFileName(file.getName());
        realm.commitTransaction();

//        musicInformationList.add(musicInformation);
    }
}
