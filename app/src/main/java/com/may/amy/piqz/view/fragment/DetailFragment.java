package com.may.amy.piqz.view.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.DetailFragmentBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.util.KaC;
import com.may.amy.piqz.view.activity.MainActivity;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnTouchListener {
    private static final String TAG_EVENT = "OnTouchEvent";
    private PostItemViewModel mViewModel;
    private DetailFragmentBinding binding;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // Zoom states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    private PointF startPoint = new PointF();
    private PointF midPoint = new PointF();
    private float oldDist = 1f;
    private String savedItemClicked;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mViewModel == null) {
            ((MainActivity) getActivity()).removeFragment(this);
        }
        binding.setViewModel(mViewModel);
        binding.toolbarDetail.setNavigationIcon(R.drawable.vd_arrow_back);
        binding.toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).removeFragment(DetailFragment.this);
            }
        });

        if (mViewModel.getPost().get().getPostType() != KaC.TYPE_IMAGE) {
            binding.wbSinglePost.loadUrl(mViewModel.getPost().get().getUrl());
        } else {
            binding.ivImageLarge.setOnTouchListener(this);
        }
    }

    public void setNewsItem(PostItemViewModel viewModel) {
        this.mViewModel = viewModel;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        dumpEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                Log.d(TAG_EVENT, "Mode: DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG_EVENT, "oldDist = " + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(midPoint, event);
                    mode = ZOOM;
                    Log.d(TAG_EVENT, "Mode: ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG_EVENT, "Mode: NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG_EVENT, "newDist = " + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN",
                "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT
            );
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append(event.getX(i));
            sb.append(",").append(event.getY(i));
            if (i + 1 < event.getPointerCount()) {
                sb.append(";");
            }
        }
        sb.append("]");
        Log.d(TAG_EVENT, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
