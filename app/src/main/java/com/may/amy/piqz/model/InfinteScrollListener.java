package com.may.amy.piqz.model;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by kuhnertj on 26.04.2016.
 */
public class InfinteScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private LoadInterface loadInterface;

    public InfinteScrollListener(LinearLayoutManager layoutManager, LoadInterface loadInterface) {
        this.layoutManager = layoutManager;
        this.loadInterface = loadInterface;

    }

    private int previousTotal = 0, visibleThreshold = 2, firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private boolean loading = true;


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
            }
        }
    }
    public interface LoadInterface{
        void loadItems();
    }
}
