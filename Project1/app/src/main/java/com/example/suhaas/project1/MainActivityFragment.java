package com.example.suhaas.project1;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.suhaas.project1.ServiceManager.MovieClient;
import com.example.suhaas.project1.ServiceManager.MovieService;
import com.example.suhaas.project1.adapters.ImageAdapter;
import com.example.suhaas.project1.constants.Constants;
import com.example.suhaas.project1.database.MovieContract;
import com.example.suhaas.project1.model.ApiResult;
import com.example.suhaas.project1.model.Movie;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ImageAdapter.OnItemClickListener {

    private static final int FAVOURITE_LOADER = 0;
    private final String TAG = MainActivityFragment.class.getSimpleName();
    @Bind(R.id.movies_recycler_view)
    RecyclerView mRecycleGridView;
    private ImageAdapter mMovieAdapter;
    private ImageAdapter mFavouriteAdapter;
    private MovieService mMovieService;
    private Cursor mCursor;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private String mode = Constants.LocalKeys.MOST_POPULAR;
    final String SELECTED_KEY = "poster_Position";
    int posterPosition = mRecycleGridView.INVALID_TYPE;
    Bundle myBundle;
    Spinner spinner;
    int spinnerPosition;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVOURITE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieService = MovieClient.createService(MovieService.class);
        ButterKnife.bind(this, view);
        mMovieAdapter = new ImageAdapter(getActivity(), mMovies,this);
        mFavouriteAdapter = new ImageAdapter(getActivity(), mCursor, this);
        onViewStateRestored(savedInstanceState);
        updateMovies(mode);

//        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
//            posterPosition = savedInstanceState.getInt(SELECTED_KEY);
//        }
        return view;
    }

    public void updateMovies(final String mode) {
        if (mode == Constants.LocalKeys.FAVOURITES) {
            mRecycleGridView.setAdapter(mFavouriteAdapter);
            mRecycleGridView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.movie_columns)));
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                if (mainActivity.mTwoPane)
                    mainActivity.onItemSelected(mode,mFavouriteAdapter.getFirstMovie());
            }
        } else {
            mRecycleGridView.setAdapter(mMovieAdapter);
            mRecycleGridView.setLayoutManager(new GridLayoutManager(getContext(),getResources().getInteger(R.integer.movie_columns)));

            Call<ApiResult<Movie>> moviesCall = mMovieService.getMovies(mode);
            moviesCall.enqueue(new Callback<ApiResult<Movie>>() {
                @Override
                public void onResponse(Call<ApiResult<Movie>> call, Response<ApiResult<Movie>> response) {
                    if(!mMovies.equals(response.body().getResults())) {
                        mMovies.clear();
                        mMovies.addAll(response.body().getResults());
                        mMovieAdapter.notifyDataSetChanged();
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null) {
                            if(mainActivity.mTwoPane)
                                mainActivity.onItemSelected(mode,mMovieAdapter.getFirstMovie());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResult<Movie>> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("spinner", spinner.getSelectedItemPosition());
        outState.putInt(SELECTED_KEY, posterPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            posterPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState != null) {
            this.myBundle = savedInstanceState;
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFavouriteAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavouriteAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(Movie movie) {
        ((Communication) getActivity())
                .onItemSelected(mode, movie);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.spinnerr);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        String[] sortingCriteria = {"Popular", "Highest Rated", "Favorites"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, sortingCriteria);
        spinner.setAdapter(spinnerAdapter);
        if(this.myBundle != null){
            spinner.setSelection(myBundle.getInt("spinner", 0));
        }
        else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    spinnerPosition = 0;
                    mode = Constants.LocalKeys.MOST_POPULAR;
                    updateMovies(mode);
                    restartPosterLoader();
                }
                else if (position == 1){
                    spinnerPosition = 1;
                    mode = Constants.LocalKeys.HIGHEST_RATED;
                    updateMovies(mode);
                    restartPosterLoader();
                }
                else if (position == 2){
                    spinnerPosition = 2;
                    mode = Constants.LocalKeys.FAVOURITES;
                    updateMovies(mode);
                    restartPosterLoader();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void restartPosterLoader(){
        Log.i("RESTART LOADER", "");
        getLoaderManager().initLoader(spinnerPosition, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    public interface Communication {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String mode, Movie movie);
    }
}
