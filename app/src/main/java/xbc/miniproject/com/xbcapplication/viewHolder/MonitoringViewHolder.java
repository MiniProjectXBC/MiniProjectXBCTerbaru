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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.AddPlacementMonitoringActivity;
import xbc.miniproject.com.xbcapplication.EditBiodataActivity;
import xbc.miniproject.com.xbcapplication.EditIdleMonitoringActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.dummyModel.MonitoringModel;
import xbc.miniproject.com.xbcapplication.fragment.BiodataFragment;
import xbc.miniproject.com.xbcapplication.fragment.MonitoringFragment;
import xbc.miniproject.com.xbcapplication.model.monitoring.ModelMonitoring;
import xbc.miniproject.com.xbcapplication.model.monitoring.MonitoringDataList;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.ModelMonitoringGetOne;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class MonitoringViewHolder extends RecyclerView.ViewHolder {
    TextView listMonitoringTextViewName,
            listMonitoringTextViewIdleDate,
            listMonitoringTextViewPlacementDate;

    ImageView listMonitoringButtonAction;
    private RequestAPIServices apiServices;
    private int id;

    public MonitoringViewHolder(@NonNull View itemView) {
        super(itemView);

        listMonitoringTextViewName = (TextView) itemView.findViewById(R.id.listMonitoringTextViewName);
        listMonitoringTextViewIdleDate = (TextView) itemView.findViewById(R.id.listMonitoringTextViewIdleDate);
        listMonitoringTextViewPlacementDate = (TextView) itemView.findViewById(R.id.listMonitoringTextViewPlacementDate);

        listMonitoringButtonAction = (ImageView) itemView.findViewById(R.id.listMonitoringButtonAction);
    }

    public void setModel(final MonitoringDataList monitoringModel, final int position, final Context context){
        listMonitoringTextViewName.setText(monitoringModel.getMonitoringBiodata().getName());
        listMonitoringTextViewIdleDate.setText(monitoringModel.getIdleDate());
        if (monitoringModel.getPlacementDate() == null){
            listMonitoringTextViewPlacementDate.setText("");
        } else{
            listMonitoringTextViewPlacementDate.setText(monitoringModel.getPlacementDate().toString());
        }

        listMonitoringButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Anda Menekan Action Posisi: "+position,Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context, listMonitoringButtonAction);
                popupMenu.inflate(R.menu.monitoring_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.monitoringMenuEdit:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: " + position, Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(context,EditIdleMonitoringActivity.class);
                                intent1.putExtra("id",monitoringModel.getId());
                                context.startActivity(intent1);
                                return true;
                            case R.id.monitoringMenuPlacement:
                                //Toast.makeText(context, "Anda Menekan Action Placement pada Posisi: " + position, Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(context,AddPlacementMonitoringActivity.class);
                                intent2.putExtra("id",monitoringModel.getId());
                                context.startActivity(intent2);
                                return true;
                            case R.id.monitoringMenuDelete:
//                                Toast.makeText(context, "Anda Menekan Action Delete pada Posisi: " + position, Toast.LENGTH_SHORT).show();
                                DeleteQuestion(monitoringModel, position, context);
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

    private void DeleteQuestion(final MonitoringDataList monitoringModel, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Delete "+ monitoringModel.getMonitoringBiodata().getName()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DeactiveSuccessNotification(context);
                        deleteMonitoringAPI(monitoringModel,position,context,dialog);

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

    private void deleteMonitoringAPI(MonitoringDataList monitoringModel, int position, final Context context, final DialogInterface dialog) {
        apiServices = APIUtilities.getAPIServices();
        id = monitoringModel.getId();
        String ide = id+"";

        apiServices.deleteMonitoring(Constanta.CONTENT_TYPE_API,SessionManager.getToken(context),ide).enqueue(new Callback<ModelMonitoringGetOne>() {
            @Override
            public void onResponse(Call<ModelMonitoringGetOne> call, Response<ModelMonitoringGetOne> response) {
                dialog.dismiss();
                if (response.code() == 200){
                    String message = response.body().getMessage();
                    if (message!=null){
                        DeactiveSuccessNotification(context,message);
                    } else{
                        DeactiveSuccessNotification(context,"Message Gagal Dinonaktifkan");
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelMonitoringGetOne> call, Throwable t) {

            }
        });
    }

    private void DeactiveSuccessNotification(final Context context, String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage(message+"!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MonitoringFragment monitoringFragment= new MonitoringFragment();
                        EditText monitoringEditTextSearch = (EditText) ((Activity) context).findViewById(R.id.monitoringEditTextSearch);
                        RecyclerView monitoringRecyclerView = (RecyclerView) ((Activity) context).findViewById(R.id.monitoringRecyclerViewList);
                        monitoringEditTextSearch.setText("");
                        monitoringRecyclerView.setVisibility(View.INVISIBLE);
                    }
                })
                .setCancelable(false)
                .show();
    }
}
