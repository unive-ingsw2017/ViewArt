package it.unive.ViewArt.other;


import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int currentPage = 1;

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        super.onScrolled(view, dx, dy);

        if (!view.canScrollVertically(1)) {
            currentPage++;
            onLoadMore(currentPage, view);
        }
    }

    public abstract void onLoadMore(int page, RecyclerView view);
}