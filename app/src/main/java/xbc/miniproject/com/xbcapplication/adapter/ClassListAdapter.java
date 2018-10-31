package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.kelas.DataList;
import xbc.miniproject.com.xbcapplication.viewHolder.ClassViewHolder;

public class ClassListAdapter extends RecyclerView.Adapter<ClassViewHolder> {
    private Context context;
    private List<DataList> dataList;

    public ClassListAdapter(Context context, List<DataList> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override

    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_class,
                viewGroup,
                false
        );
        return new ClassViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int i) {
        final DataList user = dataList.get(i);
        classViewHolder.setModel(user, i, context);
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }
    public void filterList(List<DataList> filterList) {
        dataList = filterList;
        notifyDataSetChanged();
    }
}
