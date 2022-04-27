package com.example.josephsproject

import android.content.Intent
import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class CustomAdapter(private val dataSet: Array<Array<String>>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val yearTextView: TextView
        val posterImageView: ImageView


        init {
            // Define click listener for the ViewHolder's View.
            titleTextView = view.findViewById(R.id.movie_title)
            yearTextView = view.findViewById(R.id.movie_year)
            posterImageView = view.findViewById(R.id.movie_poster)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Picasso.get().setLoggingEnabled(true)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.titleTextView.text = dataSet[position][0]
        viewHolder.yearTextView.text = dataSet[position][1]
        Picasso.get().load(dataSet[position][3]).into(viewHolder.posterImageView)


        viewHolder.titleTextView.setOnClickListener {
            val context = viewHolder.titleTextView.context
            val intent = Intent(context,MovieDetail::class.java).apply{
                putExtra("Movie Array", dataSet[position])
            }
            context.startActivity(intent)
        }
        viewHolder.posterImageView.setOnClickListener{
            val context = viewHolder.posterImageView.context
            val intent = Intent(context,MovieDetail::class.java).apply{
                putExtra("Movie Array", dataSet[position])
            }
            context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
