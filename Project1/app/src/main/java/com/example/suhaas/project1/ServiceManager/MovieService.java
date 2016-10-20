package com.example.suhaas.project1.ServiceManager;


import com.example.suhaas.project1.constants.Constants;
import com.example.suhaas.project1.model.ApiResult;
import com.example.suhaas.project1.model.Movie;
import com.example.suhaas.project1.model.Review;
import com.example.suhaas.project1.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

    @GET("{mode}" + Constants.Api.API_KEY_PARAM)
    Call<ApiResult<Movie>> getMovies(@Path("mode") String mode);

    @GET("{id}/reviews" + Constants.Api.API_KEY_PARAM)
    Call<ApiResult<Review>> getReviews(@Path("id") long id);

    @GET("{id}/videos" + Constants.Api.API_KEY_PARAM)
    Call<ApiResult<Trailer>> getTrailers(@Path("id") long id);
}
