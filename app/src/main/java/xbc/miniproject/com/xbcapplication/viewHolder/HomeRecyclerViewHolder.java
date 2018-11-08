package xbc.miniproject.com.xbcapplication.viewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import xbc.miniproject.com.xbcapplication.R;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView lblListHeader;

    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        lblListHeader = (TextView) itemView.findViewById(R.id.lblListHeader);

    }

    public void setModel(Context context, String header, int position){
        lblListHeader.setText(header);
    }
}
