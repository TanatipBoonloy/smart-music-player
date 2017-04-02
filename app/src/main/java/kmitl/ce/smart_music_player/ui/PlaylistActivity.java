package kmitl.ce.smart_music_player.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;
import io.realm.RealmResults;
import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.entity.RealmMusicListened;
import kmitl.ce.smart_music_player.service.DBInitialService;
import kmitl.ce.smart_music_player.service.ReadFileService;
import kmitl.ce.smart_music_player.service.Utility;

public class PlaylistActivity extends AppCompatActivity implements  View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //    private List<MusicInformation> musicInformationList;
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
    private ProgressBar spinner;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private Integer playStateImage = null;
    ImageButton musicPlayingButton;

    private SegmentedGroup segment;
    private Realm realm;
    private PlaylistFragment playlsitFragment;
    private RecycleMusicFragment recycleFragment;
    private RecycleSuggesionFragment recycleSuggesion;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);




        realm = Realm.getDefaultInstance();

//        this.musicInformationList = new ArrayList<>();
        this.normalIndexList = new ArrayList<>();
        this.readFileService = new ReadFileService();
        this.mediaPlayer = new MediaPlayer();

//        try {
//            DBInitialService.initialize(this.realm, this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
        getMusicList();
//        insertDummyContactWrapper();







//        for (int i = 0; i < this.musicInformationList.size(); i++) {
//            this.normalIndexList.add(i);
//        }
        int count = (int) this.realm.where(RealmMusicInformation.class).count();
        for (int i = 0; i < count; i++) {
            this.normalIndexList.add(i);
        }
        this.activeIndexList = this.normalIndexList;

        this.musicPlayingButton = (ImageButton) findViewById(R.id.music_playing_button);
        musicPlayingButton.setMaxWidth((new DisplayMetrics().widthPixels) * 30 / 100);




        this.playlsitFragment = new PlaylistFragment();
        this.recycleFragment = new RecycleMusicFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //set visible suggesion fragment
        if(true){
            this.recycleSuggesion = new RecycleSuggesionFragment();
            transaction.replace(R.id.contentFragmentSuggesion, recycleSuggesion);

        }else{
            FrameLayout suggesion = (FrameLayout) findViewById(R.id.contentFragmentSuggesion);
            suggesion.setVisibility(View.INVISIBLE);
        }

        transaction.replace(R.id.contentFragment, recycleFragment);
        transaction.commit();

        segment = (SegmentedGroup) findViewById(R.id.segmented_control);
        segment.setTintColor(Color.parseColor("#a39b9b"), Color.parseColor("#ffffff"));
        RadioButton songBtn = (RadioButton) findViewById(R.id.songs);
        RadioButton playlistBtn = (RadioButton) findViewById(R.id.playlsits);

        //Set listener for button
        songBtn.setOnClickListener(this);
        playlistBtn.setOnClickListener(this);

        segment.check(R.id.songs);


        //Test--------------------------------------
        ImageView search = (ImageView) findViewById(R.id.search_icon);
        this.searchFragment = new SearchFragment();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchFragment df= new SearchFragment();
                df.show(getSupportFragmentManager(), "musicPlayingDialog");
            }
        });




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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void playSong(int position) {
        setPlayTime();
        this.currentSongPlaying = position;
        this.currentSongIndex = (isShuffle) ? this.activeIndexList.indexOf(position) : position;
        play();
        setPlayToolBar();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    readFileService.getAllMusicFile(realm);
//
//                } else {
//
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }

    public void getMusicList() {
        if (Build.VERSION.SDK_INT >= 23) {
//            if (PlaylistActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            } else { readFileService.getAllMusicFile(realm);
//            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
//
//                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE_ASK_PERMISSIONS);
            checkPermissionGetAllSong();

        } else {
//            readFileService.getAllMusicFile(realm);
            readStorage();
        }


    }

    public void play() {
//        if (mediaPlayer.isPlaying()) {
//            int position = mediaPlayer.getCurrentPosition();
//            setPlayTime(position, getMusicInformation().getRealmIndex());
//        }

        RealmMusicInformation realmMusicInformation = getRealmMusicInformation();
        mediaPlayer.reset();

        // if else to test streaming
        if (realmMusicInformation.getId() == 1) {
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.reset();
                    return false;
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            try {
                mediaPlayer.setDataSource("https://s3-ap-southeast-1.amazonaws.com/kmitl-4d-smp.stream/test/01+Sorry+(feat.+Kurt+Hugo+Schneider.m3u8");
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                mediaPlayer.setDataSource(realmMusicInformation.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPlayToolBar() {
        playStateImage = R.drawable.pause_button;
        updatePlayButton(musicPlayingButton);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int screenWidth = displaymetrics.widthPixels;

        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setWidth((screenWidth) * 70 / 100);

        musicPlayingTitle.setText(Utility.subStringTitle(getRealmMusicInformation().getTitle(), 0));

        Toolbar musicPlayingBar = (Toolbar) findViewById(R.id.music_list_playing);
        setSupportActionBar(musicPlayingBar);

//        holder.textView.setWidth((screenWidth) * 80 / 100);
//        holder.imageView.setMaxWidth((screenWidth) * 20 / 100);
        musicPlayingBar.setVisibility(View.VISIBLE);

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


    public RealmMusicInformation getRealmMusicInformation() {
//        if( this.currentSongPlaying != null) {
//            return musicInformationList.get(this.currentSongPlaying);
//        } else {
//            return null;
//        }
        return (this.currentSongPlaying != null) ? realm.where(RealmMusicInformation.class)
                .findAll()
                .get(this.currentSongPlaying) : null;
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
            int count = (int) this.realm.where(RealmMusicInformation.class).count();
            for (int i = 0; i < count; i++) {
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
        musicPlayingTitle.setText(Utility.subStringTitle(getRealmMusicInformation().getTitle(), 0));
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
            rIndex = getRealmMusicInformation().getId();
        } else {
            if (getRealmMusicInformation() != null) {
                playTime = getRealmMusicInformation().getDuration();
                rIndex = getRealmMusicInformation().getId();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.songs:
                songsButton(segment);
                break;
            case R.id.playlsits:
                playlistButton(segment);
                break;
            default:
                // Nothing to do
        }
    }

    private void songsButton(SegmentedGroup group) {

        Toast.makeText(this, "library click", Toast.LENGTH_SHORT).show();
        this.recycleFragment= new RecycleMusicFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(this.playlsitFragment);
        transaction.replace(R.id.contentFragment, this.recycleFragment);
//        transaction.addToBackStack(null);
        transaction.commit();

//        this.playlsitFragment.onDestroy();
    }

    private void playlistButton(SegmentedGroup group) {
        Toast.makeText(this, "playlist  click", Toast.LENGTH_SHORT).show();
        this.playlsitFragment= new PlaylistFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(this.recycleFragment);
        transaction.replace(R.id.contentFragment, this.playlsitFragment);
//        transaction.addToBackStack(null);
        transaction.commit();

//        this.recycleFragment.onDestroy();

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if ( v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent( event );
//    }

    private void checkPermissionGetAllSong() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }else{
            readStorage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    readStorage();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Read external Storage Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void readStorage(){
        System.out.println("hiiiiiiiiii555555okkkk");
        try {
            DBInitialService.initialize(this.realm, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            DBInitialService.initializePlaylist(this.realm,this);
        }catch (Exception e){
            e.printStackTrace();
        }
        readFileService.getAllMusicFile(realm);
    }
}