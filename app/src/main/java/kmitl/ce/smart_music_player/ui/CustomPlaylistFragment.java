package kmitl.ce.smart_music_player.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.entity.RealmMusicInformation;
import kmitl.ce.smart_music_player.service.Utility;

/**
 * Created by Dell on 23/3/2560.
 */

public class CustomPlaylistFragment extends DialogFragment {

    private Button suffleButton;
    private ImageView imageView;
    private TextView playlistName;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Realm realm;


    public static CustomPlaylistFragment newInstance() {
        CustomPlaylistFragment fragment = new CustomPlaylistFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme );
        return fragment;
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
        this.realm = Realm.getDefaultInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.manage_playlist, container, false);


        ImageButton backBtn = (ImageButton) rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onDestroy();
            }
        });


        this.imageView= (ImageView) rootView.findViewById(R.id.imageView);
        this.playlistName=(TextView) rootView.findViewById(R.id.playlist_name);

        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemViewCacheSize(100);
        this.mRecyclerView.setDrawingCacheEnabled(true);
        this.mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        this.mAdapter = new PlaylistMusicCustomListAdapter(this.getActivity() , this.realm);

        this.mRecyclerView.setAdapter(this.mAdapter);

        this.playlistName.setText("Favorite");

        setUpPlaylistCustomView();

        return rootView;
    }


    private void setImageView() {
        //ImageView
//        byte[] thumbnail = ((PlaylistActivity) getActivity()).getRealmMusicInformation().getThumnail();
//        if (thumbnail != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
//            imageView.setImageBitmap(bitmap);
//        } else {
            Picasso.with(getActivity()).load(R.drawable.view_image).into(imageView);
//        }
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
    }

    private void setUpPlaylistCustomView() {

        setImageView();
    }
}
