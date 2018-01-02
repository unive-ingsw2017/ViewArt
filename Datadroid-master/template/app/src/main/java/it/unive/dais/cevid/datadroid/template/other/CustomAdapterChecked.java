package it.unive.dais.cevid.datadroid.template.other;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.template.activity.MapsActivity;

public class CustomAdapterChecked extends ArrayAdapter<SimpleEntry<String, Integer>> {

    private final LayoutInflater layoutInflater;
    private ArrayList<SimpleEntry<String, Integer>> info;
    private String tabella;
    private String colonna;

    public CustomAdapterChecked(Context context, int textViewResourceId, List<SimpleEntry<String, Integer>> info, String tabella, String colonna) {
        super(context, textViewResourceId, info);
        this.info = new ArrayList<>();
        this.info.addAll(info);
        this.tabella = tabella;
        this.colonna = colonna;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.checked_row, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.check_text);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.my_checkbox);

            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v.findViewById(R.id.my_checkbox);
                    Map.Entry<String, Integer> entry = (SimpleEntry<String,Integer>) cb.getTag();
                    String txt = (String) ((TextView) v.findViewById(R.id.check_text)).getText();

                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = 0 WHERE " + colonna + " = '" + txt + "'", new String[]{});
                        entry.setValue(0);
                    } else {
                        cb.setChecked(true);
                        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = 1 WHERE " + colonna + " = '" + txt + "'", new String[]{});
                        entry.setValue(1);
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleEntry<String, Integer> tuple = info.get(position);
        holder.text.setText(tuple.getKey());
        Log.e("value","" + tuple.getValue() );
        holder.checkBox.setChecked(tuple.getValue() != 0);

        holder.checkBox.setTag(tuple);

        return convertView;
    }

    private class ViewHolder {
        private TextView text;
        private CheckBox checkBox;
    }

}