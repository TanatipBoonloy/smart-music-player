package kmitl.ce.smart_music_player.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.models.Music;
import kmitl.ce.smart_music_player.ui.activities.MainActivity;
import kmitl.ce.smart_music_player.utils.StringEditorUtil;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicsAdapter extends RecyclerView.Adapter<MusicsAdapter.MusicViewHolder> {
    private Context mContext;
    private List<Music> musicList;

    public MusicsAdapter(Context context, List<Music> musicList) {
        this.mContext = context;
        this.musicList = musicList;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_musics, null);

        MusicViewHolder vh = new MusicViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MusicViewHolder holder, int position) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        holder.textView.setWidth((screenWidth) * 80 / 100);
        holder.imageView.setMaxWidth((screenWidth) * 20 / 100);

        Music music = musicList.get(position);

        holder.textView.setText(StringEditorUtil.subStringMusicTitle(music.getName(), 2));
        holder.artistName.setText(StringEditorUtil.subStringMusicTitle(music.getArtist(), 2));

//        byte[] thumbnail = realmMusic.getThumnail();
//        if (thumbnail != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
////            holder.imageView.setImageBitmap(bitmap);
//            int sizePx = convertDpToPixel(50);
//            holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, false));
//        } else {
//            Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
//        }

        Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);

//        Picasso.with(mContext).load(bitmap)
//                .error(R.drawable.musical_note)
//                .placeholder(R.drawable.musical_note)
//                .into(holder.imageView);
//        Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
//        holder.imageView.setImageDrawable(null);

        holder.textView.setTag(holder);
        holder.imageView.setTag(holder);
        holder.artistName.setTag(holder);
        holder.all.setTag(holder);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicViewHolder musicViewHolder = (MusicViewHolder) v.getTag();
                int position = musicViewHolder.getAdapterPosition();
                ((MainActivity) mContext).playSong(position);
            }
        };

        holder.textView.setOnClickListener(onClickListener);
        holder.imageView.setOnClickListener(onClickListener);
        holder.artistName.setOnClickListener(onClickListener);
        holder.all.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView artistName;
        public RelativeLayout all;

        public MusicViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.music_image);
            this.textView = (TextView) view.findViewById(R.id.music_title);
            this.artistName = (TextView ) view.findViewById(R.id.music_artist);
            this.all = (RelativeLayout) view.findViewById(R.id.all);
        }
    }

//    private int convertDpToPixel(int dp) {
//        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
//        int px = (int) (dp * (metrics.densityDpi / 160f));
//        return px;
//    }
}
