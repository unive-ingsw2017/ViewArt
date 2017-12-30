package it.unive.dais.cevid.datadroid.template.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import it.unive.dais.cevid.datadroid.template.activity.MapsActivity;

import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.AUTORE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.BENE_CULTURALE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.DATA;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.DATAZIONE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.ID;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.SELEZIONATO;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.TIPOLOGIA;

/**
 * Created by filippo on 30/12/17.
 */

public class DbLibrary {

    private void populateAutori(DbManager db) {

        Cursor cr = MapsActivity.db.getDatabaseAccess().query(true, "opere",new String[]{AUTORE} , null,null,null,null, null, null);

        cr.moveToFirst();
        ArrayList<String> buffer = new ArrayList<>();
        for (int i = 1; i <= cr.getCount(); i++){
            ContentValues cv = new ContentValues();
            cv.put(AUTORE,cr.getString(cr.getColumnIndex(AUTORE)));
            cv.put(SELEZIONATO,0);
            db.getDatabaseAccess().insert("autori", null, cv);
            cr.moveToNext();
        }
        cr.close();
    }
    private void populateDate(DbManager db) {

        Cursor cr = MapsActivity.db.getDatabaseAccess().query(true, "opere",new String[]{DATAZIONE} , null,null,null,null, null, null);

        cr.moveToFirst();
        ArrayList<String> buffer = new ArrayList<>();
        for (int i = 1; i <= cr.getCount(); i++){
            ContentValues cv = new ContentValues();
            cv.put(DATA,cr.getString(cr.getColumnIndex(DATAZIONE)));
            cv.put(SELEZIONATO,0);
            db.getDatabaseAccess().insert("date", null, cv);
            cr.moveToNext();
        }
        cr.close();
    }
    private void populateTipologie(DbManager db) {

        Cursor cr = MapsActivity.db.getDatabaseAccess().query(true, "opere",new String[]{BENE_CULTURALE} , null,null,null,null, null, null);

        cr.moveToFirst();
        ArrayList<String> buffer = new ArrayList<>();
        for (int i = 1; i <= cr.getCount(); i++){
            ContentValues cv = new ContentValues();
            cv.put(TIPOLOGIA,cr.getString(cr.getColumnIndex(BENE_CULTURALE)));
            cv.put(SELEZIONATO,0);
            db.getDatabaseAccess().insert("tipologie", null, cv);
            cr.moveToNext();
        }
        cr.close();
    }

    private void updateOpereaux(DbManager db) {
        Cursor cr = db.getDatabaseAccess().query(false, "opere", null , null,null,null,null, null, null);

        cr.moveToFirst();
        ArrayList<String> buffer = new ArrayList<>();
        String autore,datazione,id;
        for (int i = 1; i <= cr.getCount(); i++){
            autore=cr.getString(cr.getColumnIndex(AUTORE));
            datazione=cr.getString(cr.getColumnIndex(DATAZIONE));
            id=cr.getString(cr.getColumnIndex(ID));
            if (autore ==null)
                autore="";
            if (datazione ==null)
                datazione="";



            updateOpere(db,autore,datazione,id);
            cr.moveToNext();
        }
        cr.close();

    }

    private void updateOpere(DbManager db, String old_autore , String old_datazione, String id) {
        ContentValues contentValues = new ContentValues();
        String v1=old_autore.replaceAll("[^a-zA-Z 'èéùòàì]","");
        contentValues.put(AUTORE,v1.replaceAll("I|II|III|IV|V|VI|VII|VIII|IX|X|XI|XII|XIII|XIV|XV|XVI|XVII|XVIII|XIX|XX|XXI| notizie| sec| secc| fino| al| cc| canotizie| ca| prima| metà|  |   |   | post| capost| fine| inizio| dal| ante| notiziec]",""));
        String s0=old_datazione.replaceAll("-","/");
        String s1=s0.replaceAll("[^XVI/]","");
        String s2=s1.replaceAll("  |   |    |    |      |       |        |/ |//|///|/ /","");
        //controlla se sono secoli romani validi
        /*if (s2.matches("(IX|IV|V?I{0,3})|(IX|IV|V?I{0,3})/(IX|IV|V?I{0,3})")==true)
            contentValues.put(DATAZIONE,s2 + " Secolo");
        else
            contentValues.put(DATAZIONE,"Data assente o non valida");*/
        contentValues.put(DATAZIONE,s2 + " Secolo");

        db.getDatabaseAccess().update( "opere",contentValues ,(ID + " =" + id),null);
    }

}
