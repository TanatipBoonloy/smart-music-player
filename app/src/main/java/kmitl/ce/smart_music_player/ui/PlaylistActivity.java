package kmitl.ce.smart_music_player.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Toolbar musicListToolbar = (Toolbar) findViewById(R.id.music_list_toolbar);
        setSupportActionBar(musicListToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MusicInformation> musicInformationList = new ArrayList<>();
        for(int i = 0  ; i < 20 ; i++){
            MusicInformation musicInformation = new MusicInformation();
            musicInformation.setTitle("name_" + i);
            musicInformation.setAuthor("author__" + i);
            musicInformation.setLength(4000+i*120);
            musicInformation.setThumbnail(null);
            musicInformationList.add(musicInformation);
        }
        mAdapter = new MusicListAdapter(PlaylistActivity.this,musicInformationList);
        mRecyclerView.setAdapter(mAdapter);

//        Toolbar musicPlayingBar = (Toolbar) findViewById(R.id.music_list_playing);
//        musicPlayingBar.animate().translationY(-musicPlayingBar.getBottom()).setInterpolator(
//                new AccelerateInterpolator()).start();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    public void playSong(final MusicInformation musicInformation){
        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setTextSize(20);
        musicPlayingTitle.setText(musicInformation.getTitle());

        Toolbar musicPlayingBar = (Toolbar) findViewById(R.id.music_list_playing);
        setSupportActionBar(musicPlayingBar);

        musicPlayingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayingFragment musicPlayingFragment = MusicPlayingFragment.newInstance(musicInformation);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.playlist_fragment_container,musicPlayingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
