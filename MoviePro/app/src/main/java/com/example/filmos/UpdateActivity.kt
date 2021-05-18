package com.example.filmos

import DatabaseHandler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_update.*

class UpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_update)

        val movie_id = intent.getIntExtra("movie_id",1)
        val movie_name = intent.getStringExtra("movie_name")
        val movie_director = intent.getStringExtra("movie_director")
        val movie_release = intent.getStringExtra("movie_release")
        val movie_nbrS = intent.getIntExtra("movie_nbrS",0)
        val movie_image = intent.getByteArrayExtra("movie_image")

        etUpdateName.text.append(movie_name.toString())
        etUpdateDirector.text.append(movie_director.toString())
        etUpdateReleaseDate.text.append(movie_release.toString())
        etUpdateNbrS.text.append(movie_nbrS.toString())
        etID.text.append(movie_id.toString())

        tvUpdate.setOnClickListener {
            val id = etID.text.toString().toInt()
            val name = etUpdateName.text.toString()
            val director = etUpdateDirector.text.toString()
            val release = etUpdateReleaseDate.text.toString()
            val nbrS = etUpdateNbrS.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !director.isEmpty() && !release.isEmpty() && !nbrS.isEmpty()) {
                val status =
                    databaseHandler.updateMovie(MovieModelClass(id, name, director, release, nbrS.toInt()))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Movie Updated.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Fields cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        tvCancel.setOnClickListener {
            //Back to main
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}