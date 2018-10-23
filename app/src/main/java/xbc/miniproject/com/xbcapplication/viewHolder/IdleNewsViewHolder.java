package xbc.miniproject.com.xbcapplication.viewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import xbc.miniproject.com.xbcapplication.EditBiodataActivity;
import xbc.miniproject.com.xbcapplication.EditIdleNewsActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.ShareIdleNewsActivity;
import xbc.miniproject.com.xbcapplication.dummyModel.BiodataModel;
import xbc.miniproject.com.xbcapplication.dummyModel.IdleNewsModel;

public class IdleNewsViewHolder extends RecyclerView.ViewHolder {
    TextView listIdelNewsTextViewTitle,
            listIdleNewsTextViewCategory;

    ImageView listIdleNewsButtonAction;

    public IdleNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        listIdleNewsButtonAction = (ImageView) itemView.findViewById(R.id.listIdleNewsButtonAction);
        listIdelNewsTextViewTitle = (TextView) itemView.findViewById(R.id.listIdelNewsTextViewTitle);
        listIdleNewsTextViewCategory = (TextView) itemView.findViewById(R.id.listIdleNewsTextViewCategory);
    }

    public void setModelIdle(final IdleNewsModel idleNewsModel, final int position, final Context context){
        listIdelNewsTextViewTitle.setText(idleNewsModel.getTitle());
        listIdelNewsTextViewTitle.setText(idleNewsModel.getCategory());

        listIdleNewsButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Anda Menekan Action Posisi: "+position,Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context,listIdleNewsButtonAction);
                popupMenu.inflate(R.menu.idle_news_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.idleNewsMenuEdit:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, EditIdleNewsActivity.class);
                                ((Activity)context).startActivity(intent);
                                return true;
                            case R.id.idleNewsMenuPublish:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                PublishQuestion(idleNewsModel,position,context);
                                return true;
                            case R.id.idleNewsMenuShare:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(context, ShareIdleNewsActivity.class);
                                ((Activity)context).startActivity(intent1);
                                return true;
                            case R.id.idleNewsMenuDelete:
                                //Toast.makeText(context, "Anda Menekan Action Deactive pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                DeleteQuestion(idleNewsModel,position,context);
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

    private void DeleteQuestion(final IdleNewsModel idleNewsModel, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Menghapus "+ idleNewsModel.getTitle()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteSuccessNotification(context);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).show();
    }

    private void DeleteSuccessNotification(final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Data Successfully Deleted!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).show();
    }

    private void PublishQuestion(final IdleNewsModel idleNewsModel, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Publish "+ idleNewsModel.getTitle()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PublishSuccessNotification(context);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void PublishSuccessNotification(final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Data Successfully Published!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }
}