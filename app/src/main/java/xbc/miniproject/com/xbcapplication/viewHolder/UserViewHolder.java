package xbc.miniproject.com.xbcapplication.viewHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.dummyModel.UserModel;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private TextView listUserUsername;
    private TextView listUserRole;
    private TextView listUserStatus;
    private ImageView listUserButtonAction;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        listUserUsername =  (TextView)itemView.findViewById(R.id.listUserUsername);
        listUserRole = (TextView)itemView.findViewById(R.id.listUserRole);
        listUserStatus = (TextView) itemView.findViewById(R.id.listUserStatus);
        listUserButtonAction = (ImageView)itemView.findViewById(R.id.listUserButtonAction);
    }
    public void setModel(UserModel userModel, final int position, final Context context){
        listUserUsername.setText(userModel.getUsername());
        listUserRole.setText(userModel.getRole());
        listUserStatus.setText(userModel.getStatus());
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
                                //Toast anda menekan edit user
                                //panggil activity edit user
                                return true;
                            case R.id.userMenuDeactivate:
                                //Toas anda menekan deactive user
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
    private void DeactiveQuestion(UserModel userModel, final int position, final Context context){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
        builder.setTitle("Warning !")
                .setMessage("Apakan Anda Yakin Akan Deactive "+userModel.getUsername())
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DeactiveNotification(context);
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
    private void DeactiveNotification(final Context context){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
        builder.setTitle("NOTOFICATION !")
                .setMessage("Data Succesfully Deactive !")
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
