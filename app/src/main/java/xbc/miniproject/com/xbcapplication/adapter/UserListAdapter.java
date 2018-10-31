package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.user.DataList;
import xbc.miniproject.com.xbcapplication.viewHolder.UserViewHolder;

public class UserListAdapter  extends RecyclerView.Adapter<UserViewHolder>{
    private Context context;
    private List<DataList> userModelList;

    public UserListAdapter(Context context, List<DataList> userModelList) {
        this.context=context;
        this.userModelList=userModelList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View cutomView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_user,
                viewGroup,
                false
        );
        return new UserViewHolder(cutomView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        final DataList user = userModelList.get(position);
        userViewHolder.setModel(user, position, context);
    }

    @Override
    public int getItemCount() {
        if(userModelList!=null){
            return userModelList.size();
        }else {
            return 0;
        }
    }
    public void filterList (List<DataList>filterList){
        userModelList = filterList;
        notifyDataSetChanged();
    }
}
