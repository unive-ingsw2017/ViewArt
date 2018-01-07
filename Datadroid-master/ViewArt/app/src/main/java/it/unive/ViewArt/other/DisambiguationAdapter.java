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

        // Inflate the custom layout
        View opereView = inflater.inflate(R.layout.row, parent, false);

        // Return a new holder instance
        return new ViewHolder(opereView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(DisambiguationAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Opera opera = arrayOpere.get(position);

        // Set item views based on your views and data model
        TextView titleView = viewHolder.titleView;
        TextView infoView1 = viewHolder.infoView1;
        TextView infoView2 = viewHolder.infoView2;
        ImageView immagine = viewHolder.imageView;

        titleView.setText(opera.getTitolo());
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
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView titleView;
        private TextView infoView1;
        private TextView infoView2;
        private ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
            infoView1 = (TextView) itemView.findViewById(R.id.info1);
            infoView2 = (TextView) itemView.findViewById(R.id.info2);
            imageView = (ImageView) itemView.findViewById(R.id.image);
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