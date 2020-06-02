package edu.self.thirty_days_of_kotlin.moviedetails.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.self.thirty_days_of_kotlin.R
import edu.self.thirty_days_of_kotlin.moviedetails.model.VideoItem
import kotlinx.android.synthetic.main.trailers_item.view.*


class TrailerAdapter(val trailerList: List<VideoItem?>?) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.trailers_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (trailerList.isNullOrEmpty()) {
            0
        } else trailerList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val thumbUrl: String =
            "http://img.youtube.com/vi/".plus(trailerList?.get(position)?.key)
                .plus("/hqdefault.jpg")

        holder.mTextView.setOnClickListener {
            shareYoutubeLink(
                "https://www.youtube.com/watch?v=".plus(trailerList?.get(position)?.key),
                getActivity(holder.mIvPlay.context)!!
            )
        }


        holder.mIvPlay.setOnClickListener { view ->
            trailerList?.get(position)?.key?.let {
                openYoutubeLink(
                    it, view.context
                )
            }
        }


        Glide.with(holder.mIvTrailerThumb.context)
            .load(thumbUrl)
            .placeholder(R.drawable.ic_panorama_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .into(holder.mIvTrailerThumb);
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvTrailerThumb = view.iv_trailer_thumb
        val mIvPlay = view.ivPlay
        val mTextView = view.textView

    }


    private fun getActivity(contextPassed: Context): Activity? {
        var context: Context = contextPassed
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    private fun shareYoutubeLink(
        link: String,
        context: Context
    ) {
        val intentBuilder = IntentBuilder.from((context as Activity))
        val intent = intentBuilder
            .setType("text/plain")
            .setText(link)
            .setChooserTitle("Choose to share trailer link")
            .createChooserIntent()
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent)
        }
    }


    private fun openYoutubeLink(
        key: String,
        context: Context
    ) {
        val applicationIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$key"))
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$key")
        )
        try {
            context.startActivity(applicationIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(browserIntent)
        }
    }


}