package kmitl.ce.smart_music_player.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;
import kmitl.ce.smart_music_player.service.Utility;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicPlayingFragment extends DialogFragment {
    public static final String KEY_MESSAGE = "music_inform";

    private MusicInformation musicInformation;
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

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        Dialog dialog = new Dialog(rootView.getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(rootView);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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
        byte[] thumbnail = ((PlaylistActivity) getActivity()).getMusicInformation().getThumbnail();
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
    public void onDestroy() {
        super.onDestroy();
        ((PlaylistActivity) getActivity()).updatePlayButton(((PlaylistActivity) getActivity()).getMusicPlayingButton());
        ((PlaylistActivity) getActivity()).updateSongName();
    }

    private void setUpMusicPlayerView() {
        musicInformation = ((PlaylistActivity) getActivity()).getMusicInformation();
        songNameView.setText(Utility.subStringTitle(musicInformation.getTitle(), 1));
        artistNameView.setText(musicInformation.getArtist());
        setImageView();
        seekBarprocess.setMax(musicInformation.getDuration() / 1000);
        songTotalDuration.setText(getTimeString(musicInformation.getDuration()));
    }

    private String getTimeString(Integer millisec) {
        int min = (millisec / 1000) / 60;
        int sec = (millisec / 1000) % 60;
        return String.format("%02d:%02d", min, sec);
    }


}
