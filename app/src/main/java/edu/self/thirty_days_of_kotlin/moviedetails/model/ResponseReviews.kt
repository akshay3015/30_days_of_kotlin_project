package edu.self.thirty_days_of_kotlin.moviedetails.model

import com.google.gson.annotations.SerializedName

data class ResponseReviews(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<ReviewItem?>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)

data class ReviewItem(

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
