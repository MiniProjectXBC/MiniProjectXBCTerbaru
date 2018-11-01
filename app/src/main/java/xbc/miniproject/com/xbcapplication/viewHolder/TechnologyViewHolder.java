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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.EditTechnologyActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.technology.DataList;
import xbc.miniproject.com.xbcapplication.model.technology.ModelTechnology;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;

public class TechnologyViewHolder extends RecyclerView.ViewHolder{
    private TextView listTecnologyName;
    private ImageView listTechnologyButtonAction;
    private RequestAPIServices apiServices;
    int id;

    public TechnologyViewHolder(@NonNull View itemView) {
        super(itemView);
        listTecnologyName = (TextView)itemView.findViewById(R.id.listTecnologyName);
        listTechnologyButtonAction = (ImageView)itemView.findViewById(R.id.listTechnologyButtonAction);
    }
    public void setModel(final DataList dataList, final int position, final Context context) {
        listTecnologyName.setText(dataList.getName());

        listTechnologyButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, listTechnologyButtonAction);
                popupMenu.inflate(R.menu.technology_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.technologyMenuEdit:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, EditTechnologyActivity.class);
                                intent.putExtra("id", dataList.getId());
                                ((Activity)context).startActivity(intent);
                                return true;
                            case R.id.tchnologyMenuDeactivate:
                                //Toast.makeText(context, "Anda Menekan Action Deactive pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                DeactiveQuestion(dataList, position, context);
//                                DeactiveSuccessNotification(context);
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
    private  void  DeactiveQuestion(final DataList dataList, final int position, final Context context){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Deactive "+ dataList.getName())
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deactiveTechnologyApi(dataList, position, context);
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
    private void deactiveTechnologyApi(DataList dataList, final int position, final Context context){
        apiServices = APIUtilities.getAPIServices();
        id = dataList.getId();

        apiServices.deactiveTechnology(Constanta.CONTENT_TYPE_API,
                Constanta.AUTHORIZATION_DEACTIVATED_TECHNOLOGY, id)
                .enqueue(new Callback<ModelTechnology>() {
                    @Override
                    public void onResponse(Call<ModelTechnology> call, Response<ModelTechnology> response) {
                        if(response.code() == 200){
                            String message = response.body().getMessage();
                            if(message!=null){
                                DeactiveSuccessNotification(context, message);
                            }else {
                                DeactiveSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTechnology> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void DeactiveSuccessNotification(final Context context, String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Testimony Successfully Deactivated!")
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
