package ch.ffhs.vity.vity.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.VityItem;

public class VityItemListAdapter extends BaseAdapter {

    private List<VityItem> listActivities;
    private Context mContext;

    public VityItemListAdapter(List<VityItem> listActivities, Context mContext) {
        this.listActivities = listActivities;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listActivities.size();
    }

    @Override
    public Object getItem(int position) {
        return listActivities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.activity_search_list_items, null);
        }

        TextView viewTitle = convertView.findViewById(R.id.list_activity_title);
        TextView viewDistance = convertView.findViewById(R.id.list_activity_distance);

        // Set data
        viewTitle.setText(listActivities.get(position).getTitle());
        viewDistance.setText(listActivities.get(position).getLocation());

        return convertView;
    }
}
