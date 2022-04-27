package com.example.josephsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MovieDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movie = intent.getStringArrayExtra("Movie Array")
        val movieDetailTitle = findViewById<TextView>(R.id.movie_detail_title)
        val movieDetailYear = findViewById<TextView>(R.id.movie_detail_year)
        val movieDetailDirector = findViewById<TextView>(R.id.movie_detail_director)
        val movieDetailDescription = findViewById<TextView>(R.id.movie_detail_description)
        val moviePoster = findViewById<ImageView>(R.id.movie_detail_image)


        val imgurl = movie!![3]
        movieDetailTitle.text = movie?.get(0) ?: null
        movieDetailYear.text = movie?.get(1) ?: null
        movieDetailDirector.text = movie?.get(2) ?: null
        movieDetailDescription.text = movie?.get(4) ?:null
        Picasso.get().load(imgurl).into(moviePoster)

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        fun onContextItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> {
                    this.finish()
                    return true
                }
            }
            return super.onContextItemSelected(item)
        }


    }
}