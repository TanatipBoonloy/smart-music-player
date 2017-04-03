package kmitl.ce.smart_music_player.ui;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.entity.RealmPlaylistInformation;
import kmitl.ce.smart_music_player.model.PlaylistAllInformation;
import kmitl.ce.smart_music_player.model.PlaylistInformation;
import kmitl.ce.smart_music_player.service.DBInitialService;

/**
 * Created by Dell on 22/3/2560.
 */

public class PlaylistFragment extends Fragment implements PlaylistListAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Realm realm;
    private TextView addNewPlaylist;
    private int playlistMax=15;
    private PlaylistListAdapter.OnItemClickListener listener;



    @Override
    public void onItemClicked(View v) {

        PlaylistListAdapter.PlaylistViewHolder playlistViewHolder = (PlaylistListAdapter.PlaylistViewHolder) v.getTag();

        int position = playlistViewHolder.getAdapterPosition();

        RealmPlaylistInformation realmPlaylistInformation= realm.where(RealmPlaylistInformation.class)
                .findAll()
                .get(position);

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(realmPlaylistInformation.getPlaylistName(), "");
        PlaylistInformation obj = gson.fromJson(json, PlaylistInformation.class);
        RealmList<RealmMusicInformation> listMusicInformation = new RealmList<RealmMusicInformation>();

        if(obj!=null & obj.getSongs()!=null){
            for (int i=0; i<obj.getSongs().length;i++){
                listMusicInformation.add(i,realm.where(RealmMusicInformation.class)
                        .equalTo("id",Integer.parseInt(obj.getSongs()[i])).findFirst());
            }
        }else{

        }

        CustomPlaylistFragment df= new CustomPlaylistFragment()
                .newInstance(realmPlaylistInformation,listMusicInformation);
        df.show(getFragmentManager(), "musicPlayingDialog");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.playlist_fragment, container, false);
        this.realm = Realm.getDefaultInstance();

//        try {
//            DBInitialService.initialize(this.realm, getContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemViewCacheSize(100);
        this.mRecyclerView.setDrawingCacheEnabled(true);
        this.mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        listener = this;
        this.mAdapter = new PlaylistListAdapter(listener,this.getActivity() , this.realm);

        this.mRecyclerView.setAdapter(this.mAdapter);



        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Add New Playlist");
        dialog.setContentView(R.layout.create_playlist);


        Button buttonCancel = (Button) dialog.findViewById(R.id.button_cancel);
        Button buttonOK = (Button) dialog.findViewById(R.id.button_ok);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newPlaylist = (EditText) dialog.findViewById(R.id.playlist_name);
                savePlaylist(newPlaylist.getText().toString());
                dialog.dismiss();

            }
        });

        this.addNewPlaylist= (TextView) rootView.findViewById(R.id.addNewPlaylist);
        this.addNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        return rootView;
    }

    private void savePlaylist(final String str){
        final SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        final Gson gson = new Gson();

        String GetJson = appSharedPrefs.getString("Playlists", "");
        PlaylistAllInformation playlistAllInformation = gson.fromJson(GetJson, PlaylistAllInformation.class);

        if (playlistAllInformation!=null && playlistAllInformation.getPlaylists()!=null){
            String[] oldArray = playlistAllInformation.getPlaylists();
            String[] array=new String[oldArray.length+1];
            for (int i=0;i<oldArray.length;i++){
                array[i]=oldArray[i];
            }
            array[array.length-1]=str;
            playlistAllInformation.setPlaylists(array);
        }else {
            playlistAllInformation=new PlaylistAllInformation();
            String[] array = new String[1];
            array[0]=str;
            playlistAllInformation.setPlaylists(array);
        }

        PlaylistInformation playlistInformation=new PlaylistInformation();
        playlistInformation.setPlaylistName(str);
        String PutJsonPlaylistInformation = gson.toJson(playlistInformation);
        String PutJsonAllPlaylist = gson.toJson(playlistAllInformation);
        prefsEditor.putString("Playlists", PutJsonAllPlaylist);
        prefsEditor.putString(str, PutJsonPlaylistInformation);
        prefsEditor.commit();

//        System.out.println("Playlistsssss  "+PutJsonAllPlaylist);
//        System.out.println("Playlist Information  "+ PutJsonPlaylistInformation);

        final RealmQuery<RealmPlaylistInformation> query = realm.where(RealmPlaylistInformation.class);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {

                RealmPlaylistInformation rmif = r.createObject(RealmPlaylistInformation.class, query.findAll().size());
                rmif.setPlaylistName(str);
//                            rmif.setSongs(obj.getSongs());
//                System.out.println("Realm Build Playlist Information  ");
            }
        });

    }


}