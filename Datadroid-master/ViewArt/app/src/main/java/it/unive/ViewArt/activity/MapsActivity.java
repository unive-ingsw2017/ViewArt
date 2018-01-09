/**
 * Questo package contiene le componenti Android riusabili.
 * Si tratta di classi che contengono già funzionalità base e possono essere riusate apportandovi modifiche
 */
package it.unive.ViewArt.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener;
import com.google.maps.android.clustering.ClusterManager.OnClusterItemInfoWindowClickListener;

import java.util.ArrayList;

import it.unive.ViewArt.R;
import it.unive.ViewArt.database.DbManager;
import it.unive.ViewArt.other.GlideApp;
import it.unive.ViewArt.other.Opera;


public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraMoveStartedListener, OnClusterClickListener<Opera>, OnClusterItemInfoWindowClickListener<Opera> {

    protected static final int REQUEST_CHECK_SETTINGS = 500;
    protected static final int PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION = 501;

    private static final String TAG = "MapsActivity";
    /**
     * Creo un database manager per la gestione delle informazioni
     */
    public static DbManager db = null;
    public static SparseArray<Opera> opereArray = new SparseArray<>();
    private static long back_pressed;
    /**
     * Questo oggetto è la mappa di Google Maps. Viene inizializzato asincronamente dal metodo {@code onMapsReady}.
     */
    protected GoogleMap gMap;
    /**
     * Pulsanti in sovraimpressione gestiti da questa app. Da non confondere con i pulsanti che GoogleMaps mette in sovraimpressione e che non
     * fanno parte degli oggetti gestiti manualmente dal codice.
     */
    protected ImageButton button_here, button_car;
    /**
     * API per i servizi di localizzazione.
     */
    protected FusedLocationProviderClient fusedLocationClient;
    /**
     * Posizione corrente. Potrebbe essere null prima di essere calcolata la prima volta.
     */
    @Nullable
    protected LatLng currentPosition = null;
    /**
     * Il marker che viene creato premendo il pulsante button_here (cioè quello dell'app, non quello di Google Maps).
     * E' utile avere un campo d'istanza che tiene il puntatore a questo marker perché così è possibile rimuoverlo se necessario.
     * E' null quando non è stato creato il marker, cioè prima che venga premuto il pulsante HERE la prima volta.
     */
    @Nullable
    protected Marker hereMarker = null;
    private ClusterManager<Opera> mClusterManager;
    private Opera clickedItem;
    private static boolean firstTime = true;

    /**
     * Questo metodo viene invocato quando viene inizializzata questa activity.
     * Si tratta di una sorta di "main" dell'intera activity.
     * Inizializza i campi d'istanza, imposta alcuni listener e svolge gran parte delle operazioni "globali" dell'activity.
     *
     * @param savedInstanceState bundle con lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //apri connessione con database

        if (opereArray.size() == 0) {
            db = new DbManager(this);
            creaArray();
        }
        // inizializza le preferenze
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // trova gli oggetti che rappresentano i bottoni e li salva come campi d'istanza
        button_here = (ImageButton) findViewById(R.id.button_here);
        button_car = (ImageButton) findViewById(R.id.button_car);

        // API per i servizi di localizzazione
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // inizializza la mappa asincronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // quando viene premito il pulsante HERE viene eseguito questo codice
        button_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "here button clicked");
                gpsCheck();
                updateCurrentPosition();
                if (hereMarker != null) hereMarker.remove();
                if (currentPosition != null) {
                    MarkerOptions opts = new MarkerOptions();
                    opts.position(currentPosition);
                    opts.title(getString(R.string.marker_title));
                    opts.snippet(String.format("lat: %glng: %g", currentPosition.latitude, currentPosition.longitude));
                    hereMarker = gMap.addMarker(opts);
                    if (gMap != null)
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, getResources().getInteger(R.integer.zoomFactor_button_here)));
                } else
                    Log.d(TAG, "no current position available");
            }
        });
    }

    /**
     * crea una Lista contenente tutte le opere, serve ad evitare di dovere effettuare più volte delle query alla tabella opere
     */
    private void creaArray() {
        Cursor cr = db.getDatabaseAccess().rawQuery("SELECT _id, lat, lon, titolo, autore FROM opere", null);

        while (cr.moveToNext()) {
            Opera opera = new Opera(
                    cr.getInt(0),
                    Double.parseDouble(cr.getString(1)),
                    Double.parseDouble(cr.getString(2)),
                    cr.getString(3),
                    cr.getString(4));
            opereArray.append(cr.getInt(0), opera);
        }
        cr.close();
    }

    // ciclo di vita della app
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Pulisce la mappa quando l'app viene distrutta.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gMap.clear();
    }

    /**
     * Quando arriva un Intent viene eseguito questo metodo.
     * Può essere esteso e modificato secondo le necessità.
     *
     * @see Activity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // inserire codice qui
                        break;
                    case Activity.RESULT_CANCELED:
                        // o qui
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * Questo metodo viene chiamato quando viene richiesto un permesso.
     * Si tratta di un pattern asincrono che va gestito come qui impostato: per gestire nuovi permessi, qualora dovesse essere necessario,
     * è possibile riprodurre un comportamento simile a quello già implementato.
     *
     * @param requestCode  codice di richiesta.
     * @param permissions  array con i permessi richiesti.
     * @param grantResults array con l'azione da intraprende per ciascun dei permessi richiesti.
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permissions granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                } else {
                    Log.e(TAG, "permissions not granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                    Snackbar.make(this.findViewById(R.id.map), R.string.msg_permissions_not_granted, Snackbar.LENGTH_LONG);
                }
            }
        }
    }

    /**
     * Invocato quando viene creato il menu delle impostazioni.
     *
     * @param menu l'oggetto menu.
     * @return ritornare true per visualizzare il menu.
     * @see Activity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_with_options, menu);
        return true;
    }

    // onConnection callbacks
    //
    //

    /**
     * Invocato quando viene cliccata una voce nel menu delle impostazioni.
     *
     * @param item la voce di menu cliccata.
     * @return ritorna true per continuare a chiamare altre callback; false altrimenti.
     * @see Activity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MENU_SETTINGS:
                startActivity(new Intent(this, FilterActivity.class));
                break;
            case R.id.MENU_INFO:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }
        return false;
    }

    /**
     * Viene chiamata quando i servizi di localizzazione sono attivi.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param bundle il bundle passato da Android.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(Bundle)
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "location service connected");
    }

    /**
     * Viene chiamata quando i servizi di localizzazione sono sospesi.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param i un intero che rappresenta il codice della causa della sospenzione.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "location service connection suspended");
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();
    }

    // maps callbacks
    //
    //

    /**
     * Viene chiamata quando la connessione ai servizi di localizzazione viene persa.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param connectionResult oggetto che rappresenta il risultato della connessione, con le cause della disconnessione ed altre informazioni.
     * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(ConnectionResult)
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "location service connection lost");
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
    }

    /**
     * Chiamare questo metodo per aggiornare la posizione corrente del GPS.
     * Si tratta di un metodo proprietario, che non ridefinisce alcun metodo della superclasse né implementa alcuna interfaccia: un metodo
     * di utilità per aggiornare asincronamente in modo robusto e sicuro la posizione corrente del dispositivo mobile.
     */
    public void updateCurrentPosition() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requiring permission");
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            Log.d(TAG, "permission granted");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            if (loc != null) {
                                currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                                Log.i(TAG, "current position updated");
                            }
                        }
                    });
        }
    }

    /**
     * Viene chiamato quando si clicca sulla mappa.
     * Aggiungere qui codice che si vuole eseguire quando l'utente clicca sulla mappa.
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapClick(LatLng latLng) {
    }

    /**
     * Viene chiamato quando si clicca a lungo sulla mappa (long click).
     * Aggiungere qui codice che si vuole eseguire quando l'utente clicca a lungo sulla mappa.
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    /**
     * Viene chiamato quando si muove la camera.
     * Attenzione: solamente al momento in cui la camera comincia a muoversi, non durante tutta la durata del movimento.
     *
     * @param reason
     */
    @Override
    public void onCameraMoveStarted(int reason) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            gMap.setMyLocationEnabled(true);
        }

        //imposta i listener della google map
        gMap.setOnMapLongClickListener(this);
        gMap.setOnCameraMoveStartedListener(this);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.6, 12.0), 7));
        UiSettings uis = gMap.getUiSettings();
        uis.setZoomGesturesEnabled(true);
        uis.setMyLocationButtonEnabled(true);
        gMap.setOnMyLocationButtonClickListener(
                () -> {
                    gpsCheck();
                    return false;
                });
        uis.setCompassEnabled(true);
        uis.setZoomControlsEnabled(true);
        uis.setMapToolbarEnabled(true);

        mClusterManager = new ClusterManager<>(this, gMap);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<Opera>() {
                    @Override
                    public boolean onClusterItemClick(Opera clusterItem) {
                        clickedItem = clusterItem;
                        return false;
                    }
                });
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        gMap.setOnCameraIdleListener(mClusterManager);
        gMap.setOnMarkerClickListener(mClusterManager);
        gMap.setOnInfoWindowClickListener(mClusterManager);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
        gMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());


        //Decide se creare la visualizzazione da primo avvio o se ci sono dei filtri da applicare
        if (firstTime) {
            filteredAction();
            firstTime = false;
        } else if (getIntent().getBooleanExtra("Filtri", false) && filterNumber() > 0)
            filteredAction();
        else
            defaultAction();
    }

    /**
     * @return restituisce il numero dei filtri attualmente attivi
     */
    private int filterNumber() {
        int cont;
        Cursor cr = db.getDatabaseAccess().rawQuery(
                "select * from autori where autori.selezionato = 1 " +
                        "union " +
                        "select * from date where date.selezionato = 1 " +
                        "union " +
                        "select * from tipologie where tipologie.selezionato = 1", null);
        cont = cr.getCount();
        cr.close();
        return cont;
    }

    // marker stuff
    //
    //

    /**
     * Naviga dalla posizione from alla posizione to chiamando il navigatore di Google.
     *
     * @param from posizione iniziale.
     * @param to   posizione finale.
     */
    protected void navigate(@NonNull LatLng from, @NonNull LatLng to) {
        Intent navigation = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + from.latitude + "," + from.longitude + "&daddr=" + to.latitude + "," + to.longitude + ""));
        navigation.setPackage("com.google.android.apps.maps");
        Log.i(TAG, String.format("starting navigation from %s to %s", from, to));
        startActivity(navigation);
    }


    // defaultAction code

    /**
     * Controlla lo stato del GPS e dei servizi di localizzazione, comportandosi di conseguenza.
     * Viene usata durante l'inizializzazione ed in altri casi speciali.
     */
    protected void gpsCheck() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(MapsActivity.this).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void defaultAction() {

        mClusterManager.clearItems();

        for (int i = 1; i <= opereArray.size(); i++)
            mClusterManager.addItem(opereArray.get(i));

    }

    /* gestione callback cluster*/

    private void filteredAction() {
        Cursor cr = db.getDatabaseAccess().rawQuery(
                "select _id from opere, autori where opere.autore = autori.autore and autori.selezionato = 1 " +
                        "union " +
                        "select _id from opere, date where opere.datazione = date.data and date.selezionato = 1 " +
                        "union " +
                        "select _id from opere, tipologie where opere.bene_culturale = tipologie.tipologia and tipologie.selezionato = 1", null);

        mClusterManager.clearItems();
        if (cr.getCount() == 0)
            for (int i = 1; i <= opereArray.size(); i++)
                mClusterManager.addItem(opereArray.get(i));
        else
            while (cr.moveToNext())
                mClusterManager.addItem(opereArray.get(cr.getInt(0)));

        cr.close();
    }

    @Override
    public boolean onClusterClick(Cluster<Opera> cluster) {
        Intent intent = new Intent(MapsActivity.this, DisambiguationActivity.class);
        ArrayList<Integer> clusterItemIndex = new ArrayList<>();

        for (Opera o : cluster.getItems())
            clusterItemIndex.add(o.getId());

        intent.putExtra("cluster", clusterItemIndex);
        startActivity(intent);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Opera opera) {
        Intent intent = new Intent(MapsActivity.this, ItemInfoActivity.class);
        intent.putExtra("operaId", opera.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(getBaseContext(), "Premere nuovamente per uscire", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {
        private String previousImageUrl;

        private final LayoutInflater mInflater;

        public CustomInfoViewAdapter(LayoutInflater inflater) {
            this.mInflater = inflater;
        }


        @Override
        public View getInfoWindow(Marker marker) {
            View view = mInflater.inflate(R.layout.info_window, null);

            TextView titolo = view.findViewById(R.id.title);
            TextView autore = view.findViewById(R.id.snippet);
            ImageView img = view.findViewById(R.id.pic);

            titolo.setText(clickedItem.getTitle());
            autore.setText(clickedItem.getSnippet());

            if (clickedItem.getImgUrl().equals("")) {
                img.setImageDrawable(img.getContext().getDrawable(R.drawable.no_image));
            } else {
                GlideApp.with(getContext())
                        .load(clickedItem.getImgUrl())
                        .fitCenter()
                        .error(R.drawable.no_image)
                        .encodeQuality(50)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                                img.setImageDrawable(resource);
                                String url = clickedItem.getImgUrl();
                                if (!TextUtils.equals(url, previousImageUrl) && marker.isInfoWindowShown()) {
                                    previousImageUrl = url;
                                    marker.showInfoWindow();
                                }

                            }
                        });
            }
            return view;
        }


        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

    }

    public MapsActivity getContext() {
        return this;
    }


}
