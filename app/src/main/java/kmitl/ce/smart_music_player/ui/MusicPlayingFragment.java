package kmitl.ce.smart_music_player.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.entity.RealmPlaylistInformation;
import kmitl.ce.smart_music_player.model.MusicInformation;
import kmitl.ce.smart_music_player.model.PlaylistAllInformation;
import kmitl.ce.smart_music_player.model.PlaylistInformation;
import kmitl.ce.smart_music_player.service.Utility;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicPlayingFragment extends DialogFragment {
    public static final String KEY_MESSAGE = "music_inform";

//    private MusicInformation musicInformation;
    private SeekBar seekBarprocess;
    private TextView songCurrentDuration;
    private TextView songTotalDuration;
    private android.os.Handler handler = new android.os.Handler();
    private MediaPlayer mediaPlayer;
    private TextView songNameView;
    private TextView artistNameView;
    private ImageView imageView;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;

    private Integer stateRepeat;
    private Integer stateShuffle;

    public static MusicPlayingFragment newInstance() {
        MusicPlayingFragment fragment = new MusicPlayingFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme );
        return fragment;
    }

    public MusicPlayingFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);

//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.music_playing_fragment, container, false);



//        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        Dialog dialdow().setLayout(Viog = new Dialog(rootView.getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(rootView);
//        dialog.getWinewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        this.mediaPlayer = ((PlaylistActivity) getActivity()).getMediaPlayer();
        this.songNameView = (TextView) rootView.findViewById(R.id.song_name);
        this.artistNameView = (TextView) rootView.findViewById(R.id.song_artist);
        this.seekBarprocess = (SeekBar) rootView.findViewById(R.id.seekBar);
        this.songCurrentDuration = (TextView) rootView.findViewById(R.id.currentDurationLabel);
        this.songTotalDuration = (TextView) rootView.findViewById(R.id.totalDurationLabel);
        this.imageView = (ImageView) rootView.findViewById(R.id.imageView);
        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.next);
        ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.previous);
        this.repeatButton = (ImageButton) rootView.findViewById(R.id.repeat);
        this.shuffleButton = (ImageButton) rootView.findViewById(R.id.shuffle);
        setRepeatButton();
        setShuffleButton();

        ImageButton backBtn = (ImageButton) rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        final ImageButton playButton = (ImageButton) rootView.findViewById(R.id.play);
        Picasso.with(getActivity().getApplicationContext())
                .load(((PlaylistActivity) getActivity()).getPlayStateImage())
                .into(playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PlaylistActivity) getActivity()).playStateClick(playButton);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //next
                ((PlaylistActivity) getActivity()).nextSong();
                setUpMusicPlayerView();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //previous
                ((PlaylistActivity) getActivity()).previousSong();
                setUpMusicPlayerView();
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                    int mTotalDuration = mediaPlayer.getDuration();
                    seekBarprocess.setProgress(mCurrentPosition / 1000);
                    songCurrentDuration.setText(getTimeString(mCurrentPosition));
                    songTotalDuration.setText(getTimeString(mTotalDuration));

                    if (getActivity() != null) {
                        setUpMusicPlayerView();
                    }

                }
                handler.postDelayed(this, 1000);
            }
        });

        seekBarprocess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000);
                    songCurrentDuration.setText(getTimeString(i * 1000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //repeat
                ((PlaylistActivity) getActivity()).setIsRepeat(!((PlaylistActivity) getActivity()).getIsRepeat());
                setRepeatButton();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shuffle
                ((PlaylistActivity) getActivity()).setIsShuffle(!((PlaylistActivity) getActivity()).getIsShuffle());
                setShuffleButton();
            }
        });

        setUpMusicPlayerView();

        //////////////////////////////Test///////////////////////////////

        final SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        final Gson gson = new Gson();

        String GetJson = appSharedPrefs.getString("Playlists", "");
        PlaylistAllInformation playlistAllInformation = gson.fromJson(GetJson, PlaylistAllInformation.class);

        final String[] Playlists;
        if(playlistAllInformation.getPlaylists()!=null){
             Playlists =playlistAllInformation.getPlaylists() ;
        }else Playlists= new String[]{""};


        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        final String[] mSelected = new String[]{Playlists[0]};


        if(Playlists!=null) {
            builder.setTitle("Add To Playlist");
            builder.setSingleChoiceItems(Playlists, 0, new DialogInterface.OnClickListener() {
                //            String mSelected="";
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSelected[0] = Playlists[which];
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.

                    String GetJson = appSharedPrefs.getString(mSelected[0], "");
                    PlaylistInformation playlistInformation = gson.fromJson(GetJson, PlaylistInformation.class);
                    RealmMusicInformation realmMusicInformation = ((PlaylistActivity) getActivity()).getRealmMusicInformation();

                    if (playlistInformation!=null && playlistInformation.getSongs()!=null){
                        String[] oldArray = playlistInformation.getSongs();
                        String[] array=new String[oldArray.length+1];
                        for (int i=0;i<oldArray.length;i++){
                            array[i]=oldArray[i];
                        }
                        array[array.length-1]= String.valueOf(realmMusicInformation.getId());
                        playlistInformation.setSongs(array);
                    }else {

                        playlistInformation=new PlaylistInformation();
                        String[] array = new String[1];
                        array[0]=String.valueOf(realmMusicInformation.getId());
                        playlistInformation.setSongs(array);

                    }

                    playlistInformation.setPlaylistName(mSelected[0]);
                    String PutJsonPlaylist = gson.toJson(playlistInformation);
                    prefsEditor.putString(playlistInformation.getPlaylistName(), PutJsonPlaylist);
                    prefsEditor.commit();

                    Toast.makeText(getApplicationContext(), PutJsonPlaylist , Toast.LENGTH_SHORT).show();

//                    String test = appSharedPrefs.getString(playlistInformation.getPlaylistName(), "");
//                    PlaylistInformation test2 = gson.fromJson(GetJson, PlaylistInformation.class);
////
//                    System.out.println("showwwwwwwwwwwwIIIIIII"+test);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", null);
        }else{
            builder.setTitle("Don't have playlist to add");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("Cancel", null);
        }
        builder.create();

        ImageView etc = (ImageView) rootView.findViewById(R.id.addToPlaylist);
        etc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                builder.show();
            }
        });


        return rootView;
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            Window window = dialog.getWindow();
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        }
//    }

    private void setImageView() {
        //ImageView
        byte[] thumbnail = ((PlaylistActivity) getActivity()).getRealmMusicInformation().getThumnail();
        if (thumbnail != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
            imageView.setImageBitmap(bitmap);
        } else {
            Picasso.with(getActivity()).load(R.drawable.musical_note).into(imageView);
        }
    }

    private void setRepeatButton() {
        if (((PlaylistActivity) getActivity()).getIsRepeat()) {
            stateRepeat = R.drawable.repeat_button_press;
        } else {
            stateRepeat = R.drawable.repeat_button;
        }
        Picasso.with(getActivity()).load(stateRepeat).into(repeatButton);
    }

    private void setShuffleButton() {
        if (((PlaylistActivity) getActivity()).getIsShuffle()) {
            stateShuffle = R.drawable.shuffle_arrows_press;
        } else {
            stateShuffle = R.drawable.shuffle_arrows;
        }
        Picasso.with(getActivity()).load(stateShuffle).into(shuffleButton);
    }



    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((PlaylistActivity) getActivity()).updatePlayButton(((PlaylistActivity) getActivity()).getMusicPlayingButton());
        ((PlaylistActivity) getActivity()).updateSongName();
    }

    private void setUpMusicPlayerView() {
        RealmMusicInformation realmMusicInformation = ((PlaylistActivity) getActivity()).getRealmMusicInformation();
        songNameView.setText(Utility.subStringTitle(realmMusicInformation.getTitle(), 1));
        artistNameView.setText(realmMusicInformation.getArtist());
        setImageView();
        seekBarprocess.setMax(realmMusicInformation.getDuration() / 1000);
        songTotalDuration.setText(getTimeString(realmMusicInformation.getDuration()));
    }

    private String getTimeString(Integer millisec) {
        int min = (millisec / 1000) / 60;
        int sec = (millisec / 1000) % 60;
        return String.format("%02d:%02d", min, sec);
    }

//    public RealmPlaylistInformation getRealmPlaylistInformation() {
//        return (this.currentSongPlaying != null) ? realm.where(RealmMusicInformation.class)
//                .findAll()
//                .get(this.currentSongPlaying) : null;
//    }


}
