package com.qiushui.RxJavaDemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.qiushui.RxJavaDemo.model.MeiziModel;

import rx.schedulers.Timestamped;

/**
 * @author Qiushui
 */
public class MainActivity extends AppCompatActivity implements IView, ITimestampedView {

    private RecyclerView mRecyclerview;
    private SwipeRefreshLayout mSwiperefresh;

    private MeiziPresenter mPresenter;
    private MeiziAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MeiziPresenter(this, this);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mSwiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }
        });

        // 设置recyclerview
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerview.setAdapter(mAdapter = new MeiziAdapter(this,
                new Timestamped<>(getViewDataTimeMillis(), new MeiziModel())));

        mPresenter.loadData();
    }

    @Override
    public void isRefreshing(final boolean isRefreshing) {
        mSwiperefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwiperefresh.setRefreshing(isRefreshing);
            }
        });
    }

    @Override
    public void refreshDataSuccess(Timestamped<MeiziModel> listTimestamped) {
        // 更新数据
        mRecyclerview.swapAdapter(mAdapter = new MeiziAdapter(this, listTimestamped), false);
    }

    @Override
    public void refreshDataError(Throwable throwable) {
        Toast.makeText(this, "OnError = " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public long getViewDataTimeMillis() {
        return mAdapter == null ? 0 : mAdapter.getTimestampMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }
}
