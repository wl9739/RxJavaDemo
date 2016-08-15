package com.qiushui.RxJavaDemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiushui.RxJavaDemo.model.MeiziModel;
import com.squareup.picasso.Picasso;

import rx.schedulers.Timestamped;

/**
 * @author Qiushui
 */
public class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziHodlder> {

    private final Timestamped<MeiziModel> data;
    private final Context contex;

    public MeiziAdapter(Context context, Timestamped<MeiziModel> data) {
        this.contex = context;
        this.data = data;
    }

    @Override
    public MeiziHodlder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeiziHodlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meizi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MeiziHodlder holder, int position) {
            Picasso.with(contex).load(data.getValue().getResults().get(position).getUrl()).into(holder.mImageView);
    }


    @Override
    public int getItemCount() {
        if (data == null || data.getValue() == null || data.getValue().getResults() == null) {
            return 0;
        } else {
            return data.getValue().getResults().size();
        }
    }

    public long getTimestampMillis() {
        return data.getTimestampMillis();
    }

    public class MeiziHodlder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public MeiziHodlder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.meizi_item_img);
        }
    }
}
