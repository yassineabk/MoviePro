package com.example.filmos
import DatabaseHandler
import ItemAdapter
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.details.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRestart() {
        super.onRestart()
        setupListofDataIntoRecyclerView()
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailId = intent.getStringExtra("email_id")
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        searchText.setOnClickListener {
            searchText.text.clear()
        }
        // Click event of the search button.
        searchButton.setOnClickListener { view ->
            setupSearchedListofDataIntoRecyclerView(searchText.text.toString())
        }
        // Click event of the radio button Acceptable.
        radioBtnAcceptable.setOnClickListener {
            setupListofAcceptableDataIntoRecyclerView()
        }
        // Click event of the radio button Mauvais.
        radioBtnMauvais.setOnClickListener {
            setupListofMauvaisDataIntoRecyclerView()
        }
        // Click event of the add button.
        btnAdd.setOnClickListener { view ->
            addRecord()
        }
        setupListofDataIntoRecyclerView()
    }
    //Method for saving the movie records in database
    @RequiresApi(Build.VERSION_CODES.R)
    private fun addRecord() {
        val name = etName.text.toString()
        val director = etDirector.text.toString()
        val release = etReleaseDate.text.toString()
        val nbrS = etNbrS.text.toString().toInt()
        val nbrS_Field = etNbrS.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !director.isEmpty() && !release.isEmpty() && !nbrS_Field.isEmpty()) {
            val status =
                databaseHandler.addMovie(MovieModelClass(0, name, director,release,nbrS))
            if (status > -1) {
                Toast.makeText(applicationContext, "Movie saved", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etDirector.text.clear()
                etReleaseDate.text.clear()
                etNbrS.text.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Fields cannot be blank !",
                Toast.LENGTH_LONG
            ).show()
        }
        setupListofDataIntoRecyclerView()
    }

    /**
     * Function is used to get the Items List which is added in the database table.
     */
    private fun getItemsList(): ArrayList<MovieModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewMovie method of DatabaseHandler class to read the records
        val movieList: ArrayList<MovieModelClass> = databaseHandler.viewMovie()

        return movieList
    }
    /**
     * Function is used to get the Items List filterd by Nombre de sorties which is added in the database table.
     */
    private fun getItemsListAcceptable(): ArrayList<MovieModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewMovie method of DatabaseHandler class to read the records
        val movieList: ArrayList<MovieModelClass> = databaseHandler.viewMovieByNbrSAcceptable()

        return movieList
    }
    /**
     * Function is used to get the Items List filterd by Nombre de sorties which is added in the database table.
     */
    private fun getItemsListMauvais(): ArrayList<MovieModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewMovie method of DatabaseHandler class to read the records
        val movieList: ArrayList<MovieModelClass> = databaseHandler.viewMovieByNbrSMauvais()

        return movieList
    }
    /**
     * Function is used to get the Searched Items List which is added in the database table.
     */
    private fun getSearchedItemsList(name:String): ArrayList<MovieModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewMovie method of DatabaseHandler class to read the records
        val movieList: ArrayList<MovieModelClass> = databaseHandler.viewMovieByName(name)

        return movieList
    }
    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setupListofAcceptableDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsListAcceptable())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }private fun setupListofMauvaisDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsListMauvais())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }
    /**
     * Function is used to show the list (search) on UI of inserted data.
     */
    private fun setupSearchedListofDataIntoRecyclerView(name:String) {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getSearchedItemsList(name))
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Method is used to show the custom details dialog.
     */
    fun showDetails(movieModelClass: MovieModelClass) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("movie_name", movieModelClass.name)
        intent.putExtra("movie_director", movieModelClass.director)
        intent.putExtra("movie_release", movieModelClass.release)
        intent.putExtra("movie_nbrS", movieModelClass.nbrS)
        startActivity(intent)
        finish()
    }
    /**
     * Method is used to show the custom details dialog for updating the data.
     */
    fun showUpdate(movieModelClass: MovieModelClass) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("movie_id", movieModelClass.id)
        intent.putExtra("movie_name", movieModelClass.name)
        intent.putExtra("movie_director", movieModelClass.director)
        intent.putExtra("movie_release", movieModelClass.release)
        intent.putExtra("movie_nbrS", movieModelClass.nbrS)
        startActivity(intent)
        finish()
    }

    /**
     * Method is used to show the delete alert dialog.
     */
    fun deleteRecordAlertDialog(movieModelClass: MovieModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Movie")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${movieModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteMovie(MovieModelClass(movieModelClass.id, "", "","", 0))
            if (status > -1) {
                Toast.makeText(
                        applicationContext,
                        "Movie deleted successfully.",
                        Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}