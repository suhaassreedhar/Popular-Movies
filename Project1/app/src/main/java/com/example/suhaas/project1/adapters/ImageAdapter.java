package com.example.suhaas.project1.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.suhaas.project1.R;
import com.example.suhaas.project1.constants.Constants;
import com.example.suhaas.project1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private List<Movie> movieList;
    private  Context mContext;
    private OnItemClickListener mlistener;
    private Cursor mCursor;
    private boolean fromDB;

    public ImageAdapter(Context context, List<Movie> movieDataList, OnItemClickListener listener) {
        movieList = movieDataList;
        mContext = context;
        mlistener = listener;
    }
    public ImageAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        mCursor = cursor;
        mContext = context;
        mlistener = listener;
        fromDB = true;
    }
    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View posterView = inflater.inflate(R.layout.item_image, parent, false);

        ViewHolder viewHolder = new ViewHolder(posterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (fromDB) {
            if (mCursor != null) {
                mCursor.moveToPosition(position);
                int id = mCursor.getInt(1);
                String title = mCursor.getString(2);
                String poster = mCursor.getString(4);
                String background = mCursor.getString(5);
                String overview = mCursor.getString(3);
                double rating = mCursor.getDouble(6);
                double popularity = mCursor.getDouble(8);
                String date = mCursor.getString(9);
                int vote_count = mCursor.getInt(7);
                Movie movie = new Movie(id, title, poster, background, overview, rating, date, popularity, vote_count);
                holder.bind(movie, mlistener);
            }
        }
        else
            holder.bind(movieList.get(position), mlistener);
    }

    public Movie getFirstMovie(){
        if (fromDB) {
            if (mCursor != null) {
                mCursor.moveToPosition(0);
                int id = mCursor.getInt(1);
                String title = mCursor.getString(2);
                String poster = mCursor.getString(4);
                String background = mCursor.getString(5);
                String overview = mCursor.getString(3);
                double rating = mCursor.getDouble(6);
                double popularity = mCursor.getDouble(8);
                String date = mCursor.getString(9);
                int vote_count = mCursor.getInt(7);
                return new Movie(id, title, poster, background, overview, rating, date, popularity, vote_count);
            }
        }
        else
            return movieList.get(0);
        return null;
    }

    @Override
    public int getItemCount() {
        if (fromDB) {
            return (mCursor == null) ? 0 : mCursor.getCount();

        }
        return movieList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.grid_item_image)
        ImageView posterImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final Movie item, final ImageAdapter.OnItemClickListener listener) {
            String image_url = Constants.Api.IMAGE_URL_LOW_QUALITY + item.getPosterpath();
            Picasso.with(itemView.getContext())
                    .load(image_url)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(posterImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
