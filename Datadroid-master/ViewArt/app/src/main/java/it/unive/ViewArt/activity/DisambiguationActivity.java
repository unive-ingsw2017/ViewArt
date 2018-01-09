package it.unive.ViewArt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import it.unive.ViewArt.R;
import it.unive.ViewArt.other.DisambiguationAdapter;
import it.unive.ViewArt.other.EndlessRecyclerViewScrollListener;
import it.unive.ViewArt.other.Opera;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

import static it.unive.ViewArt.activity.MapsActivity.opereArray;


public class DisambiguationActivity extends AppCompatActivity {

    RecyclerView rvOpere;
    DisambiguationAdapter adapter;
    ArrayList<Integer> itemIdArray;
    int index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disambiguation);

        itemIdArray = getIntent().getIntegerArrayListExtra("cluster");
        ArrayList<Opera> arrayItem = moreOpere();

        rvOpere = findViewById(R.id.custom_list);

        adapter = new DisambiguationAdapter(this, arrayItem, new DisambiguationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Opera item) {
                Intent intent = new Intent(DisambiguationActivity.this, ItemInfoActivity.class);
                intent.putExtra("operaId", item.getId());
                startActivity(intent);
            }
        });


        rvOpere.setLayoutManager(new LinearLayoutManager(this));

        //animations
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(400);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
        slideAdapter.setDuration(400);
        rvOpere.setAdapter(slideAdapter);

        //costumizations
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOpere.addItemDecoration(itemDecoration);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener() {
            @Override
            public void onLoadMore(int page, RecyclerView view) {
                List<Opera> moreOpere = moreOpere();
                int curSize = adapter.getItemCount();
                arrayItem.addAll(moreOpere);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, arrayItem.size() - 1);
                    }
                });
            }
        };
        // Adds the scroll listener to RecyclerView
        rvOpere.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private ArrayList<Opera> moreOpere() {

        ArrayList<Opera> buffer = new ArrayList<>();
        int i = 0;
        while (i < 50 && index < itemIdArray.size()) {
            buffer.add(opereArray.get(itemIdArray.get(index)));
            index++;
            i++;
        }
        return buffer;
    }
}


