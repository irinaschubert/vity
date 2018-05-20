package ch.ffhs.vity.vity.activity;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.LocationTypeConverter;
import ch.ffhs.vity.vity.database.VityItem;
import ch.ffhs.vity.vity.map.Map;

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
        viewDistance.setText(getDistance(listActivities.get(position)));

        return convertView;
    }

    private String getDistance(VityItem item){
        Location itemLocation = LocationTypeConverter.toLocation(item.getLocation());
        Location currentLocation = Map.getCurrentLocation();
        LatLng myPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        float[] results = new float[1];
        currentLocation.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), itemLocation.getLatitude(), itemLocation.getLongitude(), results);
        // cut decimals from float
        DecimalFormat df = new DecimalFormat("#0");
        return df.format(results[0]);
    }

}
