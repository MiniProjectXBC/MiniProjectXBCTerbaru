package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.testimony.DataListTestimony;
import xbc.miniproject.com.xbcapplication.viewHolder.TestimonyViewHolder;

public class TestimonyListAdapter extends RecyclerView.Adapter<TestimonyViewHolder>{
    private Context context;
    private List<DataListTestimony> testimonyModelList;
    public TestimonyListAdapter(Context context, List<DataListTestimony> testimonyModelList) {
        this.context=context;
        this.testimonyModelList=testimonyModelList;
    }
    @NonNull
    @Override
    public TestimonyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_testimony,
                viewGroup,
                false
        );
        return new TestimonyViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestimonyViewHolder testimonyViewHolder, int position) {
        final DataListTestimony user = testimonyModelList.get(position);
        testimonyViewHolder.setModel(user, position,context);
    }

    @Override
    public int getItemCount() {
        if(testimonyModelList!=null){
            return testimonyModelList.size();
        }else{
            return 0;
        }
    }
    public void filterList (List<DataListTestimony> filterLst){
        testimonyModelList = filterLst;
        notifyDataSetChanged();
    }
}
