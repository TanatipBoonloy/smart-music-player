//package kmitl.ce.smart_music_player.ui;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import io.realm.Realm;
//import kmitl.ce.smart_music_player.R;
//
///**
// * Created by Dell on 23/3/2560.
// */
//
//public class SuggesionFragment extends Fragment {
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private Realm realm;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.realm = Realm.getDefaultInstance();
//
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View rootView = inflater.inflate(R.layout.fragment_suggesion_music, container, false);
//
//        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
////        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        this.mRecyclerView.setHasFixedSize(true);
//        this.mRecyclerView.setItemViewCacheSize(100);
//        this.mRecyclerView.setDrawingCacheEnabled(true);
//        this.mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        this.mRecyclerView.setLayoutManager(layoutManager);
//
//        this.mAdapter = new SuggesionAdapter(this.getActivity() , this.realm);
//
//        this.mRecyclerView.setAdapter(this.mAdapter);
//
//        return rootView;
//    }
//
//}
