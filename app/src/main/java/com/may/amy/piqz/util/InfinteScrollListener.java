package com.may.amy.piqz.util;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Scroll Listener for RecyclerView that sends a request to load more content.
 */
public class InfinteScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private LoadInterface loadInterface;
    private Handler h;

    public InfinteScrollListener(LinearLayoutManager layoutManager, LoadInterface loadInterface) {
        this.layoutManager = layoutManager;
        this.loadInterface = loadInterface;
        h = new Handler();

    }

    private int previousTotal = 0, visibleThreshold = 2, firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private boolean loading = true;

    //TODO: Change to GridLayoutManager for Tablets
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if (loading && totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)){
                //End has been reached
                Log.i("InifinteScrollListener", "End reached");
                loadInterface.loadItems();
                loading = true;
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading = false;
                    }
                }, 7 * 1000);
            }
        }
    }
    public interface LoadInterface{
        void loadItems();
    }
}
