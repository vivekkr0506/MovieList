package com.example.saveomovie.models

import com.example.saveomovie.models.MovieDetails

data class Movies(
    val page: Int,
    val results: List<MovieDetails>,
    val total_pages: Int,
    val total_results: Int
)
