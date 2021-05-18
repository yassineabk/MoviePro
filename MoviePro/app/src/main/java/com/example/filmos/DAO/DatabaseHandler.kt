import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.example.filmos.MovieModelClass
import java.io.ByteArrayOutputStream

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MovieDatabase"

        private const val TABLE_CONTACTS = "MovieTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_DIRECTOR = "director"
        private const val KEY_RELEASE_DATE = "release"
        private const val KEY_NBR_S = "nbrS"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DIRECTOR + " TEXT," + KEY_RELEASE_DATE + " TEXT,"
                + KEY_NBR_S + " INTEGER" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    /**
     * Function to insert data
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun addMovie(movie: MovieModelClass): Long {


        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, movie.name) // MovieModelClass Name
        contentValues.put(KEY_DIRECTOR, movie.director) // MovieModelClass Director
        contentValues.put(KEY_RELEASE_DATE, movie.release) // MovieModelClass Release_Date
        contentValues.put(KEY_NBR_S, movie.nbrS) // MovieModelClass NbrS

        // Inserting movie details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    //Method to read the records from database in form of ArrayList
    fun viewMovie(): ArrayList<MovieModelClass> {

        val movieList: ArrayList<MovieModelClass> = ArrayList<MovieModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var director: String
        var release: String
        var nbrS: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                director = cursor.getString(cursor.getColumnIndex(KEY_DIRECTOR))
                release = cursor.getString(cursor.getColumnIndex(KEY_RELEASE_DATE))
                nbrS = cursor.getInt(cursor.getColumnIndex(KEY_NBR_S))

                val movie = MovieModelClass(id = id, name = name, director = director ,release = release, nbrS = nbrS)
                movieList.add(movie)

            } while (cursor.moveToNext())
        }
        return movieList
    }


    //Method to read the records from database in form of ArrayList
    fun viewMovieByName(name: String): ArrayList<MovieModelClass> {

        val movieList: ArrayList<MovieModelClass> = ArrayList<MovieModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS WHERE $KEY_NAME LIKE ?"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, arrayOf('%'+name+'%'))

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var director: String
        var release: String
        var nbrS: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                director = cursor.getString(cursor.getColumnIndex(KEY_DIRECTOR))
                release = cursor.getString(cursor.getColumnIndex(KEY_RELEASE_DATE))
                nbrS = cursor.getInt(cursor.getColumnIndex(KEY_NBR_S))

                val movie = MovieModelClass(id = id, name = name, director = director ,release = release, nbrS = nbrS)
                movieList.add(movie)

            } while (cursor.moveToNext())
        }
        return movieList
    }
    /**
     * Function to update record
     */
    fun updateMovie(movie: MovieModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, movie.name) // MovieModelClass Name
        contentValues.put(KEY_DIRECTOR, movie.director) // MovieModelClass Director
        contentValues.put(KEY_RELEASE_DATE, movie.release) // MovieModelClass Release_Date
        contentValues.put(KEY_NBR_S, movie.nbrS) // MovieModelClass NbrS

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + movie.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
    /**
     * Function to delete record
     */
    fun deleteMovie(movie: MovieModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, movie.id) // MovieModelClass id
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + movie.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
}