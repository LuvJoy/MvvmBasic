package com.joseph.mvvmbasic.data.model


import com.google.gson.annotations.SerializedName

data class MovieDetails(
    @SerializedName("budget")
    var budget: Int,
    @SerializedName("homepage")
    var homepage: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("imdb_id")
    var imdbId: String,
    @SerializedName("original_language")
    var originalLanguage: String,
    @SerializedName("original_title")
    var originalTitle: String,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("popularity")
    var popularity: Double,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("release_date")
    var releaseDate: String,
    @SerializedName("revenue")
    var revenue: Long,
    @SerializedName("runtime")
    var runtime: Int,
    @SerializedName("status")
    var status: String,
    @SerializedName("tagline")
    var tagline: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("video")
    var video: Boolean,
    @SerializedName("vote_average")
    var voteAverage: Float,
    @SerializedName("vote_count")
    var voteCount: Int
)