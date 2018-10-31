package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.trainer.DataListTrainer;
import xbc.miniproject.com.xbcapplication.viewHolder.TrainerViewHolder;

public class TrainerListAdapter extends RecyclerView.Adapter<TrainerViewHolder> {

    private Context context;
    private List<DataListTrainer> trainerModelList;

    public TrainerListAdapter(Context context, List<DataListTrainer> trainerModelList) {
        this.context = context;
        this.trainerModelList = trainerModelList;
    }

    @NonNull
    @Override
    public TrainerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_trainer,
                viewGroup,
                false
        );
        return new TrainerViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerViewHolder trainerViewHolder, int position) {
        final DataListTrainer user= trainerModelList.get(position);
        trainerViewHolder.setModel(user, position, context);
    }

    @Override
    public int getItemCount() {
        if(trainerModelList!=null){
            return trainerModelList.size();
        }else {
            return 0;
        }
    }
    public  void filterList(ArrayList<DataListTrainer> filterList){
        trainerModelList = filterList;
        notifyDataSetChanged();
    }
}
