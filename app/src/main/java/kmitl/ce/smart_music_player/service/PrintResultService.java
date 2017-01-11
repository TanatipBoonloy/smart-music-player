package kmitl.ce.smart_music_player.service;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.entity.RealmMusicListened;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jo on 10/20/2016.
 */
public class PrintResultService {
    public static void printResult(Context context,Realm realm) {
        try{
            String songsPath = Environment.getExternalStorageDirectory().getPath() + "/Results/music_result.csv";
            String listenedPath = Environment.getExternalStorageDirectory().getPath() + "/Results/music_listened.csv";

            File externalFile = new File(songsPath);
            FileOutputStream outputStream = new FileOutputStream(externalFile);
            RealmQuery<RealmMusicInformation> query = realm.where(RealmMusicInformation.class);
            RealmResults<RealmMusicInformation> result = query.findAll();

//            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for(RealmMusicInformation realmMusicInformation : result) {
                String string = realmMusicInformation.getName() + "," + realmMusicInformation.getPlayedTime() + "," + realmMusicInformation.getDuration() + "\n";
                outputStream.write(string.getBytes());
            }

            outputStream.close();

            externalFile = new File(listenedPath);
            outputStream = new FileOutputStream(externalFile);
            RealmQuery<RealmMusicListened> listenedRealmQuery = realm.where(RealmMusicListened.class);
            RealmResults<RealmMusicListened> listenedRealmResults = listenedRealmQuery.findAll();

            for(RealmMusicListened realmMusicListened : listenedRealmResults ) {
                String string = realmMusicListened.getId() + "," + realmMusicListened.getRealmMusicInformation().getId() + "," + realmMusicListened.getPlayTimed() + "\n";
                outputStream.write(string.getBytes());
            }

            outputStream.close();

            Toast.makeText(context, "Result Saved.",
                    Toast.LENGTH_LONG).show();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
