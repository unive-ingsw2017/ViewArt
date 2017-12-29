package it.unive.dais.cevid.datadroid.template.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import it.unive.dais.cevid.datadroid.template.R;

public class FilterActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_autore:
                    mTextMessage.setText(R.string.autore);
                    return true;
                case R.id.navigation_tipologia:
                    mTextMessage.setText(R.string.autore);
                    return true;
                case R.id.navigation_data:
                    mTextMessage.setText(R.string.autore);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();

        menu.findItem(R.id.navigation_autore).setIcon(R.drawable.autore);
        menu.findItem(R.id.navigation_autore).setTitle("Autore");

        menu.findItem(R.id.navigation_tipologia).setTitle("Tipologia");

        menu.findItem(R.id.navigation_data).setIcon(R.drawable.data);
        menu.findItem(R.id.navigation_data).setTitle("Data");

    }

}
