package com.joseph.mvvmbasic.data.api

import com.joseph.mvvmbasic.data.model.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDbInterface {
    //https://api.themoviedb.org/3/movie/550?api_key=a1f8376a2ee3f152380b68f443070d2c

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int
    ) : Single<MovieDetails>
}