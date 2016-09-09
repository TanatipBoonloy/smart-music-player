package kmitl.ce.smart_music_player.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.MusicInformation;

/**
 * Created by Jo on 8/16/2016.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder>{
    private List<MusicInformation> musicInformationList;
    private Context mContext;

    public MusicListAdapter(Context context,List<MusicInformation> musicInformationList){
        this.musicInformationList = musicInformationList;
        this.mContext = context;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_card,null);

        MusicViewHolder vh = new MusicViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MusicViewHolder holder, int position) {
        holder.textView.setText(musicInformationList.get(position).getTitle());

        byte[] thumbnail = musicInformationList.get(position).getThumbnail();
        if(thumbnail != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
            holder.imageView.setImageBitmap(bitmap);
        }
        else{
            Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
        }
//        Picasso.with(mContext).load(bitmap)
//                .error(R.drawable.musical_note)
//                .placeholder(R.drawable.musical_note)
//                .into(holder.imageView);
//        Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
//        holder.imageView.setImageDrawable(null);

        holder.textView.setTag(holder);
        holder.imageView.setTag(holder);

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MusicViewHolder musicViewHolder = (MusicViewHolder) v.getTag();

                int position = musicViewHolder.getAdapterPosition();

//                Toast.makeText(mContext,musicInformationList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                ((PlaylistActivity)mContext).playSong(position);
            }
        };

        holder.textView.setOnClickListener(onClickListener);
        holder.imageView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return musicInformationList.size();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;

        public MusicViewHolder(View view){
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.music_image);
            this.textView = (TextView) view.findViewById(R.id.music_title);
        }
    }
}
