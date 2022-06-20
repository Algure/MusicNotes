package com.algure.musicnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.algure.musicnotes.R
import com.algure.musicnotes.objects.MusicData


class MusicListAdapter(private val dataSet: List<MusicData>, onItemClicked:(Int) -> Unit) :
    RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    val onItemClicked:(Int) -> Unit

    init{
        this.onItemClicked = onItemClicked
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val descTextView: TextView
        val progress: ProgressBar
        val linearLayout: LinearLayout
        val button1: ImageView
        val button2: ImageView

        init {
            titleTextView = view.findViewById(R.id.title)
            descTextView = view.findViewById(R.id.subtitle)
            progress = view.findViewById(R.id.progress)
            linearLayout = view.findViewById(R.id.line1)
            button1 = view.findViewById(R.id.backbtn)
            button2 = view.findViewById(R.id.nextbtn)

            button1.visibility = INVISIBLE
            button2.visibility = INVISIBLE
//            progress.visibility = INVISIBLE
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.notification_large, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.titleTextView.text = dataSet[position].title
        viewHolder.descTextView.text = dataSet[position].description
        viewHolder.linearLayout.setBackgroundColor( dataSet[position].color)
        viewHolder.linearLayout.setOnClickListener({
            onItemClicked(position)
        })

    }

    override fun getItemCount() = dataSet.size

}
