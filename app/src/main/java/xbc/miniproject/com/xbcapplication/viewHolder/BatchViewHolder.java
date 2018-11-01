package xbc.miniproject.com.xbcapplication.viewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import xbc.miniproject.com.xbcapplication.AddParticipantBatchActivity;
import xbc.miniproject.com.xbcapplication.EditBatchActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.batch.DataList;

public class BatchViewHolder extends RecyclerView.ViewHolder {
    TextView listBatchTextViewTechnology,
            listBatchTextViewName,
            listBatchTextViewTrainer;

    ImageView listBatchButtonAction;

    public BatchViewHolder(@NonNull View itemView) {
        super(itemView);

        listBatchButtonAction = (ImageView) itemView.findViewById(R.id.listBatchButtonAction);
        listBatchTextViewName = (TextView) itemView.findViewById(R.id.listBatchTextViewName);
        listBatchTextViewTechnology = (TextView) itemView.findViewById(R.id.listBatchTextViewTechnology);
        listBatchTextViewTrainer = (TextView) itemView.findViewById(R.id.listBatchTextViewTrainer);

    }

    public  void setModel(final DataList batchModel, final int position , final Context context){
        listBatchTextViewTechnology.setText(batchModel.getTechnology().getName());
        listBatchTextViewName.setText(batchModel.getName());
        listBatchTextViewTrainer.setText(batchModel.getTrainer().getName());

        listBatchButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Anda Menekan Action Posisi: "+position,Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context,listBatchButtonAction);
                popupMenu.inflate(R.menu.batch_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.batchMenuEdit:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, EditBatchActivity.class);
                                intent.putExtra("id", batchModel.getId());
                                ((Activity)context).startActivity(intent);
                                return true;
                            case R.id.batchMenuAddParticipant:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(context, AddParticipantBatchActivity.class);
                                ((Activity)context).startActivity(intent2);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }
}
