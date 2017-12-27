package it.unive.dais.cevid.datadroid.template;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class DisambiguationActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disambiguation);

        ArrayList<MyItem> a = getIntent().getParcelableArrayListExtra("items");

        ArrayList<String> info = new ArrayList<>();
        ArrayList<String> url = new ArrayList<>();

        for (MyItem i : a) {
            info.add(i.getTitle() + " " + i.getSnippet());
            url.add(i.getUrl());
        }

        final ListView listView = (ListView) findViewById(R.id.custom_list);
        listView.setAdapter(new CustomAdapter(this, info, url));


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                ListItem newsData = (ListItem) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
            }
        });*/

    }


}


