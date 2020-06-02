package edu.self.thirty_days_of_kotlin.movielist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.raywenderlich.wewatch.network.RetrofitClient
import edu.self.thirty_days_of_kotlin.R
import edu.self.thirty_days_of_kotlin.model.ResultsItem
import edu.self.thirty_days_of_kotlin.moviedetails.MovieDetailsActivity
import edu.self.thirty_days_of_kotlin.movielist.adapter.MovieListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class MainActivity : AppCompatActivity(), MovieListAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = RetrofitClient.moviesApi
        loaderVisibility(true)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getTopRatedMovies(RetrofitClient.API_KEY)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        loaderVisibility(false)
                        Log.d("res", response.body().toString())
                        //Do something with response e.g show to the UI.
                        val gridLayoutManager = GridLayoutManager(this@MainActivity, 3)
                        rv_movies.layoutManager = gridLayoutManager
                        val movielist = response.body()?.results
                        Log.d("res", response.body()?.results.toString())
                        rv_movies.adapter = MovieListAdapter(
                            movieList = movielist,
                            listener = this@MainActivity
                        )
                        (rv_movies.adapter as MovieListAdapter).notifyDataSetChanged()

                    } else {
                        loaderVisibility(false)

                        toast("Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    loaderVisibility(false)

                    toast("Exception ${e.message}")
                } catch (e: Throwable) {
                    loaderVisibility(false)

                    toast("Ooops: Something else went wrong")
                }
            }
        }
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG)

    }


    private fun loaderVisibility(b: Boolean) {
        if (b) {
            pb_loading_indicator.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            tv_error_message_display.visibility = View.INVISIBLE
        } else {
            pb_loading_indicator.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            tv_error_message_display.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(item: ResultsItem?, pos: Int) {
        val i = Intent(this@MainActivity, MovieDetailsActivity::class.java)
        i.putExtra("title", item?.title)
        i.putExtra("poster_url", item?.posterPath)
        i.putExtra("release_date", item?.releaseDate)
        i.putExtra("rating", item?.voteAverage)
        i.putExtra("plot", item?.overview)
        i.putExtra("id", item?.id)
        startActivity(i)

    }
}
