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
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.app.movieapp1.R;
import com.google.app.movieapp1.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private static int viewHolderCount;

    private List<Review> mReviews;

    public ReviewAdapter(List<Review> mReviews, Context context) {
        this.mReviews = mReviews;
        viewHolderCount = 0;
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        viewHolder.tv_review.setText("ViewHolder index: " + viewHolderCount);

        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView tv_author;
        // Will display which ViewHolder is displaying this data
        TextView tv_review;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_review = (TextView) itemView.findViewById(R.id.tv_review);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex) {
            tv_author.setText(mReviews.get(listIndex).getAuthor());
            tv_review.setText(mReviews.get(listIndex).getContent());

        }

    }
}
