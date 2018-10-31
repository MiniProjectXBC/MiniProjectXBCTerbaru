package xbc.miniproject.com.xbcapplication.viewHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.kelas.DataList;
import xbc.miniproject.com.xbcapplication.model.kelas.ModelClass;
import xbc.miniproject.com.xbcapplication.model.kelas.delete.ModelClassDelete;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class ClassViewHolder extends RecyclerView.ViewHolder {
    private TextView listClassTextViewBatch,
            listClassTextViewName;
    private ImageView listClassButtonAction;
    private RequestAPIServices apiServices;
    int id;

    public ClassViewHolder(@NonNull View itemView) {
        super(itemView);


        listClassTextViewBatch = (TextView) itemView.findViewById(R.id.listClassTextViewBatch);
        listClassTextViewName = (TextView) itemView.findViewById(R.id.listClassTextViewName);
        listClassButtonAction = (ImageView) itemView.findViewById(R.id.listClassButtonAction);
    }

    public void setModel (final DataList classModel, final int position, final Context context){
        listClassTextViewBatch.setText(classModel.getBatch().getTechnology().getName());
        listClassTextViewName.setText(classModel.getBiodata().getName());

        listClassButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, listClassButtonAction);
                popupMenu.inflate(R.menu.class_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.classMenuDelete:
                                DeleteQuestion(classModel, position, context);
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

    private void DeleteQuestion(final DataList classModel, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Menghapus "+ classModel.getBiodata().getName()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteClassApi(classModel, position, context);
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

    private void deleteClassApi(DataList classModel, final int position, final Context context){
        apiServices = APIUtilities.getAPIServices();
        id = classModel.getId();

        apiServices.deleteClass(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context), id)
                .enqueue(new Callback<ModelClass>() {
                    @Override
                    public void onResponse(Call<ModelClass> call, Response<ModelClass> response) {
                        if(response.code() == 200){
                            String message = response.body().getMessage();
                            if(message!=null){
                                DeleteSuccessNotification(context, message);
                            }else{
                                DeleteSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelClass> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void DeleteSuccessNotification(final Context context, String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
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
