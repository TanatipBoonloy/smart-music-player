package kmitl.ce.smart_music_player.service;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.entity.RealmPlaylistInformation;

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
