package kmitl.ce.smart_music_player.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicPlayingFragment extends DialogFragment{
    public static final String KEY_MESSAGE = "music_inform";


    private MusicInformation musicInformation;
    private List<MusicInformation> musicInformationList;
    private int currentSongInt;
    private Integer playButtonState=null;
    private boolean isRepeat=false;
    private boolean isShuffle=false;
    private SeekBar seekBarprocess;
    private TextView songCurrentDuration;
    private TextView songTotalDuration;
    private android.os.Handler handler =new android.os.Handler();

    public static MusicPlayingFragment newInstance(int arg,MusicInformation musicInformation){
        MusicPlayingFragment fragment = new MusicPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count",arg);
        fragment.setArguments(bundle);
        fragment.setMusicInformation(musicInformation);
        return fragment;
    }

    public void setMusicInformation(MusicInformation musicInformation){
        this.musicInformation = musicInformation;
    }


    public MusicPlayingFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.music_playing_fragment,container,false);

        Bundle bundle=this.getArguments();
        currentSongInt=(bundle.getInt("count",0))-1;  //first input is 1 but array start 0
        musicInformationList =((PlaylistActivity)getActivity()).getMusicInformationList();



        final TextView songNameView = (TextView)rootView.findViewById(R.id.song_name);
        songNameView.setText(musicInformation.getTitle());

        final TextView artistNameView = (TextView) rootView.findViewById(R.id.song_artist);
        artistNameView.setText(musicInformation.getArtist());

        ImageButton backBtn = (ImageButton)rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        //ImageView Set
        setImageView(musicInformationList,currentSongInt,rootView);

        seekBarprocess=(SeekBar) rootView.findViewById(R.id.seekBar);

        songCurrentDuration = (TextView)rootView.findViewById(R.id.currentDurationLabel);
        songTotalDuration= (TextView)rootView.findViewById(R.id.totalDurationLabel);

        final ImageButton playButton = (ImageButton)rootView.findViewById(R.id.play);
        Picasso.with(getActivity().getApplicationContext())
                .load(((PlaylistActivity)getActivity()).getPlayStateImage())
                .into(playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PlaylistActivity)getActivity()).playStateClick(playButton);
            }
        });

        final ImageButton nextButton = (ImageButton)rootView.findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicInformation newSong=new MusicInformation();
                if (currentSongInt+1  < musicInformationList.size()) {
                    newSong = musicInformationList.get(currentSongInt+1);
                    currentSongInt+=1;
                }else if(currentSongInt+1==musicInformationList.size()){
                    newSong=musicInformationList.get(0);
                    currentSongInt=0;
                }
                songNameView.setText(newSong.getTitle());
                artistNameView.setText(newSong.getArtist());
                ((PlaylistActivity)getActivity()).playSong(newSong);
                playButtonState = R.drawable.pause_button;
                Picasso.with(getActivity()).load(playButtonState).into(playButton);
                setImageView(musicInformationList,currentSongInt,rootView);
            }
        });

        final ImageButton previousButton = (ImageButton)rootView.findViewById(R.id.previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicInformation newSong=new MusicInformation();
                if (currentSongInt-1 >= 0) {
                    newSong = musicInformationList.get(currentSongInt-1);
                    currentSongInt-=1;
                }else if(currentSongInt-1 < 0){
                    newSong=musicInformationList.get(musicInformationList.size()-1);
                    currentSongInt=musicInformationList.size()-1;
                }
                songNameView.setText(newSong.getTitle());
                artistNameView.setText(newSong.getArtist());
                ((PlaylistActivity)getActivity()).playSong(newSong);
                playButtonState = R.drawable.pause_button;
                Picasso.with(getActivity()).load(playButtonState).into(playButton);
                setImageView(musicInformationList,currentSongInt,rootView);
            }
        });

        final ImageButton repeatButton = (ImageButton)rootView.findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRepeat==true)  isRepeat = false;
                else isRepeat=true;
            }
        });

        final ImageButton shuffleButton = (ImageButton)rootView.findViewById(R.id.shuffle);
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShuffle==false) isShuffle=true;
                else isShuffle=false;
            }
        });

        return rootView;
    }

    private Runnable updateTimeTask=new Runnable() {
        @Override
        public void run() {
            int currentPosition=((PlaylistActivity)getActivity()).getCurrentPosition();
            int totalDuration=((PlaylistActivity)getActivity()).getTotalDuration();

            songTotalDuration.setText(totalDuration);
            songCurrentDuration.setText(currentPosition);
        }
    };



    public void setImageView(List<MusicInformation> musicInformationList, int count,View rootView){
        //ImageView
        final ImageView imageView=(ImageView)rootView.findViewById(R.id.imageView);
        byte[] thumbnail = musicInformationList.get(count).getThumbnail();
        if(thumbnail != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
            imageView.setImageBitmap(bitmap);
        }
        else{
            Picasso.with(getActivity()).load(R.drawable.musical_note).into(imageView);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ((PlaylistActivity)getActivity()).updatePlayButton(((PlaylistActivity)getActivity()).getMusicPlayingButton());
    }
}
