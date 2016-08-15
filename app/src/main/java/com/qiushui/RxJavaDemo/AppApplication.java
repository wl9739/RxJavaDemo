package com.qiushui.RxJavaDemo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Qiushui
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 配置Realm
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("meiziDB.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
