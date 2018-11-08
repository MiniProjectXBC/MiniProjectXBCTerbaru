package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.viewHolder.BiodataViewHolder;
import xbc.miniproject.com.xbcapplication.viewHolder.HomeRecyclerViewHolder;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    Context context;
    List<String> dataList;

    public HomeRecyclerAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_home_parent_menu,
                viewGroup,
                false
        );
        return new HomeRecyclerViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder homeRecyclerViewHolder, int i) {
        homeRecyclerViewHolder.setModel(context,dataList.get(i),i);
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }
}
