package kmitl.ce.smart_music_player;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Jo on 10/20/2016.
 */
public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
