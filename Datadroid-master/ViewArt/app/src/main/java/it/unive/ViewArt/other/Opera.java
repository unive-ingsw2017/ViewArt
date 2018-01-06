package it.unive.ViewArt.other;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class Opera implements ClusterItem, Parcelable {
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

    public Opera(int id, double lat, double len, String autore, String titolo) {
        this.id = id;
        position = new LatLng(lat, len);
        this.titolo = titolo;
        this.autore = autore;
    }


    protected Opera(Parcel in) {
        position = in.readParcelable(LatLng.class.getClassLoader());
        img = in.readString();
        bene_culturale = in.readString();
        titolo = in.readString();
        soggetto = in.readString();
        localizzazione = in.readString();
        datazione = in.readString();
        autore = in.readString();
        materia_tecnica = in.readString();
        misure = in.readString();
        definizione = in.readString();
        denominazione = in.readString();
        classificazione = in.readString();
    }

    public static final Creator<Opera> CREATOR = new Creator<Opera>() {
        @Override
        public Opera createFromParcel(Parcel in) {
            return new Opera(in);
        }

        @Override
        public Opera[] newArray(int size) {
            return new Opera[size];
        }
    };

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
        return "Autore: " + format(autore);
    }

    public String getImgUrl() {
        return img;
    }

    public String getBene_culturale() {
        return format(bene_culturale);
    }

    public String getTitolo() {
        return format(titolo);
    }

    public String getSoggetto() {
        return format(soggetto);
    }

    public String getLocalizzazione() {
        return format(localizzazione);
    }

    public String getDatazione() {
        return format(datazione);
    }

    public String getAutore() {
        return format(autore);
    }

    public String getMateria_tecnica() {
        return format(materia_tecnica);
    }

    public String getMisure() {
        return format(misure);
    }

    public String getDefinizione() {
        return format(definizione);
    }

    public String getDenominazione() {
        return format(denominazione);
    }

    public String getClassificazione() {
        return format(classificazione);
    }

    public int getId() { return id; }

    private String format(String s) {
        if (s == null || s.equals(""))
            return "Non presente";
        else
            return s;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setBene_culturale(String bene_culturale) {
        this.bene_culturale = bene_culturale;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setSoggetto(String soggetto) {
        this.soggetto = soggetto;
    }

    public void setLocalizzazione(String localizzazione) {
        this.localizzazione = localizzazione;
    }

    public void setDatazione(String datazione) {
        this.datazione = datazione;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public void setMateria_tecnica(String materia_tecnica) {
        this.materia_tecnica = materia_tecnica;
    }

    public void setMisure(String misure) {
        this.misure = misure;
    }

    public void setDefinizione(String definizione) {
        this.definizione = definizione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public void setClassificazione(String classificazione) {
        this.classificazione = classificazione;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(position, flags);
        dest.writeString(img);
        dest.writeString(bene_culturale);
        dest.writeString(titolo);
        dest.writeString(soggetto);
        dest.writeString(localizzazione);
        dest.writeString(datazione);
        dest.writeString(autore);
        dest.writeString(materia_tecnica);
        dest.writeString(misure);
        dest.writeString(definizione);
        dest.writeString(denominazione);
        dest.writeString(classificazione);
    }
}
