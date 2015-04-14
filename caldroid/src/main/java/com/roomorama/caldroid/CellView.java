package com.roomorama.caldroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.caldroid.R;

import java.util.ArrayList;

/**
 * Created by crocodile2u on 3/30/15.
 */
public class CellView extends TextView {

    public static final int STATE_TODAY = R.attr.state_date_today;
    public static final int STATE_SELECTED = R.attr.state_date_selected;
    public static final int STATE_DISABLED = R.attr.state_date_disabled;
    public static final int STATE_PREV_NEXT_MONTH = R.attr.state_date_prev_next_month;

    private ArrayList<Integer> customStates = new ArrayList<Integer>();

    public CellView(Context context) {
        super(context);
    }

    public CellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (null == customStates) customStates = new ArrayList<Integer>();
    }

    public void resetCustomStates() {
        customStates.clear();
    }

    public void addCustomState(int state) {
        if (!customStates.contains(state)) {
            customStates.add(state);
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        init();
        int customStateSize = customStates.size();
        if (customStateSize > 0) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + customStateSize);
            int[] stateArray = new int[customStateSize];
            int i = 0;
            for (Integer state : customStates) {
                stateArray[i] = state;
                i++;
            }
            mergeDrawableStates(drawableState, stateArray);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }
}
