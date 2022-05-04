package com.example.josephsproject

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CameraAdapter(private val camList: ArrayList<Camera>) :
    RecyclerView.Adapter<CameraAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val camTitleTextView: TextView
        val camFeedImageView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            camTitleTextView = view.findViewById(R.id.camTitle)
            camFeedImageView = view.findViewById(R.id.camFeed)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.camera_row_item, viewGroup, false)
        return ViewHolder(view)
        Log.e("Tag","ViewHolderCreated")
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Picasso.get().setLoggingEnabled(true)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.camTitleTextView.text = camList[position].Description

        Picasso.get().load(camList[position].ImageUrl).into(viewHolder.camFeedImageView)


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = camList.size

}
