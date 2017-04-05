//package kmitl.ce.smart_music_player.ui;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import io.realm.Realm;
//import kmitl.ce.smart_music_player.R;
//import kmitl.ce.smart_music_player.utility.StringEditorUtil;
//
///**
// * Created by Jo on 8/16/2016.
// */
//public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MusicViewHolder> {
//    private Context mContext;
//    private Realm realm;
//
//    public SearchListAdapter(Context context, Realm realm) {
//        this.mContext = context;
//        this.realm = realm;
//    }
//
//    @Override
//    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_musics, null);
//
//        MusicViewHolder vh = new MusicViewHolder(view);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(final MusicViewHolder holder, int position) {
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        ((Activity) mContext).getWindowManager()
//                .getDefaultDisplay()
//                .getMetrics(displaymetrics);
//        int screenWidth = displaymetrics.widthPixels;
//        holder.textView.setWidth((screenWidth) * 80 / 100);
//        holder.imageView.setMaxWidth((screenWidth) * 20 / 100);
//        //holder.likeButton.setWidth(screenWidth * 20 / 100);
//
//        RealmMusicInformation realmMusic = realm.where(RealmMusicInformation.class)
//                .findAll()
//                .get(position);
//
//        holder.textView.setText(StringEditorUtil.subStringMusicTitle(realmMusic.getTitle(), 2));
////        holder.textView.setText(StringEditorUtil.subStringMusicTitle(musicInformationList.get(position).getTitle(), 2));
//        //holder.likeButton.setText("Like");
//
////        int rIndex = musicInformationList.get(position).getRealmIndex();
////        RealmResults<RealmMusicInformation> result = realm.where(RealmMusicInformation.class)
////                .equalTo("id", rIndex)
////                .findAll();
////        RealmMusicInformation realmMusic = result.get(0);
////        if (realmMusic.getLike() == null) {
////            holder.likeButton.setBackgroundColor(Color.parseColor("#2E2EFE"));
////        } else if (realmMusic.getLike()) {
////            holder.likeButton.setBackgroundColor(Color.parseColor("#A9D0F5"));
////        } else {
////            holder.likeButton.setBackgroundColor(Color.parseColor("#F78181"));
////        }
//
////        byte[] thumbnail = musicInformationList.get(position).getThumbnail();
//        byte[] thumbnail = realmMusic.getThumnail();
//        if (thumbnail != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
////            holder.imageView.setImageBitmap(bitmap);
//            int sizePx = convertDpToPixel(50);
//            holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, false));
//        } else {
//            Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
//        }
////        Picasso.with(mContext).load(bitmap)
////                .error(R.drawable.musical_note)
////                .placeholder(R.drawable.musical_note)
////                .into(holder.imageView);
////        Picasso.with(mContext).load(R.drawable.musical_note).into(holder.imageView);
////        holder.imageView.setImageDrawable(null);
//
//        holder.textView.setTag(holder);
//        holder.imageView.setTag(holder);
//        //holder.likeButton.setTag(holder);
//
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MusicViewHolder musicViewHolder = (MusicViewHolder) v.getTag();
//
//                int position = musicViewHolder.getAdapterPosition();
//
////                Toast.makeText(mContext,musicInformationList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                ((MainActivity) mContext).playSong(position);
//            }
//        };
//
////        View.OnClickListener onLikeBtn = new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                MusicViewHolder musicViewHolder = (MusicViewHolder) v.getTag();
////
////                int position = musicViewHolder.getAdapterPosition();
////
////                int rIndex = musicInformationList.get(position).getRealmIndex();
////                RealmResults<RealmMusicInformation> result = realm.where(RealmMusicInformation.class)
////                        .equalTo("id", rIndex)
////                        .findAll();
////                RealmMusicInformation realmMusic = result.get(0);
////                Boolean oldValue = realmMusic.getLike();
////                realm.beginTransaction();
////                if (oldValue == null) {
////                    realmMusic.setLike(true);
////                } else if (oldValue) {
////                    realmMusic.setLike(false);
////                } else {
////                    realmMusic.setLike(null);
////                }
////                realm.commitTransaction();
////
////                notifyItemChanged(position);
////            }
////        };
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
//        return 1;
//    }
//
//    public static class MusicViewHolder extends RecyclerView.ViewHolder {
//        public ImageView imageView;
//        public TextView textView;
//        //public Button likeButton;
//
//        public MusicViewHolder(View view) {
//            super(view);
//            this.imageView = (ImageView) view.findViewById(R.id.music_image);
//            this.textView = (TextView) view.findViewById(R.id.music_title);
//            //this.likeButton = (Button) view.findViewById(R.id.like_button);
//        }
//    }
//
//    private int convertDpToPixel(int dp) {
//        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
//        int px = (int) (dp * (metrics.densityDpi / 160f));
//        return px;
//    }
//}
