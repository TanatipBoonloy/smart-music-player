package kmitl.ce.smart_music_player.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.models.Music;
import kmitl.ce.smart_music_player.models.rest.response.base.BaseResponse;
import kmitl.ce.smart_music_player.network.ApiClient;
import kmitl.ce.smart_music_player.network.ApiInterface;
import kmitl.ce.smart_music_player.ui.fragments.MusicsFragment;
import kmitl.ce.smart_music_player.ui.fragments.MusicPlayerFragment;
import kmitl.ce.smart_music_player.utils.StringEditorUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // model variable
    private MediaPlayer mediaPlayer;
    private Integer currentPosition;
    private Integer currentSongPlaying;
    private Integer currentSongIndex;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private List<Integer> shuffleIndexList;
    private List<Integer> normalIndexList;
    private List<Integer> activeIndexList;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private List<Music> musicList;

    // ui variable
    private ImageButton musicPlayingButton;
    private SegmentedGroup segment;
    //    private PlaylistFragment playlsitFragment;
    private MusicsFragment recycleFragment;
//    private SuggesionFragment recycleSuggesion;
//    private SearchFragment searchFragment;
    private String TYPE_ACTIVITY = "TYPE_ACTIVITY";
    private Integer playStateImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getMusicList();
    }

    private void setUpActivity() {
        this.normalIndexList = new ArrayList<>();
        this.mediaPlayer = new MediaPlayer();

        int count = musicList.size();
        for (int i = 0; i < count; i++) {
            this.normalIndexList.add(i);
        }
        this.activeIndexList = this.normalIndexList;

        this.musicPlayingButton = (ImageButton) findViewById(R.id.music_playing_button);
        musicPlayingButton.setMaxWidth((new DisplayMetrics().widthPixels) * 30 / 100);

//        this.playlsitFragment = new PlaylistFragment();
        this.recycleFragment = MusicsFragment.newInstance(musicList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //set visible suggesion fragment
        if (false) {
//            this.recycleSuggesion = new SuggesionFragment();
//            transaction.replace(R.id.contentFragmentSuggesion, recycleSuggesion);
        } else {
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] list = new String[]{"Search", "Log out"};

        final int[] mSelected = {0};

        builder.setTitle("Select");
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelected[0] = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                dialog.dismiss();
                if (mSelected[0] == 0) {
//                    searchFragment = new SearchFragment();
//                    SearchFragment df = new SearchFragment();
//                    df.show(getSupportFragmentManager(), "musicPlayingDialog");
                } else {
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create();

        ImageView etc = (ImageView) findViewById(R.id.etc);
        etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });


        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Integer toIndex = null;
                if (currentSongIndex + 1 >= activeIndexList.size()) {
                    if (getIsRepeat()) {
                        toIndex = 0;
                    }
                } else {
                    toIndex = currentSongIndex + 1;
                }
                if (toIndex != null) {
                    playSong(toIndex);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void playSong(int position) {
        this.currentSongPlaying = position;
        this.currentSongIndex = (isShuffle) ? this.activeIndexList.indexOf(position) : position;
        play();
        setPlayToolBar();
    }

    public void getMusicList() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissionGetAllSong();
        } else {
            getRandomSong();
        }
    }

    public void play() {
        Music music = getMusic();
        mediaPlayer.reset();
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
            mediaPlayer.setDataSource(music.getStreaming_url());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayToolBar() {
        playStateImage = R.drawable.pause_button;
        updatePlayButton(musicPlayingButton);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int screenWidth = displaymetrics.widthPixels;

        TextView musicPlayingTitle = (TextView) findViewById(R.id.music_playing_title);
        musicPlayingTitle.setWidth((screenWidth) * 70 / 100);

        musicPlayingTitle.setText(StringEditorUtil.subStringMusicTitle(getMusic().getName(), 0));

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
                MusicPlayerFragment musicPlayerFragment = MusicPlayerFragment.newInstance(getMusic());
                musicPlayerFragment.show(ft, "musicPlayingDialog");
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
        Picasso.with(MainActivity.this).load(playStateImage).into(imageButton);
    }

    public int getPlayStateImage() {
        return playStateImage;
    }

    public ImageButton getMusicPlayingButton() {
        return musicPlayingButton;
    }

    public Music getMusic() {
        return (this.currentSongPlaying != null) ? musicList.get(currentSongPlaying) : null;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void nextSong() {
        this.currentSongIndex = (this.currentSongIndex + 1 >= this.activeIndexList.size()) ?
                0 : this.currentSongIndex + 1;
        this.currentSongPlaying = this.activeIndexList.get(this.currentSongIndex);
        play();
    }

    public void previousSong() {
        this.currentSongIndex = (this.currentSongIndex - 1 < 0) ?
                this.activeIndexList.size() : this.currentSongIndex - 1;
        this.currentSongPlaying = this.activeIndexList.get(this.currentSongIndex);
        play();
    }

    public void setIsRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public void setIsShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
        if (this.isShuffle) {
            this.shuffleIndexList = new ArrayList<>();
            int count = this.musicList.size();
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
        musicPlayingTitle.setText(StringEditorUtil.subStringMusicTitle(getMusic().getName(), 0));
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
        this.recycleFragment = MusicsFragment.newInstance(musicList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.remove(this.playlsitFragment);
        transaction.replace(R.id.contentFragment, this.recycleFragment);
        transaction.commit();
    }

    private void playlistButton(SegmentedGroup group) {
//        this.playlsitFragment = new PlaylistFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.remove(this.recycleFragment);
//        transaction.replace(R.id.contentFragment, this.playlsitFragment);
//        transaction.commit();

    }

    private void checkPermissionGetAllSong() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        } else {
            getRandomSong();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getRandomSong();
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

    private void getRandomSong() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse<List<Music>>> call = apiService.getRandomSong(20);
        call.enqueue(new Callback<BaseResponse<List<Music>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Music>>> call, Response<BaseResponse<List<Music>>> response) {
                if (response.isSuccessful()) {
                    musicList = response.body().getData();
                    setUpActivity();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Music>>> call, Throwable t) {

            }
        });
    }
}