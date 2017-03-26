package kmitl.ce.smart_music_player.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.entity.RealmAllPlaylistInformation;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.entity.RealmPlaylistInformation;
import kmitl.ce.smart_music_player.model.PlaylistAllInformation;
import kmitl.ce.smart_music_player.model.PlaylistInformation;

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

    public static List<RealmPlaylistInformation> initializePlaylist(final Realm realm, Context context) throws Exception {
        RealmQuery<RealmPlaylistInformation> query = realm.where(RealmPlaylistInformation.class);
        RealmResults<RealmPlaylistInformation> result = query.findAll();

        if (result.size() < 1) {
            final SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            final Gson gson = new Gson();

            String GetJsonPlaylists = appSharedPrefs.getString("Playlists", "");
           final PlaylistAllInformation playlistAllInformation = gson.fromJson(GetJsonPlaylists, PlaylistAllInformation.class);

            if(playlistAllInformation!=null){


                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm r) {
                        int index = 1;
                        for (String str: playlistAllInformation.getPlaylists()){
                            String GetJsonPlaylist = appSharedPrefs.getString(str, "");
                            PlaylistInformation obj = gson.fromJson(GetJsonPlaylist,PlaylistInformation.class);

                            RealmPlaylistInformation rmif = r.createObject(RealmPlaylistInformation.class, index);
                            rmif.setPlaylistName(str);
//                            rmif.setSongs(obj.getSongs());
                            index++;
                        }
                    }
                });

                result = query.findAll();
            }

        }
        return result;
    }
}
