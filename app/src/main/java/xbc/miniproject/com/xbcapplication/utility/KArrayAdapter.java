package xbc.miniproject.com.xbcapplication.utility;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.List;

public class KArrayAdapter<T>
        extends ArrayAdapter<T> {
    private Filter filter = new KNoFilter();
    public List<T> items;

    @Override
    public Filter getFilter() {
        return filter;
    }

    public KArrayAdapter(Context context, int textViewResourceId,
                         List<T> objects) {
        super(context, textViewResourceId, objects);
        Log.v("Krzys", "Adapter created " + filter);
        items = objects;
    }

    private class KNoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults result = new FilterResults();
            result.values = items;
            result.count = items.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            notifyDataSetChanged();
        }
    }
}