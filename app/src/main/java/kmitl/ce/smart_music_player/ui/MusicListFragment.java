package kmitl.ce.smart_music_player.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.response.MusicResponse;

/**
 * Created by Dell on 22/3/2560.
 */

public class MusicListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<MusicResponse> musicList;

    public static MusicListFragment newInstance(List<MusicResponse> musicList) {
        MusicListFragment fragment = new MusicListFragment();
        fragment.setMusicList(musicList);
        return fragment;
    }

    public MusicListFragment(){
    }

    public void setMusicList(List<MusicResponse> musicResponses) {
        this.musicList = musicResponses;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recycleview_music, container, false);

        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemViewCacheSize(100);
        this.mRecyclerView.setDrawingCacheEnabled(true);
        this.mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        this.mAdapter = new MusicListAdapter(getActivity(), this.musicList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        return rootView;

    }

}
