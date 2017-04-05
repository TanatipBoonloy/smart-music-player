package kmitl.ce.smart_music_player.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.models.Music;
import kmitl.ce.smart_music_player.ui.adapters.MusicsAdapter;

/**
 * Created by Dell on 22/3/2560.
 */

public class MusicsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Music> musicList;

    public static MusicsFragment newInstance(List<Music> musicList) {
        MusicsFragment fragment = new MusicsFragment();
        fragment.setMusicList(musicList);
        return fragment;
    }

    public MusicsFragment(){
    }

    public void setMusicList(List<Music> Musics) {
        this.musicList = Musics;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_musics, container, false);

        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemViewCacheSize(100);
        this.mRecyclerView.setDrawingCacheEnabled(true);
        this.mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        this.mAdapter = new MusicsAdapter(getActivity(), this.musicList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        return rootView;

    }

}
