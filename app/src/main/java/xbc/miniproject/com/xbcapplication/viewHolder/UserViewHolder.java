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
import xbc.miniproject.com.xbcapplication.EditUserActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.trainer.ModelTrainer;
import xbc.miniproject.com.xbcapplication.model.user.DataList;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private TextView listUserUsername;
    private TextView listUserRole;
    private TextView listUserStatus;
    private ImageView listUserButtonAction;
    private RequestAPIServices apiServices;
    int id;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        listUserUsername =  (TextView)itemView.findViewById(R.id.listUserUsername);
        listUserRole = (TextView)itemView.findViewById(R.id.listUserRole);
        listUserStatus = (TextView) itemView.findViewById(R.id.listUserStatus);
        listUserButtonAction = (ImageView)itemView.findViewById(R.id.listUserButtonAction);
    }
    public void setModel(final DataList dataList, final int position, final Context context){
        listUserUsername.setText(dataList.getUsername());
        listUserRole.setText(dataList.getRole().getName());
        if(dataList.getIsDelete()==false){
            listUserStatus.setText("Active");
        }else {
            listUserStatus.setText("Deactive");
        }


        listUserButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu =  new PopupMenu(context, listUserButtonAction);
                popupMenu.inflate(R.menu.user_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.userMenuEdit:
                                Intent intent = new Intent(context, EditUserActivity.class);
                                intent.putExtra("id", dataList.getId());
                                ((Activity)context).startActivity(intent);
                                return true;
                            case R.id.userMenuDeactivate:
                                DeactiveQuestion(dataList, position, context);
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
    private void DeactiveQuestion(final DataList dataList, final int position, final Context context){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
        builder.setTitle("Warning !")
                .setMessage("Apakan Anda Yakin Akan Deactive "+dataList.getUsername())
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DeactiveUSerAPI(dataList, position, context);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }
    private void DeactiveUSerAPI(DataList dataList, final int position, final Context context){
        apiServices = APIUtilities.getAPIServices();
        id = dataList.getId();

        apiServices.deactivateUser(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context), id)
                .enqueue(new Callback<ModelTrainer>() {
                    @Override
                    public void onResponse(Call<ModelTrainer> call, Response<ModelTrainer> response) {
                        if(response.code()==200){
                            String message = response.body().getMessage();
                            if(message!=null){
                                DeactiveNotification(context, message);
                            }else {
                                DeactiveNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTrainer> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void DeactiveNotification(final Context context, String message){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
        builder.setTitle("NOTOFICATION !")
                .setMessage(message+"!")
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
