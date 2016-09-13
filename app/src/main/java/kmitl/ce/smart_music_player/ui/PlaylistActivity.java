package kmitl.ce.smart_music_player.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;
import kmitl.ce.smart_music_player.service.ReadFileService;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MusicInformation> musicInformationList;
    private ReadFileService readFileService;
    private MediaPlayer mediaPlayer;
    private int currentPosition;
    private int currentSongInt;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private List<MusicInformation> musicInformationListRandom;
    private List<MusicInformation> musicInformationListNormal;

    private Integer playStateImage = null;
    ImageButton musicPlayingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        musicInformationList = new ArrayList<>();
        musicInformationListRandom=new ArrayList<>();
        musicInformationListNormal=new ArrayList<>();
        readFileService = new ReadFileService();
        mediaPlayer = new MediaPlayer();
        getMusicList();
        musicInformationListNormal=musicInformationList;


        final Toolbar musicListToolbar = (Toolbar) findViewById(R.id.music_list_toolbar);
        setSupportActionBar(musicListToolbar);
        musicPlayingButton = (ImageButton) findViewById(R.id.music_playing_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MusicListAdapter(PlaylistActivity.this, musicInformationList);
        mRecyclerView.setAdapter(mAdapter);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                currentSongInt+=1;
                if (currentSongInt==musicInformationList.size()){
                    currentSongInt=0;
                }

                if (isShuffle){
                    playSong(currentSongInt); //changed playlist
                }else if(isRepeat){
                    playSong(currentSongInt);// repeat all list
                }else{
                    playSong(currentSongInt); //no repeat,no shuffle,play next song

                }
            }
        });

        musicInformationListRandom=musicInformationList;
        //setMusicInformationListRandom();
    }

    public void playSong(int position) {
        currentSongInt = position;
        play();
        setPlayToolBar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
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

    public void getMusicList() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PlaylistActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                musicInformationList = readFileService.getAllMusicFile();
            }
        } else {
            musicInformationList = readFileService.getAllMusicFile();
        }
    }

    public void play() {
        MusicInformation musicInformation = musicInformationList.get(currentSongInt);
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(musicInformation.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayToolBar(){
        MusicInformation musicInformation = musicInformationList.get(currentSongInt);
        playStateImage = R.drawable.pause_button;
        updatePlayButton(musicPlayingButton);


        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setTextSize(20);
        musicPlayingTitle.setText(musicInformation.getTitle());

        Toolbar musicPlayingBar = (Toolbar) findViewById(R.id.music_list_playing);
        setSupportActionBar(musicPlayingBar);

        musicPlayingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prevFragment = getSupportFragmentManager().findFragmentByTag("musicPlayingDialog");
                if (prevFragment != null) {
                    ft.remove(prevFragment);
                }

                ft.addToBackStack(null);
                MusicPlayingFragment musicPlayingFragment = MusicPlayingFragment.newInstance();
                musicPlayingFragment.show(ft, "musicPlayingDialog");
            }
        });

        musicPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playStateClick(musicPlayingButton);
            }
        });
    }

    public void playStateClick(ImageButton imageButton) {
        try {
            if (mediaPlayer.isPlaying()) {
                playStateImage = R.drawable.play_button;
                mediaPlayer.pause();
                currentPosition = mediaPlayer.getCurrentPosition();
            } else {
                playStateImage = R.drawable.pause_button;
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.start();
            }
            updatePlayButton(imageButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayButton(ImageButton imageButton) {
        Picasso.with(PlaylistActivity.this).load(playStateImage).into(imageButton);
    }

    public int getPlayStateImage() {
        return playStateImage;
    }

    public ImageButton getMusicPlayingButton() {
        return musicPlayingButton;
    }



    public MusicInformation getMusicInformation(){
        return musicInformationList.get(currentSongInt);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void nextSong(){
        this.currentSongInt = this.currentSongInt+1;
        if(this.currentSongInt >= musicInformationList.size()){
            this.currentSongInt = 0;
        }
        play();
    }

    public void previousSong(){
        this.currentSongInt = this.currentSongInt-1;

        if(this.currentSongInt < 0){
            this.currentSongInt = musicInformationList.size()-1;
        }
        play();
    }

    public void setMusicInformationListRandom(){
        //Sample Random
        /*musicInformationListRandom.add(musicInformationList.get(2));
        musicInformationListRandom.add(musicInformationList.get(1));
        musicInformationListRandom.add(musicInformationList.get(0));
        musicInformationListRandom.add(musicInformationList.get(3));
        musicInformationListRandom.add(musicInformationList.get(4));
        musicInformationListRandom.add(musicInformationList.get(6));
        musicInformationListRandom.add(musicInformationList.get(5));*/


    }

    public void setIsRepeat(boolean change){
        this.isRepeat=change;
    }

    public void setIsShuffle(boolean change){
        this.isShuffle=change;
        if(isShuffle==true){
            musicInformationList=musicInformationListRandom;
        }else{
            musicInformationList=musicInformationListNormal;
        }
    }

    public boolean getIsRepeat(){return isRepeat;}

    public boolean getIsShuffle(){return isShuffle;}

    public void updateSongName(){
        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setTextSize(20);
        musicPlayingTitle.setText(getMusicInformation().getTitle());
    }
}