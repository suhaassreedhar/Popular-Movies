package com.example.suhaas.project1.database;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import com.example.suhaas.project1.constants.Constants;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.suhaas.project1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAV_MOVIE = MovieEntry.TABLE_NAME;
    public static final String PATH_FAV_MOVIE_TRAILER = TrailerEntry.TABLE_NAME;
    public static final String PATH_FAV_MOVIE_REVIEW = ReviewsEntry.TABLE_NAME;

    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;
        public static final String TABLE_NAME = "fav_movies";

        // Table name
        public static final String COLUMN_MOVIE_ID = Constants.Summary.MOVIE_ID;
        public static final String COLUMN_BACKDROP_PATH = Constants.Summary.BACKDROP_PATH;
        public static final String COLUMN_TITLE = Constants.Summary.ORIGINAL_TITLE;
        public static final String COLUMN_OVERVIEW = Constants.Summary.OVERVIEW;
        public static final String COLUMN_POPULARITY = Constants.Summary.POPULARITY;
        public static final String COLUMN_POSTER_PATH = Constants.Summary.POSTER_PATH;
        public static final String COLUMN_RELEASE_DATE = Constants.Summary.RELEASE_DATE;
        public static final String COLUMN_AVG_VOTES = Constants.Summary.VOTE_AVERAGE;
        public static final String COLUMN_COUNT_VOTES = Constants.Summary.VOTE_COUNT;
        public static String TAG = MovieEntry.class.getSimpleName();

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieUriByTitle(String title) {
            return CONTENT_URI.buildUpon()
                    .appendPath(title).build();
        }

        public static Uri buildMovieUriById(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id)).build();
        }

        public static Uri buildMovieUriByRating(double rating) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Double.toString(rating)).build();
        }

        public static String getMovieIdOrTitleFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE_TRAILER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE_TRAILER;

        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_MOVIE_ID = Constants.Trailer.ID;
        public static final String COLUMN_TRAILER_KEY = Constants.Trailer.KEY;
        public static final String COLUMN_TRAILER_NAME = Constants.Trailer.NAME;
        public static final String COLUMN_THUMBNAIL_PATH = Constants.Trailer.THUMB;
        public static final String COLUMN_TRAILER_URL = Constants.Trailer.URL;

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrailerUriByMovieTitle(String title) {
            return CONTENT_URI.buildUpon()
                    .appendPath(title).build();
        }

        public static Uri buildTrailerUriByMovieId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id)).build();
        }

        public static String getMovieIdOrTitleFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class ReviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE_REVIEW).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE_REVIEW;

        public static final String TABLE_NAME = "review";
        public static final String COLUMN_MOVIE_ID = Constants.Review.ID;
        public static final String COLUMN_AUTHOR_NAME = Constants.Review.NAME;
        public static final String COLUMN_REVIEW_CONTENT = Constants.Review.REVIEW;


        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewUriByMovieTitle(String title) {
            return CONTENT_URI.buildUpon()
                    .appendPath(title).build();
        }

        public static Uri buildReviewUriByMovieId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id)).build();
        }

        public static String getMovieIdOrTitleFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
