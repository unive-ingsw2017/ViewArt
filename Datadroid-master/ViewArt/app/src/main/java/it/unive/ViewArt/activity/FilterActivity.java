package it.unive.ViewArt.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;

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

public class FilterActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private ListView autori;
    private ListView date;
    private ListView tipologie;
    private FloatingActionButton autori_button;
    private FloatingActionButton date_button;
    private FloatingActionButton tipologie_button;
    private CustomAdapterChecked adapterAutori;
    private CustomAdapterChecked adapterTipologie;
    private CustomAdapterChecked adapterDate;


    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_autore:
                    autori.setVisibility(View.VISIBLE);
                    autori_button.setVisibility(View.VISIBLE);

                    date.setVisibility(View.INVISIBLE);
                    date_button.setVisibility(View.INVISIBLE);

                    tipologie.setVisibility(View.INVISIBLE);
                    tipologie_button.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_tipologia:
                    autori.setVisibility(View.INVISIBLE);
                    autori_button.setVisibility(View.INVISIBLE);

                    date.setVisibility(View.INVISIBLE);
                    date_button.setVisibility(View.INVISIBLE);

                    tipologie.setVisibility(View.VISIBLE);
                    tipologie_button.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_data:
                    autori.setVisibility(View.INVISIBLE);
                    autori_button.setVisibility(View.INVISIBLE);

                    date.setVisibility(View.VISIBLE);
                    date_button.setVisibility(View.VISIBLE);

                    tipologie.setVisibility(View.INVISIBLE);
                    tipologie_button.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        autori = findViewById(R.id.autori);
        autori_button = findViewById(R.id.autori_button);
        autori_button.setOnClickListener(this);
        adapterAutori = new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(AUTORI, AUTORE), AUTORI, AUTORE);
        autori.setAdapter(adapterAutori);
        autori.setItemsCanFocus(false);

        date = findViewById(R.id.date);
        date_button = findViewById(R.id.date_button);
        date_button.setOnClickListener(this);
        adapterDate = new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(DATE, DATA), DATE, DATA);
        date.setAdapter(adapterDate);

        tipologie = findViewById(R.id.tipologie);
        tipologie_button = findViewById(R.id.tipologie_button);
        tipologie_button.setOnClickListener(this);
        adapterTipologie = new CustomAdapterChecked(this, R.layout.activity_filter, retriveInformation(TIPOLOGIE, TIPOLOGIA), TIPOLOGIE, TIPOLOGIA);
        tipologie.setAdapter(adapterTipologie);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterTipologie.getFilter().filter(s);
        adapterAutori.getFilter().filter(s);
        adapterDate.getFilter().filter(s);
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FilterActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.autori_button)
            reset(adapterAutori, AUTORI, R.id.autori);
        else if (view.getId() == R.id.date_button)
            reset(adapterDate, DATE, R.id.date);
        else reset(adapterTipologie, TIPOLOGIE, R.id.tipologie);
    }

    private void reset(CustomAdapterChecked adapter, String tabella, int id) {
        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = '0'");
        for (int i = 0; i < adapter.getCount(); i++) {
            SimpleEntry<String, Integer> c = adapter.getItem(i);
            c.setValue(0);
            adapter.aggiorna();
        }
    }
}
