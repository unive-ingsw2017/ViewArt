package it.unive.ViewArt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.IOException;

import static it.unive.ViewArt.database.DatabaseStrings.AUTORE;
import static it.unive.ViewArt.database.DatabaseStrings.BENE_CULTURALE;
import static it.unive.ViewArt.database.DatabaseStrings.CLASSIFICAZIONE;
import static it.unive.ViewArt.database.DatabaseStrings.COMUNE;
import static it.unive.ViewArt.database.DatabaseStrings.DATAZIONE;
import static it.unive.ViewArt.database.DatabaseStrings.DEFINIZIONE;
import static it.unive.ViewArt.database.DatabaseStrings.DENOMINAZIONE;
import static it.unive.ViewArt.database.DatabaseStrings.ID;
import static it.unive.ViewArt.database.DatabaseStrings.IMG;
import static it.unive.ViewArt.database.DatabaseStrings.INDIRIZZO;
import static it.unive.ViewArt.database.DatabaseStrings.LAT;
import static it.unive.ViewArt.database.DatabaseStrings.LOCALIZZAZIONE;
import static it.unive.ViewArt.database.DatabaseStrings.LON;
import static it.unive.ViewArt.database.DatabaseStrings.MATERIA_TECNICA;
import static it.unive.ViewArt.database.DatabaseStrings.MISURE;
import static it.unive.ViewArt.database.DatabaseStrings.PROVINCIA;
import static it.unive.ViewArt.database.DatabaseStrings.REGIONE;
import static it.unive.ViewArt.database.DatabaseStrings.SOGGETTO;
import static it.unive.ViewArt.database.DatabaseStrings.TITOLO;


public class DbManager {
    private DBhelper dbhelper;

    public DbManager(Context ctx) {
        dbhelper = new DBhelper(ctx);
        try {
            dbhelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String img, String bene_culturale, String titolo, String soggetto, String localizzazione, String datazione, String autore, String materia_tecnica, String misure, String definizione, String denominazione, String classificazione, String regione, String provincia, String comune, String indirizzo, Double lat, Double lon) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(IMG, img);
        cv.put(BENE_CULTURALE, bene_culturale);
        cv.put(TITOLO, titolo);
        cv.put(SOGGETTO, soggetto);
        cv.put(LOCALIZZAZIONE, localizzazione);
        cv.put(DATAZIONE, datazione);
        cv.put(AUTORE, autore);
        cv.put(MATERIA_TECNICA, materia_tecnica);
        cv.put(MISURE, misure);
        cv.put(DEFINIZIONE, definizione);
        cv.put(DENOMINAZIONE, denominazione);
        cv.put(CLASSIFICAZIONE, classificazione);
        cv.put(REGIONE, regione);
        cv.put(PROVINCIA, provincia);
        cv.put(COMUNE, comune);
        cv.put(INDIRIZZO, indirizzo);
        cv.put(LAT, lat);
        cv.put(LON, lon);
        try {
            db.insert("opere", null, cv);
        } catch (SQLiteException sqle) {
            // Gestione delle eccezioni
        }
    }

    public boolean delete(String table, long id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            return db.delete(table, ID + "=?", new String[]{Long.toString(id)}) > 0;
        } catch (SQLiteException sqle) {
            return false;
        }

    }

    public SQLiteDatabase getDatabaseAccess() {
        return dbhelper.getReadableDatabase();
    }

    public Cursor query() {
        Cursor crs = null;
        try {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query("opere", null, null, null, null, null, null, null);
        } catch (SQLiteException sqle) {
            return null;
        }
        return crs;
    }

}

