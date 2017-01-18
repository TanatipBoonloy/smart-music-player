package kmitl.ce.smart_music_player.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.entity.RealmMusicListened;
import kmitl.ce.smart_music_player.model.MusicInformation;
import kmitl.ce.smart_music_player.service.DBInitialService;
import kmitl.ce.smart_music_player.service.PrintResultService;
import kmitl.ce.smart_music_player.service.ReadFileService;
import kmitl.ce.smart_music_player.service.Utility;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MusicInformation> musicInformationList;
    private ReadFileService readFileService;
    private MediaPlayer mediaPlayer;
    private Integer currentPosition;
    //    private int currentSongInt;
    private Integer currentSongPlaying;
    private Integer currentSongIndex;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private List<Integer> shuffleIndexList;
    private List<Integer> normalIndexList;
    private List<Integer> activeIndexList;

    private Integer playStateImage = null;
    ImageButton musicPlayingButton;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        realm = Realm.getDefaultInstance();

        this.musicInformationList = new ArrayList<>();
        this.normalIndexList = new ArrayList<>();
        this.readFileService = new ReadFileService();
        this.mediaPlayer = new MediaPlayer();

        try {
            DBInitialService.initialize(this.realm, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getMusicList();
        for (int i = 0; i < this.musicInformationList.size(); i++) {
            this.normalIndexList.add(i);
        }
        this.activeIndexList = this.normalIndexList;

        final Toolbar musicListToolbar = (Toolbar) findViewById(R.id.music_list_toolbar);
        setSupportActionBar(musicListToolbar);
        this.musicPlayingButton = (ImageButton) findViewById(R.id.music_playing_button);

        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new MusicListAdapter(PlaylistActivity.this, this.musicInformationList, this.realm);
        this.mRecyclerView.setAdapter(this.mAdapter);

        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
//                System.out.println(getIsRepeat());
                Integer toIndex = null;
                if (currentSongIndex + 1 >= activeIndexList.size()) {
                    if (getIsRepeat()) {
                        toIndex = 0;
                    }
                } else {
                    toIndex = currentSongIndex + 1;
                }
//                Integer toIndex = (currentSongIndex + 1 >= activeIndexList.size()) ?
//                        (PlaylistActivity.this.getIsRepeat()) ?
//                                0 : null
//                        : currentSongIndex + 1;
                if (toIndex != null) {
                    playSong(toIndex);
                }
//                if (isShuffle) {
//                    playSong(currentSongInt); //changed playlist
//                } else if (isRepeat) {
//                    playSong(currentSongInt);// repeat all list
//                } else {
//                    playSong(currentSongInt); //no repeat,no shuffle,play next song
//                }
            }
        });

        musicListToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintResultService.printResult(PlaylistActivity.this, PlaylistActivity.this.realm);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void playSong(int position) {
        setPlayTime();
        this.currentSongPlaying = position;
        this.currentSongIndex = (isShuffle) ? this.activeIndexList.indexOf(position) : position;
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

                    musicInformationList = readFileService.getAllMusicFile(realm);

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
                musicInformationList = readFileService.getAllMusicFile(realm);
            }
        } else {
            musicInformationList = readFileService.getAllMusicFile(realm);
        }
    }

    public void play() {
//        if (mediaPlayer.isPlaying()) {
//            int position = mediaPlayer.getCurrentPosition();
//            setPlayTime(position, getMusicInformation().getRealmIndex());
//        }

        MusicInformation musicInformation = getMusicInformation();
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(musicInformation.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayToolBar() {
        playStateImage = R.drawable.pause_button;
        updatePlayButton(musicPlayingButton);

        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setTextSize(20);

        musicPlayingTitle.setText(Utility.subStringTitle(getMusicInformation().getTitle(), 0));

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


    public MusicInformation getMusicInformation() {
//        if( this.currentSongPlaying != null) {
//            return musicInformationList.get(this.currentSongPlaying);
//        } else {
//            return null;
//        }
        return (this.currentSongPlaying != null) ? musicInformationList.get(this.currentSongPlaying) : null;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void nextSong() {
        setPlayTime();
//        System.out.println(this.currentSongPlaying);
        this.currentSongIndex = (this.currentSongIndex + 1 >= this.activeIndexList.size()) ?
                0 : this.currentSongIndex + 1;
        this.currentSongPlaying = this.activeIndexList.get(this.currentSongIndex);
//        System.out.println(this.currentSongPlaying);
//        if (this.currentSongInt >= musicInformationList.size()) {
//            this.currentSongInt = 0;
//        }
        play();
    }

    public void previousSong() {
        setPlayTime();
//        this.currentSongInt = this.currentSongInt - 1;
        this.currentSongIndex = (this.currentSongIndex - 1 < 0) ?
                this.activeIndexList.size() : this.currentSongIndex - 1;
        this.currentSongPlaying = this.activeIndexList.get(this.currentSongIndex);
//        if (this.currentSongInt < 0) {
//            this.currentSongInt = musicInformationList.size() - 1;
//        }
        play();
    }

    public void setIsRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public void setIsShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
        if (this.isShuffle == true) {
            this.shuffleIndexList = new ArrayList<>();
            for (int i = 0; i < this.musicInformationList.size(); i++) {
                this.shuffleIndexList.add(i);
            }
            Collections.shuffle(this.shuffleIndexList);
            this.activeIndexList = this.shuffleIndexList;
        } else {
            this.currentSongIndex = this.currentSongPlaying;
            this.activeIndexList = this.normalIndexList;
        }
    }

    public boolean getIsRepeat() {
        return isRepeat;
    }

    public boolean getIsShuffle() {
        return isShuffle;
    }

    public void updateSongName() {
        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setTextSize(20);
        musicPlayingTitle.setText(Utility.subStringTitle(getMusicInformation().getTitle(), 0));
    }

//    private void setPlayTimeOnComplete() {
//            int playTime = getMusicInformation().getDuration();
//            int rIndex = getMusicInformation().getRealmIndex();
//        setPlayTime(playTime,rIndex);
//    }
//
//    private void setPlayTimeOnPlaying() {
//        if (this.mediaPlayer.isPlaying()) {
//            int playTime = this.mediaPlayer.getCurrentPosition();
//            int rIndex = getMusicInformation().getRealmIndex();
//            setPlayTime(playTime,rIndex);
//        }
//    }

    private void setPlayTime() {
        int playTime;
        int rIndex;
        if (this.mediaPlayer.isPlaying()) {
            playTime = this.mediaPlayer.getCurrentPosition();
            rIndex = getMusicInformation().getRealmIndex();
        } else {
            if (getMusicInformation() != null) {
                playTime = getMusicInformation().getDuration();
                rIndex = getMusicInformation().getRealmIndex();
            } else {
                return;
            }
        }

        RealmResults<RealmMusicInformation> result = realm.where(RealmMusicInformation.class)
                .equalTo("id", rIndex)
                .findAll();
        RealmMusicInformation realmMusic = result.get(0);
        realm.beginTransaction();
        Number id = realm.where(RealmMusicListened.class).max("id");
        int index = (id != null) ? id.intValue() + 1 : 1;
        RealmMusicListened realmMusicListened = realm.createObject(RealmMusicListened.class,
                index);
        realmMusicListened.setRealmMusicInformation(realmMusic);
        realmMusicListened.setPlayTimed(playTime);
        realm.commitTransaction();
    }

}