package com.example.suhaas.project1.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = MovieDbHelper.class.getSimpleName();

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE," +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_AVG_VOTES + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_COUNT_VOTES + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.TrailerEntry.COLUMN_THUMBNAIL_PATH + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL," +
                MovieContract.TrailerEntry.COLUMN_TRAILER_URL + " TEXT NOT NULL," +

                " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")); ";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewsEntry.TABLE_NAME + " (" +
                MovieContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.ReviewsEntry.COLUMN_AUTHOR_NAME + " TEXT NOT NULL, " +
                MovieContract.ReviewsEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")); ";
        Log.i(TAG, "onCreate: " + SQL_CREATE_MOVIE_TABLE);
        Log.i(TAG, "onCreate: " + SQL_CREATE_TRAILER_TABLE);
        Log.i(TAG, "onCreate: " + SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
