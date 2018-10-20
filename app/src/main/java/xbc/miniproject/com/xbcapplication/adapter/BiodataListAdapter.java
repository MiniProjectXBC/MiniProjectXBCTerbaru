package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.dummyModel.BiodataModel;
import xbc.miniproject.com.xbcapplication.viewHolder.BiodataViewHolder;

public class BiodataListAdapter extends RecyclerView.Adapter<BiodataViewHolder> {
    private Context context;
    private List<BiodataModel> biodataList;

    public BiodataListAdapter(Context context, List<BiodataModel> biodataList) {
        this.context = context;
        this.biodataList = biodataList;
    }

    @NonNull
    @Override
    public BiodataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_biodata,
                viewGroup,
                false
        );
        return new BiodataViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull BiodataViewHolder biodataViewHolder, int position) {
        final BiodataModel user = biodataList.get(position);
        biodataViewHolder.setModel(user,position,context);
    }

    @Override
    public int getItemCount() {
        if (biodataList != null) {
            return biodataList.size();
        } else {
            return 0;
        }
    }
}
