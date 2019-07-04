/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.app.movieapp1.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.app.movieapp1.R;
import com.google.app.movieapp1.model.Video;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    // COMPLETED (3) Create a final private ListItemClickListener called mOnClickListener
    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
//    final private ListItemClickListener mOnClickListener;
    private final TrailerAdapterOnClickHandler mOnClickListener;

    // COMPLETED (1) Add an interface called ForecastAdapterOnClickHandler
    // COMPLETED (2) Within that interface, define a void method that access a String as a parameter

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickHandler {

        void onClick(int trailerNo, String tag);
//        void onShareURL(int trailerNo);

    }


    private static int viewHolderCount;
    private Context mContext;
    private List<Video> mVideos;

    public TrailerAdapter(List<Video> mVideos, TrailerAdapterOnClickHandler handler) {
        this.mVideos = mVideos;
//        mContext = context;
        mOnClickListener = handler;
        viewHolderCount = 1;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        ++viewHolderCount;

//        viewHolder.tv_trailer.setText("ViewHolder index: " + viewHolderCount);

//        int backgroundColorForViewHolder = ColorUtils
//                .getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
//        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);


        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    // COMPLETED (5) Implement OnClickListener in the NumberViewHolder class

    /**
     * Cache of the children views for a list item.
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView tv_trailer;
        ImageView iv_play;
        ImageView mImageView;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            tv_trailer = itemView.findViewById(R.id.bt_trailer);
            iv_play = itemView.findViewById(R.id.iv_play);
            mImageView = itemView.findViewById(R.id.share_iv);

            String toolTip = "play trailer " + mVideos.get(viewHolderCount - 1).getName();
            tv_trailer.setTooltipText(toolTip);
            tv_trailer.setContentDescription(toolTip);

            tv_trailer.setTag("0");
            iv_play.setTag("0");

            if(viewHolderCount == 1) {

                mImageView.setTag("1");
                mImageView.setOnClickListener(this);
            }
            else{
                mImageView.setVisibility(View.INVISIBLE);
            }
            tv_trailer.setOnClickListener(this);
            iv_play.setOnClickListener(this);


        }

        void bind(int listIndex) {
            tv_trailer.setText("Trailer" + (listIndex + 1));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String tag = (String) v.getTag();
            mOnClickListener.onClick(clickedPosition, tag);
        }

    }
}
