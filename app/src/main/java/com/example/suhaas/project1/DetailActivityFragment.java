package com.example.suhaas.project1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suhaas.project1.ServiceManager.MovieClient;
import com.example.suhaas.project1.ServiceManager.MovieService;
import com.example.suhaas.project1.adapters.ReviewAdapter;
import com.example.suhaas.project1.adapters.TrailerAdapter;
import com.example.suhaas.project1.constants.Constants;
import com.example.suhaas.project1.model.ApiResult;
import com.example.suhaas.project1.model.Movie;
import com.example.suhaas.project1.model.Review;
import com.example.suhaas.project1.model.Trailer;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    @Bind(R.id.detailImage)
    ImageView mImageView;
    @Bind(R.id.movieName)
    TextView mTitleView;
    @Bind(R.id.movieDetail)
    TextView mOverviewView;
    @Bind(R.id.year)
    TextView mDateView;
    @Bind(R.id.rating)
    TextView mVoteAverageView;
    private Movie mMovie;
    private String mode;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private MovieService movieService;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY);
            mode = arguments.getString(Constants.Api.SORT_KEY_PARAM);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (mMovie != null) {

            String image_url = Constants.Api.IMAGE_URL_HIGH_QUALITY + mMovie.getPosterpath();
            Picasso.with(getContext()).load(image_url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(mImageView);
            mTitleView.setText(mMovie.getTitle());
            mOverviewView.setText(mMovie.getOverview());
            String movie_date = mMovie.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mDateView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mVoteAverageView.setText(String.format("%.2f from %d votes", mMovie.getRating(), mMovie.getVote_count()));

            final List<Review> reviews = new ArrayList<>();
            final List<Trailer> trailers = new ArrayList<>();

            reviewAdapter = new ReviewAdapter(getActivity(), reviews);
            trailerAdapter = new TrailerAdapter(getActivity(), trailers);

            LinearListView reviewList = (LinearListView) rootView.findViewById(R.id.reviewList);
            reviewList.setAdapter(reviewAdapter);

            LinearListView trailerList = (LinearListView) rootView.findViewById(R.id.trailerList);
            trailerList.setAdapter(trailerAdapter);

            trailerList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, long id) {

                    String youtubeVideoId = trailers.get(position).getKey();
                    String videoURI = "vnd.youtube:" + youtubeVideoId;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURI));
                    startActivity(i);
                }
            });
            reviewList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(reviews.get(position).getUrl()));
                    startActivity(intent);
                }
            });

            movieService = MovieClient.createService(MovieService.class);

            fetchReviews();
            fetchTrailers();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);

    }

    private void fetchReviews() {
        Call<ApiResult<Review>> reviewCall = movieService.getReviews(mMovie.getId());
        reviewCall.enqueue(new Callback<ApiResult<Review>>() {
            @Override
            public void onResponse(Call<ApiResult<Review>> call, Response<ApiResult<Review>> response) {
                List<Review> reviewList = response.body().getResults();
                reviewAdapter.clear();
                for (Review r : reviewList) {
                    reviewAdapter.add(r);
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Review>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private void fetchTrailers() {
        Call<ApiResult<Trailer>> trailerCall = movieService.getTrailers(mMovie.getId());
        trailerCall.enqueue(new Callback<ApiResult<Trailer>>() {
            @Override
            public void onResponse(Call<ApiResult<Trailer>> call, Response<ApiResult<Trailer>> response) {
                List<Trailer> trailerList = response.body().getResults();
                trailerAdapter.clear();
                for (Trailer r : trailerList) {
                    trailerAdapter.add(r);
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Trailer>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }
}
