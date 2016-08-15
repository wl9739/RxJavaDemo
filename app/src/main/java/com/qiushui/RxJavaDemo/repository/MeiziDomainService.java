package com.qiushui.RxJavaDemo.repository;

import com.qiushui.RxJavaDemo.ITimestampedView;
import com.qiushui.RxJavaDemo.model.MeiziModel;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

/**
 * 数据处理
 * @author Qiushui
 */
public class MeiziDomainService {

    private MeiziNetRepository mNetRepository;
    private MeiziDiskRepository mDiskRepository;


    public MeiziDomainService() {
        mNetRepository = new MeiziNetRepository();
        mDiskRepository = new MeiziDiskRepository();
    }

    /**
     * 获取图片的主要流程
     *
     * @param timestampedView
     * @return
     */
    public Observable<Timestamped<MeiziModel>> getMdeizi(ITimestampedView timestampedView, String page) {
        return getMergedMeizi(page)
                .onErrorReturn(new Func1<Throwable, Timestamped<MeiziModel>>() {
                    @Override
                    public Timestamped<MeiziModel> call(Throwable throwable) {
                        // 不处理任何错误信息
                        return null;
                    }
                })
                .filter(getRecentMeiziFilter(timestampedView));
    }

    /**
     * 过滤操作, meiziModelTimestamped.getTimestampMillis()表示从网络下载时的时间戳,
     * timestampedView.getViewDataTimeMillis()表示本地读取文件的时间戳.
     * 如果网络时间戳大于本地文件时间戳, 则说明网络下载的数据较新, 不应该被拦截.
     *
     * @param timestampedView
     * @return
     */
    private Func1<? super Timestamped<MeiziModel>, Boolean> getRecentMeiziFilter(
            final ITimestampedView timestampedView) {
        return new Func1<Timestamped<MeiziModel>, Boolean>() {
            @Override
            public Boolean call(Timestamped<MeiziModel> meiziModelTimestamped) {
                return meiziModelTimestamped != null
                        && meiziModelTimestamped.getValue() != null
                        && meiziModelTimestamped.getValue().getResults() != null
                        && meiziModelTimestamped.getTimestampMillis() > timestampedView.getViewDataTimeMillis();
            }
        };
    }

    /**
     * 从两个数据源同时获取数据.
     *
     * @return
     */
    private Observable<Timestamped<MeiziModel>> getMergedMeizi(String page) {
        return Observable.mergeDelayError(
                mDiskRepository.getMeiziFromDB().subscribeOn(Schedulers.io()),
                mNetRepository.getMeiziFromNet(page).timestamp().doOnNext(new Action1<Timestamped<MeiziModel>>() {
                    @Override
                    public void call(Timestamped<MeiziModel> result) {
                        mDiskRepository.saveMeizi(result);
                    }
                }).subscribeOn(Schedulers.io())
        );
    }
}
