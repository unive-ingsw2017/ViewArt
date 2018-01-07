package it.unive.ViewArt.other;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.unive.ViewArt.R;

public class DisambiguationAdapter extends RecyclerView.Adapter<DisambiguationAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private ArrayList<Opera> arrayOpere;
    private ArrayList<AsyncTask> tasks = new ArrayList<>();

    public DisambiguationAdapter(ArrayList<Opera> arrayOpere, OnItemClickListener listener) {
        this.arrayOpere = arrayOpere;
        this.listener = listener;
    }

    @Override
    public DisambiguationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View opereView = inflater.inflate(R.layout.row, parent, false);

        return new ViewHolder(opereView);
    }

    @Override
    public void onBindViewHolder(DisambiguationAdapter.ViewHolder viewHolder, int position) {
        Opera opera = arrayOpere.get(position);

        TextView titleView = viewHolder.titleView;
        TextView infoView1 = viewHolder.infoView1;
        TextView infoView2 = viewHolder.infoView2;
        ImageView immagine = viewHolder.imageView;

        titleView.setText(String.format(opera.getTitolo() + "  (%d)", position));
        infoView1.setText(opera.getAutore());
        infoView2.setText(opera.getBene_culturale());

        tasks.add(new ImageDownloaderTask(immagine).execute(arrayOpere.get(position).getImgUrl(), "" + 8)); //compressione 8x

        viewHolder.bind(arrayOpere.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return arrayOpere.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public void terminateAsyncTasks() {
        for (AsyncTask task : tasks)
            task.cancel(true);
    }

    public interface OnItemClickListener {
        void onItemClick(Opera item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView infoView1;
        private TextView infoView2;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            infoView1 = itemView.findViewById(R.id.info1);
            infoView2 = itemView.findViewById(R.id.info2);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind(final Opera item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}