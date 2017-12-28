package it.unive.dais.cevid.datadroid.template.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import it.unive.dais.cevid.datadroid.template.other.CustomAdapter;
import it.unive.dais.cevid.datadroid.template.other.Opera;
import it.unive.dais.cevid.datadroid.template.R;


public class DisambiguationActivity extends Activity {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disambiguation);

        ArrayList<Opera> arrayItem = new ArrayList<>();
        arrayItem.addAll(MapsActivity.myItemsArray);
        MapsActivity.myItemsArray.clear();


        listView = (ListView) findViewById(R.id.custom_list);


        listView.setAdapter(new CustomAdapter(this, arrayItem));
        listView.setFastScrollAlwaysVisible(true);
        listView.setFastScrollEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Opera item = (Opera) listView.getItemAtPosition(position);
                Intent intent = new Intent(DisambiguationActivity.this, ItemInfoActivity.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView.destroyDrawingCache();
    }
}


