package com.example.suhaas.project1.database;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DatabaseWrapper {
    private final String TAG = DatabaseWrapper.class.getSimpleName();

    private final Context mContext;

    public DatabaseWrapper(Context context) {
        mContext = context;
    }

    public boolean isFavourite(long id) {

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Long.toString(id)},
                null);
        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            boolean resid = id==movieCursor.getLong(movieIdIndex);
            movieCursor.close();
            return resid;
        } else {
            movieCursor.close();
            return false;
        }
    }

    public long addMovie(long id, String title, String image, String image2, String overview, double rating, String date, double popularity, long vote_count) {
        long movieId;
        if (isFavourite(id)) {
            movieId = id;
            return movieId;
        } else {
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
            movieValues.put(MovieContract.MovieEntry.COLUMN_COUNT_VOTES, vote_count);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, date);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, image);
            movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, image2);
            movieValues.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, rating);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            movieId = ContentUris.parseId(insertedUri);
        }
        return movieId;
    }

    public long removeMovie(long id) {

        if (isFavourite(id)) {
            final int delete = mContext.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.TABLE_NAME +
                            "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{Long.toString(id)}
            );
            Log.d(TAG, "removeMovie: Deleted " + delete);
            return delete;
        }
        return -1;
    }
}
