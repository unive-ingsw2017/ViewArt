package it.unive.ViewArt.activity;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.util.ArrayList;
import java.util.List;

import it.unive.ViewArt.R;
import it.unive.ViewArt.other.GlideApp;
import it.unive.ViewArt.other.Opera;

import static it.unive.ViewArt.activity.MapsActivity.opereArray;

public class ItemInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoitem);

        Opera opera = opereArray.get(getIntent().getIntExtra("operaId", -1));
        int id = opera.getId();
        if (opera.toSet()) {
            Cursor cr = MapsActivity.db.getDatabaseAccess().rawQuery("SELECT bene_culturale, soggetto, localizzazione, datazione, materia_tecnica, misure, definizione, denominazione, classificazione FROM opere WHERE _id = '" + id + "'", null);
            cr.moveToFirst();
            opereArray.get(id).setBene_culturale(cr.getString(0));
            opereArray.get(id).setSoggetto(cr.getString(1));
            opereArray.get(id).setLocalizzazione(cr.getString(2));
            opereArray.get(id).setDatazione(cr.getString(3));
            opereArray.get(id).setMateria_tecnica(cr.getString(4));
            opereArray.get(id).setMisure(cr.getString(5));
            opereArray.get(id).setDefinizione(cr.getString(6));
            opereArray.get(id).setDenominazione(cr.getString(7));
            opereArray.get(id).setClassificazione(cr.getString(8));
            cr.close();
            opera = opereArray.get(id);
        }

        ImageView immagine = findViewById(R.id.image);
        if (opera.getImgUrl().equals(""))
            immagine.setImageDrawable(this.getDrawable(R.drawable.no_image));
        else {
            GlideApp.with(this)
                    .load(opera.getImgUrl())
                    .placeholder(R.drawable.loader)
                    .thumbnail(GlideApp.with(this).load(R.drawable.loader))
                    .encodeQuality(100)
                    .fitCenter()
                    .into(immagine);
        }

        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setBackgroundColor(this.getResources().getColor(R.color.trans50));
        imagePopup.setFullScreen(true);
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);
        imagePopup.initiatePopupWithPicasso(opera.getImgUrl());

        immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup.viewPopup();

            }
        });

        ListView information = findViewById(R.id.listViewInfo);

        List<String> buffer = new ArrayList<>();
        buffer.add("Autore :" + opera.getAutore());
        buffer.add("Titolo :" + opera.getTitolo());
        buffer.add("Soggetto :" + opera.getSoggetto());
        buffer.add("Tipologia :" + opera.getBene_culturale());
        buffer.add("Datazione :" + opera.getDatazione());
        buffer.add("Denominazione :" + opera.getDenominazione());
        buffer.add("Classificazione :" + opera.getClassificazione());
        buffer.add("Definizione :" + opera.getDefinizione());
        buffer.add("Materia tecnica :" + opera.getMateria_tecnica());
        buffer.add("Misure :" + opera.getMisure());
        buffer.add("Localizzazione :" + opera.getLocalizzazione());


        ListAdapter m = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, buffer) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setTypeface(null, Typeface.BOLD);

                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(buffer.get(position).substring(0, buffer.get(position).indexOf(":")));

                text2.setText(buffer.get(position).substring(buffer.get(position).indexOf(":") + 1, buffer.get(position).length()));

                return view;
            }
        };

        information.setAdapter(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


}
