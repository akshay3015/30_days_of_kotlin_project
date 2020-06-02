package edu.self.thirty_days_of_kotlin.moviedetails

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raywenderlich.wewatch.network.RetrofitClient
import com.raywenderlich.wewatch.network.RetrofitInterface
import edu.self.thirty_days_of_kotlin.R
import edu.self.thirty_days_of_kotlin.moviedetails.adapter.ReviewsAdapter
import edu.self.thirty_days_of_kotlin.moviedetails.adapter.TrailerAdapter
import kotlinx.android.synthetic.main.movies_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException as HttpException1


class MovieDetailsActivity : AppCompatActivity() {
    private var title: String? = null
    private var plotSynopsis: String? = null
    private var rating: Double? = null
    private var releaseDate: String? = null
    private var posterUrl: String? = null
    private var movieId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.movies_details
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getDataFromInetent()

        if (!TextUtils.isEmpty(movieId)) {
            val service = RetrofitClient.moviesApi

            getReviews(service)
            getTrailer(service)
        }

        Glide.with(this)
            .load(RetrofitClient.TMDB_IMAGEURL.plus(posterUrl))
            .placeholder(R.drawable.ic_panorama_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .into(iv_poster)
    }

    private fun getTrailer(service: RetrofitInterface) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getTrailers(movieId, RetrofitClient.API_KEY)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        Log.d("trailers", response?.body()?.results.toString())
                        rv_trailers.layoutManager =
                            LinearLayoutManager(
                                this@MovieDetailsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )

                        rv_trailers.isNestedScrollingEnabled = false
                        val trailerAdapter =
                            TrailerAdapter(trailerList = response?.body()?.results)
                        rv_trailers.adapter = trailerAdapter


                    } else {
                        toast("Error: ${response.code()}")
                    }
                } catch (e: HttpException1) {
                    toast("Exception ${e.message}")
                } catch (e: Throwable) {
                    toast("Ooops: Something else went wrong")
                }
            }

        }
    }

    private fun getReviews(service: RetrofitInterface) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getMovieReview(movieId, RetrofitClient.API_KEY)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        Log.d("reviews", response?.body()?.results.toString())
                        rv_reviews.layoutManager =
                            LinearLayoutManager(
                                this@MovieDetailsActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )

                        rv_reviews.isNestedScrollingEnabled = false
                        val reviewsAdapter =
                            ReviewsAdapter(reviewList = response?.body()?.results)
                        rv_reviews.adapter = reviewsAdapter


                    } else {
                        toast("Error: ${response.code()}")
                    }
                } catch (e: HttpException1) {
                    toast("Exception ${e.message}")
                } catch (e: Throwable) {
                    toast("Ooops: Something else went wrong")
                }
            }

        }
    }

    private fun getDataFromInetent() {
        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
            supportActionBar?.title = title
        }
        if (intent.hasExtra("id")) {
            movieId = intent.getIntExtra("id", 0)?.toString()
        }
        if (intent.hasExtra("poster_url")) {
            posterUrl = intent.getStringExtra("poster_url")
        }
        if (intent.hasExtra("release_date")) {
            releaseDate = intent.getStringExtra("release_date")
            relese_date.text = releaseDate
        }
        if (intent.hasExtra("rating")) {
            rating = intent.getDoubleExtra("rating", 0.0)
            ratings.text = "Ratings : $rating"
            if (rating != 0.0) {
                val rating_: Float = (rating?.toFloat()?.div(2) ?: 0) as Float
                ratingBar.rating = rating_
                tv_ratings.text = rating?.toString().plus("/10")
            }
        }
        if (intent.hasExtra("plot")) {
            plotSynopsis = intent.getStringExtra("plot")
            tv_movie_description.text = plotSynopsis
        }
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG)

    }
}