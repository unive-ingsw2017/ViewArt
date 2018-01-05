package it.unive.ViewArt.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static it.unive.ViewArt.database.DatabaseStrings.*;


public class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME = "ViewArt";

    private static String DB_PATH = "/data/data/it.unive.ViewArt/databases/";
    // Data Base Name.
    private static final String DATABASE_NAME = "ViewArt";
    // Data Base Version.
    private static final int DATABASE_VERSION = 1;

    private Context context;
    private static SQLiteDatabase sqliteDB;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context Parameters of super() are    1. Context
     *                2. Data Base Name.
     *                3. Cursor Factory.
     *                4. Data Base Version.
     */
    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * By calling this method and empty database will be created into the default system path
     * of your application so we are gonna be able to overwrite that database with our database.
     */
    public void createDataBase() throws IOException {
        //check if the database exists
        boolean databaseExist = checkDataBase();

        if (!databaseExist) {
            this.getWritableDatabase();
            copyDataBase();
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * This method opens the data base connection.
     * First it create the path up till data base of the device.
     * Then create connection with data base.
     */
    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        sqliteDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * This Method is used to close the data base connection.
     */
    @Override
    public synchronized void close() {
        if (sqliteDB != null)
            sqliteDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "CREATE TABLE opere (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IMG + " TEXT," +
                BENE_CULTURALE + " TEXT," +
                TITOLO + " TEXT," +
                SOGGETTO + " TEXT," +
                LOCALIZZAZIONE + " TEXT," +
                DATAZIONE + " TEXT," +
                AUTORE + " TEXT," +
                MATERIA_TECNICA + " TEXT," +
                MISURE + " TEXT," +
                DEFINIZIONE + " TEXT," +
                DENOMINAZIONE + " TEXT," +
                CLASSIFICAZIONE + " TEXT," +
                REGIONE + " TEXT," +
                PROVINCIA + " TEXT," +
                COMUNE + " TEXT," +
                INDIRIZZO + " TEXT," +
                LAT + " REAL," +
                LON + " REAL)";
        db.execSQL(q);
        Log.d("split", String.format("INPUT: %s", db.getPath()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
