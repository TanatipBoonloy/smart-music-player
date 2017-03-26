package kmitl.ce.smart_music_player.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
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

/**
 * Created by Dell on 25/3/2560.
 */

public class ManagePlaylistRealm {
    private Context mContext;
    Realm realm;

    public ManagePlaylistRealm(Context context,Realm realm){
        this.realm=realm;
        this.mContext=context;
    }

    public void getAllPlaylist()
    {
        if(realm.where(RealmPlaylistInformation.class).count() > 0) {

        }
    }


    //READ
    public ArrayList<String> retrieve()
    {
        ArrayList<String> playlistNames=new ArrayList<>();
        RealmResults<RealmPlaylistInformation> playlists=realm.where(RealmPlaylistInformation.class).findAll();
        for(RealmPlaylistInformation s:playlists)
        {
            playlistNames.add(s.getPlaylistName());
        }
        return playlistNames;
    }

}
