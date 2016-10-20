package com.example.suhaas.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.example.suhaas.project1.constants.Constants;
import com.example.suhaas.project1.database.DatabaseWrapper;
import com.example.suhaas.project1.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Communication{

    private String TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Bind(R.id.my_toolbar)
    public Toolbar myToolbar;
    public boolean mTwoPane;
    private String mSortMode = Constants.LocalKeys.MOST_POPULAR;
    private Movie mMovie;
    FloatingActionButton fav_btn;
    DatabaseWrapper db;
    boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            fav_btn = (FloatingActionButton) findViewById(R.id.fav_movie_button);

            db = new DatabaseWrapper(this);
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Favourite Movie", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    long id = db.addMovie(mMovie.getId(), mMovie.getTitle(), mMovie.getPosterpath(), mMovie.getBackground(), mMovie.getOverview(), mMovie.getRating(), mMovie.getDate(), mMovie.getPopularity(), mMovie.getVote_count());
                    if (fav) {
                        int i = (int) db.removeMovie(mMovie.getId());
                        if (i == 1)
                            Snackbar.make(view, String.format("%s removed from favourites", mMovie.getTitle()), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        fav_btn.setImageDrawable(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_favorite_border_white_24dp));
                    } else {
                        Snackbar.make(view, String.format("%s added to favourites", mMovie.getTitle()), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        fav_btn.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_favorite_white_24dp));
                    }
                    fav = !fav;
                }
            });
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(String mode, Movie movie) {
        mMovie=movie;
        if (mTwoPane) {
            if (db.isFavourite(mMovie.getId())) {
                fav_btn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
                fav = true;
            }
            else
                fav_btn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));

            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                    movie);
            arguments.putString(Constants.Api.SORT_KEY_PARAM, mSortMode);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment,DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(Constants.Api.SORT_KEY_PARAM,mSortMode)
                    .putExtra(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                            movie);
            startActivity(intent);
        }
    }
}
