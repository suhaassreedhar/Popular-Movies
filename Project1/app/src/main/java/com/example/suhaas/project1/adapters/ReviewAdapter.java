package com.example.suhaas.project1.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.suhaas.project1.R;
import com.example.suhaas.project1.model.Review;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewAdapter extends ArrayAdapter<Review>{
    public ReviewAdapter(Context context, List<Review> reviewList) {
        super(context, 0, reviewList);
    }

    public ReviewAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Review review = getItem(position);
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.author.setText(review.getAuthor());
        viewHolder.content.setText(Html.fromHtml(review.getContent()));
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.review_user)
        TextView author;
        @Bind(R.id.review_content)
        TextView content;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
