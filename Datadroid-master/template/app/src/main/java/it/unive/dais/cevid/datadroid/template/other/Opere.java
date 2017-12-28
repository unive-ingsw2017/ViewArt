package it.unive.dais.cevid.datadroid.template.other;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;


public class Opere implements ClusterItem {
    private LatLng position;
    private String img;
    private String bene_culturale;
    private String titolo;
    private String soggetto;
    private String localizzazione;
    private String datazione;
    private String autore;
    private String materia_tecnica;
    private String misure;
    private String definizione;
    private String denominazione;
    private String classificazione;
    private String regione;
    private String provincia;
    private String comune;
    private String indirizzo;

    public Opere(double lat, double len, String img, String bene_culturale, String titolo, String soggetto, String localizzazione, String datazione, String autore, String materia_tecnica, String misure, String definizione, String denominazione, String classificazione, String regione, String provincia, String comune, String indirizzo) {
        position = new LatLng(lat, len);
        this.img = img;
        this.bene_culturale = bene_culturale;
        this.titolo = titolo;
        this.soggetto = soggetto;
        this.localizzazione = localizzazione;
        this.datazione = datazione;
        this.autore = autore;
        this.materia_tecnica = materia_tecnica;
        this.misure = misure;
        this.definizione = definizione;
        this.denominazione = denominazione;
        this.classificazione = classificazione;
        this.regione = regione;
        this.provincia = provincia;
        this.comune = comune;
        this.indirizzo = indirizzo;
    }


    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        if (titolo == null || titolo.equals(""))
            return "no title";
        else
            return titolo;
    }

    @Override
    public String getSnippet() {
        return autore;
    }

    public String getImgUrl() {
        return img;
    }

    public String getBene_culturale() {
        return bene_culturale;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getSoggetto() {
        return soggetto;
    }

    public String getLocalizzazione() {
        return localizzazione;
    }

    public String getDatazione() {
        return datazione;
    }

    public String getAutore() {
        return autore;
    }

    public String getMateria_tecnica() {
        return materia_tecnica;
    }

    public String getMisure() {
        return misure;
    }

    public String getDefinizione() {
        return definizione;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public String getClassificazione() {
        return classificazione;
    }

    public String getRegione() {
        return regione;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getComune() {
        return comune;
    }

    public String getIndirizzo() {
        return indirizzo;
    }
}
