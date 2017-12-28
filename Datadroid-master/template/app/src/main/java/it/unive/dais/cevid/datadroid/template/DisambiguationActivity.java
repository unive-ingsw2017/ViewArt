package it.unive.dais.cevid.datadroid.template;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class DisambiguationActivity extends Activity {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disambiguation);

        ArrayList<MyItem> arrayItem = new ArrayList<>();
        arrayItem.addAll(MapsActivity.MyItemsArray);
        MapsActivity.MyItemsArray.clear();

        //ArrayList<MyItem> arrayItem = getIntent().getParcelableArrayListExtra("items");

        listView = (ListView) findViewById(R.id.custom_list);

        listView.setAdapter(new CustomAdapter(this, arrayItem));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                MyItem item = (MyItem) listView.getItemAtPosition(position);
                Toast.makeText(DisambiguationActivity.this, "Selected :" + " " + item.getImgUrl(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView.destroyDrawingCache();
    }
}


