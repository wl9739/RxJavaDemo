package com.qiushui.RxJavaDemo;

import com.qiushui.RxJavaDemo.model.MeiziModel;

import rx.schedulers.Timestamped;

/**
 * @author Qiushui
 */
public interface IView {

    void isRefreshing(boolean isRefreshing);

    void refreshDataSuccess(Timestamped<MeiziModel> listTimestamped);

    void refreshDataError(Throwable throwable);
}
