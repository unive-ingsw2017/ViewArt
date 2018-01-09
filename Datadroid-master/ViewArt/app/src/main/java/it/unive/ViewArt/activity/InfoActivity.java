package it.unive.ViewArt.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import it.unive.ViewArt.R;


public class InfoActivity extends AppCompatActivity {


    public static String credits() {
        return "--- View Art ---\n " +
                "2018\n" +
                "Corso di Laure in Informatica\n" +
                "Università Ca' Foscari di Venezia\n\n" +
                "Alessio Del Conte\n" +
                "Filippo Maganza";
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        TextView tv_1 = findViewById(R.id.textView_1);
        tv_1.setText(credits());
    }

}
