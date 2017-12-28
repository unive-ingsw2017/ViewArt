package it.unive.dais.cevid.datadroid.template;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;


public class MyItem implements ClusterItem, Serializable, Parcelable {
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

    public MyItem(double lat, double len, String img, String bene_culturale, String titolo, String soggetto, String localizzazione, String datazione, String autore, String materia_tecnica, String misure, String definizione, String denominazione, String classificazione, String regione, String provincia, String comune, String indirizzo) {
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

    protected MyItem(Parcel in) {
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
        regione = in.readString();
        provincia = in.readString();
        comune = in.readString();
        indirizzo = in.readString();
    }


    public static final Creator<MyItem> CREATOR = new Creator<MyItem>() {
        @Override
        public MyItem createFromParcel(Parcel in) {
            return new MyItem(in);
        }

        @Override
        public MyItem[] newArray(int size) {
            return new MyItem[size];
        }
    };

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
        dest.writeString(regione);
        dest.writeString(provincia);
        dest.writeString(comune);
        dest.writeString(indirizzo);
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
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
