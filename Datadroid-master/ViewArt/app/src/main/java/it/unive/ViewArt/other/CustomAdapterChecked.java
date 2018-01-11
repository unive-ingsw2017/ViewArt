package it.unive.ViewArt.other;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map;

import it.unive.ViewArt.R;
import it.unive.ViewArt.activity.MapsActivity;

public class CustomAdapterChecked extends ArrayAdapter<SimpleEntry<String, Integer>> {

    private final LayoutInflater layoutInflater;
    private ArrayList<SimpleEntry<String, Integer>> info;
    private ArrayList<SimpleEntry<String, Integer>> infoFiltered;

    private String tabella;
    private String colonna;
    private ItemFilter itemFilter;

    public CustomAdapterChecked(Context context, int textViewResourceId, ArrayList<SimpleEntry<String, Integer>> info, String tabella, String colonna) {
        super(context, textViewResourceId, info);
        this.info = info;
        this.infoFiltered = info;
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
            holder.text = convertView.findViewById(R.id.check_text);
            holder.checkBox = convertView.findViewById(R.id.my_checkbox);

            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = v.findViewById(R.id.my_checkbox);
                    Map.Entry<String, Integer> entry = (SimpleEntry<String, Integer>) cb.getTag();

                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = 0 WHERE " + colonna + " = '" + entry.getKey().replaceAll("'", "''") + "'", new String[]{});
                        entry.setValue(0);
                    } else {
                        cb.setChecked(true);
                        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = 1 WHERE " + colonna + " = '" + entry.getKey().replaceAll("'", "''") + "'", new String[]{});
                        entry.setValue(1);
                    }
                }
            });

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Map.Entry<String, Integer> entry = (SimpleEntry<String, Integer>) v.getTag();

                    if (!cb.isChecked()) {
                        cb.setChecked(false);
                        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = 0 WHERE " + colonna + " = '" + entry.getKey().replaceAll("'", "''") + "'", new String[]{});
                        entry.setValue(0);
                    } else {
                        cb.setChecked(true);
                        MapsActivity.db.getDatabaseAccess().execSQL("UPDATE " + tabella + " SET selezionato = 1 WHERE " + colonna + " = '" + entry.getKey().replaceAll("'", "''") + "'", new String[]{});
                        entry.setValue(1);
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (infoFiltered.size() > 0) {
            SimpleEntry<String, Integer> tuple = getItem(position);
            holder.text.setText(tuple.getKey());
            holder.checkBox.setChecked(tuple.getValue() != 0);
            holder.checkBox.setTag(tuple);
        }
        return convertView;
    }

    @Nullable
    @Override
    public SimpleEntry<String, Integer> getItem(int position) {
        return infoFiltered.get(position);
    }

    @Override
    public int getCount() {
        return infoFiltered.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }

        return itemFilter;
    }


    /* override dei metodi per la live search */

    private class ViewHolder {
        private TextView text;
        private CheckBox checkBox;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<SimpleEntry<String, Integer>> tempList = new ArrayList<>();

                // search content in friend list
                for (SimpleEntry<String, Integer> item : info) {
                    if (item.getKey().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(item);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = info.size();
                filterResults.values = info;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            infoFiltered = (ArrayList<SimpleEntry<String, Integer>>) results.values;
            notifyDataSetChanged();
        }
    }

    public void aggiorna(){
        notifyDataSetChanged();
    }

}