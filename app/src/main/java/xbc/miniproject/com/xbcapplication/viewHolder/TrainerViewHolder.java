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
import xbc.miniproject.com.xbcapplication.EditTrainerActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.trainer.DataListTrainer;
import xbc.miniproject.com.xbcapplication.model.trainer.ModelTrainer;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class TrainerViewHolder extends RecyclerView.ViewHolder {
    private TextView listTrainerName;
    private ImageView listTrainerButtonAction;
    private RequestAPIServices apiServices;
    int id;

    public TrainerViewHolder(@NonNull View itemView) {
        super(itemView);
        listTrainerName = (TextView)itemView.findViewById(R.id.listTrainerName);
        listTrainerButtonAction = (ImageView)itemView.findViewById(R.id.listTrainerButtonAction);
    }
    public void setModel(final DataListTrainer trainerModel, final int position, final Context context) {
        listTrainerName.setText(trainerModel.getName());
        listTrainerButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, listTrainerButtonAction);
                popupMenu.inflate(R.menu.trainer_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.trainerMenuEdit:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, EditTrainerActivity.class);
                                intent.putExtra("id",trainerModel.getId());
                                ((Activity)context).startActivity(intent);
                                return true;
                            case R.id.trainerMenuDeactivate:
                                //Toast.makeText(context, "Anda Menekan Action Deactive pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                DeactiveQuestion(trainerModel, position, context);
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

    private void DeactiveQuestion(final DataListTrainer trainerModel, final int position, final Context context){

        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan MenonAktifkan "+ trainerModel.getName()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DeactiveSuccessNotification(context);
                        deactiveTrainerAPI(trainerModel,position,context);
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


    private void deactiveTrainerAPI(DataListTrainer trainerModel, int position, final Context context) {
        apiServices = APIUtilities.getAPIServices();
        id = trainerModel.getId();

        apiServices.deactivateTrainer(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context),id)
                .enqueue(new Callback<ModelTrainer>() {
                    @Override
                    public void onResponse(Call<ModelTrainer> call, Response<ModelTrainer> response) {
                        if (response.code() == 200){
                            String message = response.body().getMessage();
                            if (message!=null){
                                DeactiveSuccessNotification(context,message);
                            } else{
                                DeactiveSuccessNotification(context,"Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTrainer> call, Throwable t) {
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
