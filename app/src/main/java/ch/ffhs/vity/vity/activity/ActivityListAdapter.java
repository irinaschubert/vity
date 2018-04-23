package ch.ffhs.vity.vity.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.ffhs.vity.vity.R;

/**
 * Created by hamme on 10.03.2018.
 */

public class ActivityListAdapter extends BaseAdapter {

    private List<VityItem> listActivities;
    private Context mContext;

    public ActivityListAdapter(List<VityItem> listActivities, Context mContext) {
        this.listActivities = listActivities;
        this.mContext = mContext;
    }

    public void addListItemToAdapter (List<VityItem> list){

        // Add new list
        listActivities.addAll(list);
        // Notify UI
        this.notifyDataSetChanged();
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
        View view = View.inflate(mContext, R.layout.list_item_activity, null);
        TextView viewTitle = (TextView) view.findViewById(R.id.list_activity_title);
        TextView viewDistance = (TextView) view.findViewById(R.id.list_activity_distance);

        // Set data
        viewTitle.setText(listActivities.get(position).getTitle());
        viewDistance.setText("Distance");

        //

        return view;
    }
}
