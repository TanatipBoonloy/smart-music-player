package kmitl.ce.smart_music_player.service;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;

/**
 * Created by Jo on 10/20/2016.
 */
public class DBInitialService {
    public static List<RealmMusicInformation> initialize(final Realm realm, Context context) throws Exception {
        RealmQuery<RealmMusicInformation> query = realm.where(RealmMusicInformation.class);
        RealmResults<RealmMusicInformation> result = query.findAll();

        if (result.size() < 1) {
            List<String> songName = new ArrayList<>();
//            List<Integer> durations = new ArrayList<>();
            InputStreamReader is = new InputStreamReader(context.getAssets()
                    .open("music_information.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String splitResult[] = line.split(",");
                songName.add(splitResult[6]);
//                String splitTime[] = splitResult[2].split(":");
//                durations.add(Integer.parseInt(splitTime[1].trim())*60 + Integer.parseInt(splitTime[2].trim()));
            }

            final List<String> songNameFinal = songName;
//            final List<Integer> durationsFinal = durations;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {
                    int index = 1;
                    for (String songName : songNameFinal) {
                        RealmMusicInformation rmif = r.createObject(RealmMusicInformation.class, index);
                        rmif.setName(songName);
//                        rmif.setLike(null);
//                        rmif.setListened(false);
//                        rmif.setPath(null);
//                        rmif.setDuration(durationsFinal.get(index - 1));
//                        rmif.setPlayed(0);
                        index++;
//                        System.out.println("add : " + (index - 1));
                    }
                }
            });

            result = query.findAll();
        }
        return result;
    }
}
