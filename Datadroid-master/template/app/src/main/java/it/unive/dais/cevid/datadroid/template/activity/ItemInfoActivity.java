package it.unive.dais.cevid.datadroid.template.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import it.unive.dais.cevid.datadroid.template.other.ImageDownloaderTask;
import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.template.other.Opera;

/**
 * Created by filippo on 28/12/17.
 */

public class ItemInfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoitem);
        Opera item = getIntent().getParcelableExtra("item");
        ImageView immagine = (ImageView) findViewById(R.id.image);
        new ImageDownloaderTask(immagine).execute("https://cc-media-foxit.fichub.com/image/floptv/276a97a2-3f7e-4ae9-8ff9-3b0d1546ffc9/immagini-avatar-whatsapp-17-maxw-600.jpg");

        TextView titolo = (TextView) findViewById(R.id.titolo);
        titolo.setText(item.getTitolo());

        TextView autore = (TextView) findViewById(R.id.autore);
        autore.setText(item.getAutore());

        TextView bene_culturale = (TextView) findViewById(R.id.bene_culturale);
        bene_culturale.setText(item.getBene_culturale());

        TextView soggetto = (TextView) findViewById(R.id.soggetto);
        soggetto.setText(item.getSoggetto());

        TextView localizzazione = (TextView) findViewById(R.id.localizzazione);
        localizzazione.setText(item.getLocalizzazione());

        TextView datazione = (TextView) findViewById(R.id.datazione);
        datazione.setText(item.getDatazione());

        TextView materia_tecnica = (TextView) findViewById(R.id.materia_tecnica);
        materia_tecnica.setText(item.getMateria_tecnica());

        TextView misure = (TextView) findViewById(R.id.misure);
        misure.setText(item.getMisure());

        TextView definizione = (TextView) findViewById(R.id.definizione);
        definizione.setText(item.getDefinizione());

        TextView denominazione = (TextView) findViewById(R.id.denominazione);
        denominazione.setText(item.getDenominazione());

        TextView classificazione = (TextView) findViewById(R.id.classificazione);
        classificazione.setText(item.getClassificazione());


    }
}
