package edu.self.thirty_days_of_kotlin.moviedetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.self.thirty_days_of_kotlin.R
import edu.self.thirty_days_of_kotlin.moviedetails.model.ReviewItem
import kotlinx.android.synthetic.main.reviews_item.view.*


class ReviewsAdapter(var reviewList: List<ReviewItem?>?) :
    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val autor = view.tv_author
        val comments = view.tv_comments

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reviews_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (reviewList.isNullOrEmpty()) {
            0
        } else reviewList!!.size
    }

    override fun onBindViewHolder(holder: ReviewsAdapter.ViewHolder, position: Int) {
        holder.autor.text = reviewList?.get(position)?.author
        holder.comments.text = reviewList?.get(position)?.content
    }
}