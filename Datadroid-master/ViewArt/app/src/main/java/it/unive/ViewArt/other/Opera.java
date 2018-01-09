package it.unive.ViewArt.other;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import it.unive.ViewArt.activity.MapsActivity;


public class Opera implements ClusterItem {
    private int id;
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

    public Opera(double lat, double len, String img, String bene_culturale, String titolo, String soggetto, String localizzazione, String datazione, String autore, String materia_tecnica, String misure, String definizione, String denominazione, String classificazione) {
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
    }

    public Opera(int id, double lat, double len, String titolo, String autore) {
        this.id = id;
        position = new LatLng(lat, len);
        this.titolo = titolo;
        this.autore = autore;
    }


    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return "Titolo: " + format(titolo);
    }

    @Override
    public String getSnippet() {
        return "Autore: " + getAutore();
    }

    public String getImgUrl() {
        if (img == null) {
            Cursor cr = MapsActivity.db.getDatabaseAccess().rawQuery("SELECT img FROM opere WHERE _id='" + id + "'", null);
            cr.moveToFirst();
            img = cr.getString(0);
            cr.close();
        }

        return img;

    }

    public String getBene_culturale() {
        if (bene_culturale == null) {
            Cursor cr = MapsActivity.db.getDatabaseAccess().rawQuery("SELECT bene_culturale FROM opere WHERE _id='" + id + "'", null);
            cr.moveToFirst();
            bene_culturale = cr.getString(0);
            cr.close();
        }
        return format(bene_culturale);
    }

    public void setBene_culturale(String bene_culturale) {
        this.bene_culturale = bene_culturale;
    }

    public String getTitolo() {
        return format(titolo);
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getSoggetto() {
        return format(soggetto);
    }

    public void setSoggetto(String soggetto) {
        this.soggetto = soggetto;
    }

    public String getLocalizzazione() {
        return format(localizzazione);
    }

    public void setLocalizzazione(String localizzazione) {
        this.localizzazione = localizzazione;
    }

    public String getDatazione() {
        return format(datazione);
    }

    public void setDatazione(String datazione) {
        this.datazione = datazione;
    }

    public String getAutore() {
        if (autore.charAt(0) == ' ')
            return format(autore.replaceFirst(" ", ""));
        return format(autore);
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getMateria_tecnica() {
        return format(materia_tecnica);
    }

    public void setMateria_tecnica(String materia_tecnica) {
        this.materia_tecnica = materia_tecnica;
    }

    public String getMisure() {
        return format(misure);
    }

    public void setMisure(String misure) {
        this.misure = misure;
    }

    public String getDefinizione() {
        return format(definizione);
    }

    public void setDefinizione(String definizione) {
        this.definizione = definizione;
    }

    public String getDenominazione() {
        return format(denominazione);
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getClassificazione() {
        return format(classificazione);
    }

    public void setClassificazione(String classificazione) {
        this.classificazione = classificazione;
    }

    public int getId() {
        return id;
    }

    private String format(String s) {
        if (s == null || s.equals(""))
            return "Non presente";
        else
            return s;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean toSet() {
        return localizzazione == null;
    }

}
