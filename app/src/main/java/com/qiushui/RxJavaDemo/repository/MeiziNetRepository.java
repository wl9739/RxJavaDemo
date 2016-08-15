package com.qiushui.RxJavaDemo.repository;

import com.qiushui.RxJavaDemo.model.MeiziModel;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 网络下载
 *
 * @author Qiushui
 */
public class MeiziNetRepository {

    private static final String BASE_URL = "http://gank.io/";
    private static GankApi gankApi;

    public MeiziNetRepository() {
        if (gankApi == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            gankApi = retrofit.create(GankApi.class);
        }
    }

    /**
     * 调用网络请求,下载妹子图
     * @return
     */
    public Observable<MeiziModel> getMeiziFromNet(String page) {
        return gankApi.getMeiziFromNet(page);
    }

    private interface GankApi {

        @GET("api/search/query/listview/category/福利/count/2/page/{page}")
        Observable<MeiziModel> getMeiziFromNet(@Path("page") String page);
    }
}
