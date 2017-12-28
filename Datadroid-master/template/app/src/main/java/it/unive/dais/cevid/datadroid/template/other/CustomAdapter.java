package it.unive.dais.cevid.datadroid.template.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.unive.dais.cevid.datadroid.template.R;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Opere> arrayItem;
    private LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<Opere> arrayItem) {
        this.arrayItem = arrayItem;
        layoutInflater = LayoutInflater.from(context);
        //MapsActivity.myItemsArray.clear();
    }

    @Override
    public int getCount() {
        return arrayItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        AsyncTask<String, Void, Bitmap> execute = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.infoView1 = (TextView) convertView.findViewById(R.id.info1);
            holder.infoView2 = (TextView) convertView.findViewById(R.id.info2);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.titleView.setText(arrayItem.get(position).getTitolo());
        holder.infoView1.setText(arrayItem.get(position).getSnippet().replace(",", ""));
        holder.infoView2.setText(arrayItem.get(position).getBene_culturale());

        if (holder.imageView != null) {
            Log.e("dadss", arrayItem.get(position).getImgUrl());
            execute = new ImageDownloaderTask(holder.imageView).execute("https://cc-media-foxit.fichub.com/image/floptv/276a97a2-3f7e-4ae9-8ff9-3b0d1546ffc9/immagini-avatar-whatsapp-17-maxw-600.jpg");
        }

        if(holder.imageView.getTag() != null)
            execute.cancel(true);

        return convertView;
    }

    private static class ViewHolder {
        private TextView titleView;
        private TextView infoView1;
        private TextView infoView2;
        private ImageView imageView;
    }
}