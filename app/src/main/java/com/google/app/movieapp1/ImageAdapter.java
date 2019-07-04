


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
package com.google.app.movieapp1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.app.movieapp1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ImageAdapter} exposes a list of weather forecasts to a
 * {@link RecyclerView}
 */
public class ImageAdapter extends BaseAdapter {

    //    public static String APP_NAME = "MainActivity_1";
    private List<Movie> mmovieData = new ArrayList<>();
    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
    }


    public ImageAdapter(Context context, List<Movie> mmovieData) {
        this.mmovieData = mmovieData;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mmovieData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = mmovieData.get(position);

        if (convertView == null) {

            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.movie_list, null);

            //Complete suggestion from the reviewer
            final ViewHolder holder = new ViewHolder();
            holder.mImageView = convertView.findViewById(R.id.iv_movie);
            convertView.setTag(holder);
        }

//        ImageView imageView = convertView.findViewById(R.id.iv_movie);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        ImageView imageView = holder.mImageView;
        String contentDesc = movie.getTitle();
        if (contentDesc == null || contentDesc.isEmpty())
            imageView.setContentDescription("");
        else
            imageView.setContentDescription(contentDesc);

        String imagePath = MainActivity.mainActivity.getResources().getString(R.string.imagePath);
        String path = imagePath + movie.getPoster_path();

        Picasso.with(mContext)
                .load(path)
                .placeholder(R.drawable.loading_red)
                .error(R.drawable.imagenotfound)
                .into(imageView);




        return convertView;
    }

    public class ViewHolder {

        private ImageView mImageView;

    }

    public void setMovieData(List<Movie> movies) {
        mmovieData = null;
        mmovieData = movies;
        this.notifyDataSetChanged();
    }
}