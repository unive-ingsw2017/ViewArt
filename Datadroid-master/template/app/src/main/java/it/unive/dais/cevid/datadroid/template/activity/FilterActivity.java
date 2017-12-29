package it.unive.dais.cevid.datadroid.template.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.unive.dais.cevid.datadroid.template.R;

import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.AUTORE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.BENE_CULTURALE;
import static it.unive.dais.cevid.datadroid.template.database.DatabaseStrings.DATAZIONE;

public class FilterActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView autori;
    private ListView date;
    private ListView tipologie;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
        autori.setAdapter(retriveInformation(AUTORE, null));

        date = (ListView) findViewById(R.id.date);
        date.setAdapter(retriveInformation(DATAZIONE, null));

        tipologie = (ListView) findViewById(R.id.tipologie);
        tipologie.setAdapter(retriveInformation(BENE_CULTURALE, null));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();

        menu.findItem(R.id.navigation_autore).setIcon(R.drawable.autore);
        menu.findItem(R.id.navigation_autore).setTitle("Autore");

        menu.findItem(R.id.navigation_tipologia).setTitle("Tipologia");

        menu.findItem(R.id.navigation_data).setIcon(R.drawable.data);
        menu.findItem(R.id.navigation_data).setTitle("Data");

    }

    ListAdapter retriveInformation(String colonna, String filtro){
        Cursor cr = MapsActivity.db.getDatabaseAccess().query(true, "opere", new String[] {colonna}, filtro,null,null,null, colonna, null);

        cr.moveToFirst();
        ArrayList<String> buffer = new ArrayList<>();
        for (int i = 1; i <= cr.getCount(); i++){
            buffer.add(cr.getString(cr.getColumnIndex(colonna)));
            cr.moveToNext();
        }
        cr.close();
        buffer.remove(0);
        return new ArrayAdapter<> (this, R.layout.checked_row, buffer);
    }

}
