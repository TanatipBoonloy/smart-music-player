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

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicPlayingFragment extends DialogFragment{
    public static final String KEY_MESSAGE = "music_inform";

    private String musicTitle;

    public static MusicPlayingFragment newInstance(MusicInformation musicInformation){
        MusicPlayingFragment fragment = new MusicPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MESSAGE,musicInformation.getAuthor());
        fragment.setArguments(bundle);
        return fragment;
    }

    public MusicPlayingFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle!=null){
            musicTitle = bundle.getString(KEY_MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.music_playing_fragment,container,false);

        TextView textView = (TextView)rootView.findViewById(R.id.song_name);
        textView.setText(musicTitle);

        ImageButton backBtn = (ImageButton)rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        return rootView;
    }
}
