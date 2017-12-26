package it.unive.dais.cevid.datadroid.lib.util;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Rappresenta un oggetto visualizzabile sulla mappa, con le informazioni minime per il posizionamento e la creazione di un marker.
 */
public interface MapItem extends Serializable {
    /**
     * Ritorna la posizione.
     *
     * @return la posizione in un oggetto di tipo LatLng.
     */
    LatLng getPosition();
    /**
     * Ritorna il titolo, o il nome, dell'item.
     *
     * @return il nome.
     */
    String getTitle();
    /**
     * Ritorna la descrizione.
     *
     * @return la descrizione.
     */
    String getDescription();

    int getId();
}
