package com.qiushui.RxJavaDemo.repository;

import com.qiushui.RxJavaDemo.model.MeiziModel;
import com.qiushui.RxJavaDemo.model.ResultsBeen;

import java.util.concurrent.Callable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.schedulers.Timestamped;

/**
 * 本地文件处理类
 *
 * @author Qiushui
 */
public class MeiziDiskRepository {

    /**
     * 保存妹子图到数据库中, 如果本地已保存有妹子图, 则先删除, 再保存
     *
     * @param meiziModelTimestamped
     */
    public void saveMeizi(Timestamped<MeiziModel> meiziModelTimestamped) {

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<MeiziModel> results = realm.where(MeiziModel.class).findAll();
        if (results.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Delete all
                    results.deleteAllFromRealm();
                }
            });
        }

        long timestamp = meiziModelTimestamped.getTimestampMillis();
        realm.beginTransaction();
        MeiziModel meiziModel = realm.copyToRealm(meiziModelTimestamped.getValue());
        meiziModel.setTime(timestamp);
        realm.commitTransaction();
    }

    /**
     * 从数据库中获取妹子图
     *
     * @return
     */
    public Observable<Timestamped<MeiziModel>> getMeiziFromDB() {
        return Observable.fromCallable(new Callable<Timestamped<MeiziModel>>() {
            @Override
            public Timestamped<MeiziModel> call() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<MeiziModel> results = realm.where(MeiziModel.class).findAll();
                results = results.sort("time", Sort.DESCENDING);

                if (results.size() > 0) {
                    MeiziModel meiziModel = new MeiziModel();
                    RealmList<ResultsBeen> resultsBeanList = new RealmList<>();
                    for (int i = 0; i < results.get(0).getResults().size(); i++) {
                        ResultsBeen resultsBeen = new ResultsBeen();
                        resultsBeen.setUrl(results.get(0).getResults().get(i).getUrl());
                        resultsBeanList.add(resultsBeen);
                    }
                    meiziModel.setTime(results.get(0).getTime());
                    meiziModel.setResults(resultsBeanList);

                    return new Timestamped<>(results.get(0).getTime(), meiziModel);
                } else {
                    return null;
                }
            }
        });
    }
}
