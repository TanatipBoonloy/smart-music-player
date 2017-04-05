//package kmitl.ce.smart_music_player.ui;
//
///**
// * Created by Dell on 23/3/2560.
// */
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.preference.PreferenceManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.squareup.picasso.Picasso;
//
//import io.realm.Realm;
//import kmitl.ce.smart_music_player.R;
//import kmitl.ce.smart_music_player.entity.RealmPlaylistInformation;
//import kmitl.ce.smart_music_player.utility.NameDisplayUtility;
//
//
//public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder> {
//    private Context mContext;
//    private Realm realm;
//    private PlaylistAllInformation obj;
//    private OnItemClickListener listener;
//
//
//
//    public interface OnItemClickListener {
//        void onItemClicked(View v);
//    }
//
//    public PlaylistListAdapter(OnItemClickListener listener,Context context,Realm realm){
//        super();
//        this.mContext = context;
//        this.realm = realm;
//        this.listener=listener;
//        SharedPreferences appSharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(mContext);
//        Gson gson = new Gson();
//        String json = appSharedPrefs.getString("Playlists", "");
//        this.obj = gson.fromJson(json, PlaylistAllInformation.class);
//    }
//
//
//    @Override
//    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_card, null);
//
//        PlaylistViewHolder vh = new PlaylistViewHolder(view);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        ((Activity) mContext).getWindowManager()
//                .getDefaultDisplay()
//                .getMetrics(displaymetrics);
//        int screenWidth = displaymetrics.widthPixels;
//        holder.textView.setWidth((screenWidth) * 80 / 100);
//        holder.imageView.setMaxWidth((screenWidth) * 20 / 100);
//        //holder.likeButton.setWidth(screenWidth * 20 / 100);
//
////        RealmMusicInformation realmPlaylist = realm.where(RealmMusicInformation.class)
////                .findAll()
////                .get(position);
//        RealmPlaylistInformation realmPlaylist = realm.where(RealmPlaylistInformation.class) .findAll().get(position);
//
//
//        holder.textView.setText(NameDisplayUtility.subStringTitle(realmPlaylist.getPlaylistName(), 2));
//
//
////        byte[] thumbnail = musicInformationList.get(position).getThumbnail();
//        byte[] thumbnail = realmPlaylist.getThumnail();
//        if (thumbnail != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
////            holder.imageView.setImageBitmap(bitmap);
//            int sizePx = convertDpToPixel(50);
//            holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, false));
//        } else {
//            Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
//        }
//
//        holder.textView.setTag(holder);
//        holder.imageView.setTag(holder);
//        //holder.likeButton.setTag(holder);
//
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                PlaylistViewHolder musicViewHolder = (PlaylistViewHolder) v.getTag();
////
////                int position = musicViewHolder.getAdapterPosition();
////
//////                Toast.makeText(mContext,musicInformationList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
////                ((MainActivity) mContext).playSong(position);
//
//
//
////                PlaylistMenuAdapter.MusicViewHolder musicViewHolder = (PlaylistMenuAdapter.MusicViewHolder) v.getTag();
////
////                int position = musicViewHolder.getAdapterPosition();
////
////                RealmPlaylistInformation obj= realm.where(RealmPlaylistInformation.class)
////                        .findAll()
////                        .get(position);
//
////                PlaylistMenuFragment df= new PlaylistMenuFragment().newInstance(obj);
////                df.show(getFragment(), "musicPlayingDialog");
//                listener.onItemClicked(v);
//            }
//        };
//
//
//        holder.textView.setOnClickListener(onClickListener);
//        holder.imageView.setOnClickListener(onClickListener);
//        //holder.likeButton.setOnClickListener(onLikeBtn);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//
////        return obj.getPlaylists().length;
//        return realm.where(RealmPlaylistInformation.class).findAll().size();
//    }
//
//    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
//        public ImageView imageView;
//        public TextView textView;
//        //public Button likeButton;
//
//        public PlaylistViewHolder(View view) {
//            super(view);
//            this.imageView = (ImageView) view.findViewById(R.id.music_image);
//            this.textView = (TextView) view.findViewById(R.id.music_title);
//
//
//            //this.likeButton = (Button) view.findViewById(R.id.like_button);
//
//        }
//    }
//
//    private int convertDpToPixel(int dp) {
//        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
//        int px = (int) (dp * (metrics.densityDpi / 160f));
//        return px;
//    }
//}
//
