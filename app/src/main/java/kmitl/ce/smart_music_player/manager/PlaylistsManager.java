package kmitl.ce.smart_music_player.manager;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.models.realm.RealmPlaylists;

/**
 * Created by Dell on 25/3/2560.
 */

public class PlaylistsManager {
    private Context mContext;
    Realm realm;

    public PlaylistsManager(Context context, Realm realm){
        this.realm=realm;
        this.mContext=context;
    }

    public void getAllPlaylist()
    {
        if(realm.where(RealmPlaylists.class).count() > 0) {

        }
    }


    //READ
    public ArrayList<String> retrieve()
    {
        ArrayList<String> playlistNames=new ArrayList<>();
        RealmResults<RealmPlaylists> playlists=realm.where(RealmPlaylists.class).findAll();
        for(RealmPlaylists s:playlists)
        {
            playlistNames.add(s.getPlaylistName());
        }
        return playlistNames;
    }

}
