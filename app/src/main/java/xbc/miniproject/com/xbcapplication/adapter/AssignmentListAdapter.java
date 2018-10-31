package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.assignment.AssignmentList;
import xbc.miniproject.com.xbcapplication.viewHolder.AssignmentViewHolder;

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentViewHolder> {
    private Context context;
    private List<AssignmentList> assignmentList;

    public AssignmentListAdapter(Context context, List<AssignmentList> assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_assignment,
                viewGroup,
                false
        );
        return new AssignmentViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder assignmentViewHolder, int i) {
        final AssignmentList user = assignmentList.get(i);
        assignmentViewHolder.setModel(user, i, context);
    }

    @Override
    public int getItemCount() {
        if (assignmentList != null) {
            return assignmentList.size();
        } else {
            return 0;
        }
    }

    public void filterList(List<AssignmentList> filterList) {
        assignmentList = filterList;
        notifyDataSetChanged();
    }
}
