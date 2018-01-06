package it.unive.ViewArt.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import it.unive.ViewArt.R;
import it.unive.ViewArt.other.CustomAdapterChecked;

import static it.unive.ViewArt.database.DatabaseStrings.AUTORE;
import static it.unive.ViewArt.database.DatabaseStrings.AUTORI;
import static it.unive.ViewArt.database.DatabaseStrings.DATA;
import static it.unive.ViewArt.database.DatabaseStrings.DATE;
import static it.unive.ViewArt.database.DatabaseStrings.TIPOLOGIA;
import static it.unive.ViewArt.database.DatabaseStrings.TIPOLOGIE;

public class FilterActivity extends AppCompatActivity {

    private ListView autori;
    private ListView date;
    private ListView tipologie;

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_autore:
                    autori.setVisibility(View.VISIBLE);
                    date.setVisibility(View.INVISIBLE);
                    tipologie.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_tipologia:
                    autori.setVisibility(View.INVISIBLE);
                    date.setVisibility(View.INVISIBLE);
                    tipologie.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_data:
                    autori.setVisibility(View.INVISIBLE);
                    date.setVisibility(View.VISIBLE);
                    tipologie.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        autori = (ListView) findViewById(R.id.autori);
        autori.setAdapter(new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(AUTORI, AUTORE), AUTORI, AUTORE));
        autori.setItemsCanFocus(false);

        date = (ListView) findViewById(R.id.date);
        date.setAdapter(new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(DATE, DATA), DATE, DATA));

        tipologie = (ListView) findViewById(R.id.tipologie);
        tipologie.setAdapter(new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(TIPOLOGIE, TIPOLOGIA), TIPOLOGIE, TIPOLOGIA));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FilterActivity.this, MapsActivity.class);
        intent.putExtra("Filtri", true);
        startActivity(intent);
    }

    ArrayList<SimpleEntry<String, Integer>> retriveInformation(String tabella, String colonna) {
        ArrayList<SimpleEntry<String, Integer>> buffer = new ArrayList<>();
        Cursor cr = MapsActivity.db.getDatabaseAccess().rawQuery("SELECT * FROM " + tabella + " order by selezionato desc", null);

        while (cr.moveToNext())
            buffer.add(new SimpleEntry<>(cr.getString(cr.getColumnIndex(colonna)), cr.getInt(cr.getColumnIndex("selezionato"))));

        cr.close();
        return buffer;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
