package kmitl.ce.smart_music_player.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.models.Music;
import kmitl.ce.smart_music_player.ui.activities.MainActivity;


/**
 * Created by Jo on 8/16/2016.
 */
public class MusicPlayerFragment extends DialogFragment {
    // model variable
    Music music;


    // ui variable
    private SeekBar seekBarprocess;
    private TextView songCurrentDuration;
    private TextView songTotalDuration;
    private android.os.Handler handler = new android.os.Handler();
    private MediaPlayer mediaPlayer;
    private ImageView imageView;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;


    public static MusicPlayerFragment newInstance(Music music) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
        fragment.setMusic(music);
        return fragment;
    }

    public MusicPlayerFragment() {
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
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
        final View rootView = inflater.inflate(R.layout.fragment_music_player, container, false);

        this.mediaPlayer = ((MainActivity) getActivity()).getMediaPlayer();

        TextView songNameView = (TextView) rootView.findViewById(R.id.song_name);
        TextView artistNameView = (TextView) rootView.findViewById(R.id.song_artist);
        songNameView.setText(music.getName());
        artistNameView.setText(music.getArtist());

        this.seekBarprocess = (SeekBar) rootView.findViewById(R.id.seekBar);
        this.songCurrentDuration = (TextView) rootView.findViewById(R.id.currentDurationLabel);
        this.songTotalDuration = (TextView) rootView.findViewById(R.id.totalDurationLabel);
        this.imageView = (ImageView) rootView.findViewById(R.id.imageView);
        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.next);
        ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.previous);
        this.repeatButton = (ImageButton) rootView.findViewById(R.id.repeat);
        this.shuffleButton = (ImageButton) rootView.findViewById(R.id.shuffle);
        this.setRepeatButton();
        this.setShuffleButton();

        ImageButton backBtn = (ImageButton) rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        final ImageButton playButton = (ImageButton) rootView.findViewById(R.id.play);
        Picasso.with(getActivity().getApplicationContext())
                .load(((MainActivity) getActivity()).getPlayStateImage())
                .into(playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).playStateClick(playButton);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //next
                ((MainActivity) getActivity()).nextSong();
                setUpMusicPlayerView();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //previous
                ((MainActivity) getActivity()).previousSong();
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
                ((MainActivity) getActivity()).setIsRepeat(!((MainActivity) getActivity()).getIsRepeat());
                setRepeatButton();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shuffle
                ((MainActivity) getActivity()).setIsShuffle(!((MainActivity) getActivity()).getIsShuffle());
                setShuffleButton();
            }
        });

        this.setUpMusicPlayerView();

//        //////////////////////////////Test///////////////////////////////
//
//        final SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        final SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        final Gson gson = new Gson();
//
//        String GetJson = appSharedPrefs.getString("Playlists", "");
//        PlaylistAllInformation playlistAllInformation = gson.fromJson(GetJson, PlaylistAllInformation.class);
//
//        final String[] Playlists;
//        if(playlistAllInformation==null || playlistAllInformation.getPlaylists()==null ){
//            Playlists= null;
////            System.out.println("nullllllllllllll   :"+GetJson);
//        }else  {
//
//            Playlists =playlistAllInformation.getPlaylists() ;
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
//
//
//        if(Playlists!=null) {
//            final String[] mSelected = new String[]{Playlists[0]};
//            builder.setTitle("Add To Playlist");
//            builder.setSingleChoiceItems(Playlists, 0, new DialogInterface.OnClickListener() {
//                //            String mSelected="";
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mSelected[0] = Playlists[which];
//                }
//            });
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
//
//                    String GetJson = appSharedPrefs.getString(mSelected[0], "");
//                    PlaylistInformation playlistInformation = gson.fromJson(GetJson, PlaylistInformation.class);
////                    RealmMusicInformation realmMusicInformation = ((MainActivity) getActivity()).getRealmMusicInformation();
//
//                    if (playlistInformation!=null && playlistInformation.getSongs()!=null){
//                        String[] oldArray = playlistInformation.getSongs();
//                        String[] array=new String[oldArray.length+1];
//                        for (int i=0;i<oldArray.length;i++){
//                            array[i]=oldArray[i];
//                        }
////                        array[array.length-1]= String.valueOf(realmMusicInformation.getId());
//                        playlistInformation.setSongs(array);
//                    }else {
//
//                        playlistInformation=new PlaylistInformation();
//                        String[] array = new String[1];
////                        array[0]=String.valueOf(realmMusicInformation.getId());
//                        playlistInformation.setSongs(array);
//
//                    }
//
//                    playlistInformation.setPlaylistName(mSelected[0]);
//                    String PutJsonPlaylist = gson.toJson(playlistInformation);
//                    prefsEditor.putString(playlistInformation.getPlaylistName(), PutJsonPlaylist);
//                    prefsEditor.commit();
//
//                    Toast.makeText(getApplicationContext(), PutJsonPlaylist , Toast.LENGTH_SHORT).show();
//
//                    dialog.dismiss();
//                }
//            });
//
//            builder.setNegativeButton("Cancel", null);
//        }else{
//            builder.setTitle("Don't have playlist to add");
//            builder.setPositiveButton("OK", null);
//            builder.setNegativeButton("Cancel", null);
//        }
//        builder.create();

//        ImageView etc = (ImageView) rootView.findViewById(R.id.addToPlaylist);
//        etc.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                builder.show();
//            }
//        });

        return rootView;
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
        ((MainActivity) getActivity()).updatePlayButton(((MainActivity) getActivity()).getMusicPlayingButton());
        ((MainActivity) getActivity()).updateSongName();
    }

    private void setUpMusicPlayerView() {
        // when next, prev, end song

        this.setImageView();
        // set name
        // set artist
        // set bar
    }

    private void setImageView() {
        //ImageView
//        byte[] thumbnail = ((MainActivity) getActivity()).getRealmMusicInformation().getThumnail();
//        if (thumbnail != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
//            imageView.setImageBitmap(bitmap);
//        } else {
        Picasso.with(getActivity()).load(R.drawable.musical_note).into(imageView);
//        }
    }

    private void setRepeatButton() {
        Integer stateRepeat;
        if (((MainActivity) getActivity()).getIsRepeat()) {
            stateRepeat = R.drawable.repeat_button_press;
        } else {
            stateRepeat = R.drawable.repeat_button;
        }
        Picasso.with(getActivity()).load(stateRepeat).into(repeatButton);
    }

    private void setShuffleButton() {
        Integer stateShuffle;
        if (((MainActivity) getActivity()).getIsShuffle()) {
            stateShuffle = R.drawable.shuffle_arrows_press;
        } else {
            stateShuffle = R.drawable.shuffle_arrows;
        }
        Picasso.with(getActivity()).load(stateShuffle).into(shuffleButton);
    }

    private String getTimeString(Integer millisec) {
        int min = (millisec / 1000) / 60;
        int sec = (millisec / 1000) % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
