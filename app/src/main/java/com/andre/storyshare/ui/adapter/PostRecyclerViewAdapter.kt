package com.andre.storyshare.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.andre.storyshare.R
import com.andre.storyshare.data.model.DataStory
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class PostRecyclerViewAdapter(
    private val context: Context,
    private var dataStoryList: List<DataStory>,
) : RecyclerView.Adapter<PostRecyclerViewAdapter.StoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.template_card_post, parent, false)
        return StoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val stories = dataStoryList[position]
        holder.bind(stories)
    }

    override fun getItemCount(): Int {
        return dataStoryList.size
    }

    inner class StoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(story: DataStory) {
            itemView.findViewById<MaterialTextView>(R.id.cardPostName).text = story.name
            itemView.findViewById<MaterialTextView>(R.id.cardPostDesc).text = story.description
            itemView.findViewById<MaterialTextView>(R.id.cardPostDate).text = formatDate(story.createdAt)
            Glide.with(context).load(story.photoUrl).into(itemView.findViewById(R.id.cardPostPicture))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date: String): String{
        val inputFormatter = DateTimeFormatter.ISO_INSTANT
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        val instant = Instant.from(inputFormatter.parse(date))
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = instant.atZone(zoneId)
        return(outputFormatter.format(zonedDateTime))
    }
    fun updateData(stories: List<DataStory>) {
        dataStoryList = stories
        notifyDataSetChanged()
    }
}