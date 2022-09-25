package com.lbdev.dumbcharadestool

data class MoviesList(
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)