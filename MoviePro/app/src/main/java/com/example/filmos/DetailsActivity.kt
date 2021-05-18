package com.example.filmos

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)
        val movie_name = intent.getStringExtra("movie_name")
        val movie_director = intent.getStringExtra("movie_director")
        val movie_release = intent.getStringExtra("movie_release")
        val movie_nbrS = intent.getIntExtra("movie_nbrS",0)
        val movie_image = intent.getByteArrayExtra("movie_image")

        movieName.text = movie_name.toString()
        movieDir.text = movie_director.toString()
        movieRel.text = movie_release.toString()
        movieEN.text = movie_nbrS.toString()
        var bitmap = movie_image?.let { BitmapFactory.decodeByteArray(movie_image, 0, it.size) }
        imgView.setImageBitmap(bitmap)

        btn_back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}