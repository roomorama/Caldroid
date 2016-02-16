package com.roomorama.caldroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.caldroid.R;

/**
 * DateGridFragment contains only 1 gridview with 7 columns to display all the
 * dates within a month.
 * <p/>
 * Client must supply gridAdapter and onItemClickListener before the fragment is
 * attached to avoid complex crash due to fragment life cycles.
 *
 * @author thomasdao
 */
public class DateGridFragment extends Fragment {
    private GridView gridView;
    private CaldroidGridAdapter gridAdapter;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private int gridViewRes = 0;
    private int themeResource = 0;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public CaldroidGridAdapter getGridAdapter() {
        return gridAdapter;
    }

    public void setGridAdapter(CaldroidGridAdapter gridAdapter) {
        this.gridAdapter = gridAdapter;
    }

    public GridView getGridView() {
        return gridView;
    }

    public void setGridViewRes(int gridViewRes) {
        this.gridViewRes = gridViewRes;
    }

    private void setupGridView() {


        // Client normally needs to provide the adapter and onItemClickListener
        // before the fragment is attached to avoid complex crash due to
        // fragment life cycles
        if (gridAdapter != null) {
            gridView.setAdapter(gridAdapter);
        }

        if (onItemClickListener != null) {
            gridView.setOnItemClickListener(onItemClickListener);
        }
        if (onItemLongClickListener != null) {
            gridView.setOnItemLongClickListener(onItemLongClickListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If gridViewRes is not valid, use default fragment layout
        if (gridViewRes == 0) {
            gridViewRes = R.layout.date_grid_fragment;
        }

        if (themeResource == 0) {
            if (gridAdapter != null) {
                themeResource = gridAdapter.getThemeResource();
            }
        }

        if (gridView == null) {
            LayoutInflater localInflater = CaldroidFragment.getThemeInflater(getActivity(),
                    inflater, themeResource);
            gridView = (GridView) localInflater.inflate(gridViewRes, container, false);
            setupGridView();
        } else {
            ViewGroup parent = (ViewGroup) gridView.getParent();
            if (parent != null) {
                parent.removeView(gridView);
            }
        }

        return gridView;
    }

}
