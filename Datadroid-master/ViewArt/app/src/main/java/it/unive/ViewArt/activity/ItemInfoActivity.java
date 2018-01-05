package it.unive.ViewArt.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unive.ViewArt.R;
import it.unive.ViewArt.other.ImageDownloaderTask;
import it.unive.ViewArt.other.Opera;

public class ItemInfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoitem);

        Opera item = getIntent().getParcelableExtra("item");

        ImageView immagine = (ImageView) findViewById(R.id.image);
        new ImageDownloaderTask(immagine).execute("https://cc-media-foxit.fichub.com/image/floptv/276a97a2-3f7e-4ae9-8ff9-3b0d1546ffc9/immagini-avatar-whatsapp-17-maxw-600.jpg", ""+ 2);

        ListView information = (ListView) findViewById(R.id.listViewInfo);

        List<String> buffer = new ArrayList<>();
        buffer.add("Autore :" + item.getAutore());
        buffer.add("Titolo :" + item.getTitolo());
        buffer.add("Soggetto :" + item.getSoggetto());
        buffer.add("Tipologia :" + item.getBene_culturale());
        buffer.add("Datazione :" + item.getDatazione());
        buffer.add("Denominazione :" + item.getDenominazione());
        buffer.add("Classificazione :" + item.getClassificazione());
        buffer.add("Definizione :" + item.getDefinizione());
        buffer.add("Materia tecnica :" + item.getMateria_tecnica());
        buffer.add("Misure :" + item.getMisure());
        buffer.add("Localizzazione :" + item.getLocalizzazione());


        ListAdapter m = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, buffer){

            @NonNull @Override
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
}
