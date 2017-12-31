package it.unive.dais.cevid.datadroid.template.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import it.unive.dais.cevid.datadroid.template.R;


public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params[0] == null || params[0].equals(""))
            Log.e("ImageDownloader", "url vuoto");
        return downloadBitmap(params[0], Integer.parseInt(params[1]));
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = imageViewReference.get();

        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.no_image);
                imageView.setImageDrawable(placeholder);
            }
        }

    }

    private Bitmap downloadBitmap(String url, int scale) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                return BitmapFactory.decodeStream(inputStream, null, options);
            }

        } catch (InterruptedIOException e) {
            Log.e("PROCESSO INTERROTTO", e.toString());
        } catch (IOException e) {
            Log.e("PROBLEMA CON URL", e.toString());
        } catch (OutOfMemoryError e) {
            Log.e("MEMORIA", "memoria finita mentre stavo scaricando le immagini");
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

}