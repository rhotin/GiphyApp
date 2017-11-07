package com.appdeveloper.rh.giphyapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Roman on 11/5/2017.
 */

public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public TextView title;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rowTitle);
            photo = (ImageView) itemView.findViewById(R.id.rowImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    private ArrayList<GifObject> mGifObjects;
    private Context mContext;

    public TrendingListAdapter(Context context, ArrayList<GifObject> gifObjects) {
        mGifObjects = gifObjects;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View trendingView = inflater.inflate(R.layout.trending_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(trendingView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GifObject gifObject = mGifObjects.get(position);

        TextView textView = holder.title;
        textView.setText(gifObject.title);
        ImageView imageView = holder.photo;
        Glide.with(getContext()).load(gifObject.imageUrl).into(imageView);
       // imageView.setImageBitmap(gifObject.gifImage);

    }

    @Override
    public int getItemCount() {
        return mGifObjects.size();
    }


}
