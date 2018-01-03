package it.unive.dais.cevid.datadroid.template.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.AbstractMap;
import java.util.ArrayList;

import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.template.other.CustomAdapterChecked;

import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.AUTORE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.AUTORI;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.DATA;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.DATE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.TIPOLOGIA;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.TIPOLOGIE;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView autori;
    private ListView date;
    private ListView tipologie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        autori = (ListView) findViewById(R.id.autori);
        autori.setAdapter(new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(AUTORI, AUTORE), AUTORI, AUTORE));
        autori.setItemsCanFocus(false);

        date = (ListView) findViewById(R.id.date);
        date.setAdapter(new CustomAdapterChecked(this, R.layout.activity_filter,retriveInformation(DATE, DATA), DATE, DATA));

        tipologie = (ListView) findViewById(R.id.tipologie);
        tipologie.setAdapter(new CustomAdapterChecked(this, R.layout.activity_filter,retriveInformation(TIPOLOGIE, TIPOLOGIA), TIPOLOGIE, TIPOLOGIA));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();

        menu.findItem(R.id.navigation_autore).setIcon(R.drawable.autore);
        menu.findItem(R.id.navigation_autore).setTitle("Autore");

        menu.findItem(R.id.navigation_tipologia).setTitle("Tipologia");

        menu.findItem(R.id.navigation_data).setIcon(R.drawable.data);
        menu.findItem(R.id.navigation_data).setTitle("Data");

        autori.setOnItemClickListener(this);

    }

    ArrayList<AbstractMap.SimpleEntry<String, Integer>> retriveInformation(String tabella, String colonna) {
        Cursor cr = MapsActivity.db.getDatabaseAccess().rawQuery("SELECT * FROM " + tabella + " order by selezionato desc", null);

        cr.moveToFirst();
        ArrayList<AbstractMap.SimpleEntry<String, Integer>> buffer = new ArrayList<>();
        for (int i = 1; i <= cr.getCount(); i++) {
            buffer.add(new AbstractMap.SimpleEntry<>(cr.getString(cr.getColumnIndex(colonna)), cr.getInt(cr.getColumnIndex("selezionato"))));
            cr.moveToNext();
        }
        cr.close();
        return buffer;
    }


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
    public void onBackPressed() {
        Intent intent = new Intent(FilterActivity.this, MapsActivity.class);
        intent.putExtra("Filtri", true);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
}
