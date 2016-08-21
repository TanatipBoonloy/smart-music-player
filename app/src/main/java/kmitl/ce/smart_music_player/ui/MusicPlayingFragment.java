package kmitl.ce.smart_music_player.ui;

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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicPlayingFragment extends DialogFragment{
    public static final String KEY_MESSAGE = "music_inform";

    private MusicInformation musicInformation;

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
        View rootView = inflater.inflate(R.layout.music_playing_fragment,container,false);

        TextView songNameView = (TextView)rootView.findViewById(R.id.song_name);
        songNameView.setText(musicInformation.getTitle());

        TextView artistNameView = (TextView) rootView.findViewById(R.id.song_artist);
        artistNameView.setText(musicInformation.getArtist());

        ImageButton backBtn = (ImageButton)rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

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
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((PlaylistActivity)getActivity()).updatePlayButton(((PlaylistActivity)getActivity()).getMusicPlayingButton());
    }
}
