package it.unive.ViewArt.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import it.unive.ViewArt.R;
import it.unive.ViewArt.other.CustomAdapter;
import it.unive.ViewArt.other.Opera;

import static it.unive.ViewArt.activity.MapsActivity.opereArray;


public class DisambiguationActivity extends AppCompatActivity {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disambiguation);

        ArrayList<Opera> arrayItem = new ArrayList<>();

        for (Integer i : getIntent().getIntegerArrayListExtra("cluster")) {
            if (opereArray.get(i).getImgUrl() == null) {
                Cursor cr = MapsActivity.db.getDatabaseAccess().rawQuery("SELECT img FROM opere WHERE _id = '" + i + "'", null);
                cr.moveToFirst();
                opereArray.get(i).setImg(cr.getString(0));
                cr.close();
            }
            arrayItem.add(opereArray.get(i));
        }

        listView = (ListView) findViewById(R.id.custom_list);


        listView.setAdapter(new CustomAdapter(this, arrayItem));
        listView.setFastScrollAlwaysVisible(true);
        listView.setFastScrollEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Opera item = (Opera) listView.getItemAtPosition(position);
                Intent intent = new Intent(DisambiguationActivity.this, ItemInfoActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomAdapter c = (CustomAdapter) listView.getAdapter();
        c.terminateAsyncTasks();
        listView.setAdapter(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}


