package com.roomorama.caldroid;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.caldroid.R;

import java.util.List;

/**
 * Customize the weekday gridview
 */
public class WeekdayArrayAdapter extends ArrayAdapter<String> {
    public static int textColor = Color.LTGRAY;

	int layoutId;

    public WeekdayArrayAdapter(Context context, int layoutId, int textViewResourceId,List<String> objects) {
        super(context, textViewResourceId, objects);
		this.layoutId = layoutId;
    }

    // To prevent cell highlighted when clicked
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // To customize text size and color
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) inflater.inflate(layoutId, null);

        // Set content
        String item = getItem(position);
        textView.setText(item);

        textView.setTextColor(textColor);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

}
