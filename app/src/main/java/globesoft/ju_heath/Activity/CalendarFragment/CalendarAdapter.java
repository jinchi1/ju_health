package globesoft.ju_heath.Activity.CalendarFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by system777 on 2017-12-20.
 */

public class CalendarAdapter extends BaseAdapter {

    Context context;


    public CalendarAdapter(Context context ) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView textView = (TextView) convertView;
        textView.setText("item" + position);

        return convertView;
    }
}
