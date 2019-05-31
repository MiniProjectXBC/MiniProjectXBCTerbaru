package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.monitoring.ModelMonitoring;
import xbc.miniproject.com.xbcapplication.model.monitoring.MonitoringDataList;
import xbc.miniproject.com.xbcapplication.viewHolder.BiodataViewHolder;
import xbc.miniproject.com.xbcapplication.viewHolder.MonitoringViewHolder;

public class MonitoringListAdapter extends RecyclerView.Adapter<MonitoringViewHolder> {
    private Context context;
    private List<MonitoringDataList> monitoringList;

    public MonitoringListAdapter(Context context, List<MonitoringDataList> monitoringList) {
        this.context = context;
        this.monitoringList = monitoringList;
    }

    @NonNull
    @Override
    public MonitoringViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_monitoring,
                viewGroup,
                false
        );
        return new MonitoringViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitoringViewHolder monitoringViewHolder, int position) {
        final MonitoringDataList user = monitoringList.get(position);
        monitoringViewHolder.setModel(user, position, context);

    }

    @Override
    public int getItemCount() {
        if (monitoringList != null) {
            return monitoringList.size();
        } else {
            return 0;
        }
    }

    public void filterList(List<MonitoringDataList> filterList) {
        monitoringList = filterList;
        notifyDataSetChanged();
    }
}
