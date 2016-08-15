package com.qiushui.RxJavaDemo;

import com.qiushui.RxJavaDemo.model.MeiziModel;
import com.qiushui.RxJavaDemo.repository.MeiziDomainService;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Timestamped;

/**
 * @author Qiushui
 */
public class MeiziPresenter {

    private IView mView;

    private ITimestampedView mITimestampedView;

    private MeiziDomainService mService;

    private Subscription meiziSubscription;


    public MeiziPresenter(IView meiziView, ITimestampedView timestampedView) {
        mView = meiziView;
        mITimestampedView = timestampedView;

        mService = new MeiziDomainService();
    }

    public void loadData() {
        mView.isRefreshing(true);
        unSubscribe();
        Observable<Timestamped<MeiziModel>> recentMeizi = mService.getMdeizi(mITimestampedView, getRandomPage())
                .observeOn(AndroidSchedulers.mainThread());

        meiziSubscription = recentMeizi.subscribe(fetchMeiziOnNext, fetchMeiziOnError, fetchMeiziOnComplete);
    }

    public void unSubscribe() {
        if (meiziSubscription != null) {
            meiziSubscription.unsubscribe();
            meiziSubscription = null;
        }
    }

    private Action1<? super Timestamped<MeiziModel>> fetchMeiziOnNext = new Action1<Timestamped<MeiziModel>>() {
        @Override
        public void call(Timestamped<MeiziModel> listTimestamped) {
            mView.refreshDataSuccess(listTimestamped);
        }
    };

    private rx.functions.Action1<java.lang.Throwable> fetchMeiziOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            mView.refreshDataError(throwable);
            mView.isRefreshing(false);
        }
    };

    private Action0 fetchMeiziOnComplete = new Action0() {
        @Override
        public void call() {
            mView.isRefreshing(false);
        }
    };

    private String getRandomPage() {
        return "" + (int)((Math.random() * 100) * 1);
    }
}
