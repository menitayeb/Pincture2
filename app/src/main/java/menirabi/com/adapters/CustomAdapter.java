package menirabi.com.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Oren on 20/06/2015.
 */
public class CustomAdapter<T> extends ArrayAdapter<CharSequence> {
    public CustomAdapter(Context context, int textViewResourceId, CharSequence[] objects) {
        super(context, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText("");
        return view;
    }
}
