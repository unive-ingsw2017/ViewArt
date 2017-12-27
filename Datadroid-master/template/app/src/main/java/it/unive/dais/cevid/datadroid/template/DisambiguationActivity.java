package it.unive.dais.cevid.datadroid.template;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DisambiguationActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disambiguation);

        // set the list adapter
        String[] entities = {"Users", "Books", "Orders", "States"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entities);
        setListAdapter(adapter);
    }

}
