package edu.self.thirty_days_of_kotlin.movielist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raywenderlich.wewatch.network.RetrofitClient
import edu.self.thirty_days_of_kotlin.R
import edu.self.thirty_days_of_kotlin.model.ResultsItem
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieListAdapter(var movieList: List<ResultsItem?>?, val listener: OnItemClickListener) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view).listen { pos, type ->
            val item = movieList?.get(pos)
            listener.onItemClick(item, pos)
        }
    }

    override fun getItemCount(): Int {
        return if (movieList.isNullOrEmpty()) {
            0
        } else movieList!!.size
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(RetrofitClient.TMDB_IMAGEURL.plus(movieList?.get(position)?.posterPath))
            .placeholder(R.drawable.ic_panorama_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .into(holder.ivMovie)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivMovie = view.movies_item
    }

    interface OnItemClickListener {
        fun onItemClick(item: ResultsItem?, pos: Int)
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
}