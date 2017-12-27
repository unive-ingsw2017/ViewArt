package it.unive.dais.cevid.datadroid.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<String> info;
    private ArrayList<String> url;
    private LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<String> info, ArrayList<String> url) {
        this.info = info;
        this.url = url;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.infoView = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.infoView.setText(info.get(position));
        if (holder.imageView != null) {
            new ImageDownloaderTask(holder.imageView).execute(url.get(position));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView infoView;
        ImageView imageView;
    }
}