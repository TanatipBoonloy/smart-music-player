package kmitl.ce.smart_music_player.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import kmitl.ce.smart_music_player.service.ReadFileService;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MusicInformation> musicInformationList;
    private ReadFileService readFileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        musicInformationList = new ArrayList<>();
        readFileService = new ReadFileService();
        getMusicList();
//        List<MusicInformation> musicInformationList = new ArrayList<>();
//        for(int i = 0  ; i < 20 ; i++){
//            MusicInformation musicInformation = new MusicInformation();
//            musicInformation.setTitle("name_" + i);
//            musicInformation.setAuthor("author__" + i);
//            musicInformation.setLength(4000+i*120);
//            musicInformation.setThumbnail(null);
//            musicInformationList.add(musicInformation);
//        }


        Toolbar musicListToolbar = (Toolbar) findViewById(R.id.music_list_toolbar);
        setSupportActionBar(musicListToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    musicInformationList = readFileService.getAllMusicFile();

                } else {
                    musicInformationList = new ArrayList<>();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getMusicList(){
        if(Build.VERSION.SDK_INT >= 23) {
            if (PlaylistActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else{
                musicInformationList = readFileService.getAllMusicFile();
            }
        }
        else{
            musicInformationList = readFileService.getAllMusicFile();
        }
    }
}
