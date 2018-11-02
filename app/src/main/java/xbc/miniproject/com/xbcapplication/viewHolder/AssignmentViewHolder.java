package xbc.miniproject.com.xbcapplication.viewHolder;

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
import xbc.miniproject.com.xbcapplication.DoneAssignmentActivity;
import xbc.miniproject.com.xbcapplication.EditAssignmnetActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.assignment.AssignmentList;
import xbc.miniproject.com.xbcapplication.model.assignment.ModelAssignment;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AssignmentViewHolder extends RecyclerView.ViewHolder {
    TextView listAssignmentTextViewName,
            listAssignmentTextViewStartDate,
            listAssignmentTextViewEndDate;

    ImageView listAssignmentButtonAction;

    RequestAPIServices apiServices;

    int id;

    public AssignmentViewHolder(@NonNull View itemView) {
        super(itemView);

        listAssignmentTextViewName = (TextView) itemView.findViewById(R.id.listAssignmentTextViewName);
        listAssignmentTextViewStartDate = (TextView) itemView.findViewById(R.id.listAssignmentTextViewStartDate);
        listAssignmentTextViewEndDate = (TextView) itemView.findViewById(R.id.listAssignmentTextViewEndDate);

        listAssignmentButtonAction = (ImageView) itemView.findViewById(R.id.listAssignmentButtonAction);
    }

    public void setModel(final AssignmentList assignmentList, final int position, final Context context) {
        listAssignmentTextViewName.setText(assignmentList.getBiodata().getName());
        listAssignmentTextViewStartDate.setText(assignmentList.getStartDate());
        listAssignmentTextViewEndDate.setText(assignmentList.getEndDate());

        listAssignmentButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Anda Menekan Action Posisi: "+position,Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context, listAssignmentButtonAction);
                popupMenu.inflate(R.menu.assignmnet_action_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.assignmentMenuEdit:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: " + position, Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(context, EditAssignmnetActivity.class);
                                intent1.putExtra("id", assignmentList.getId().toString());
                                intent1.putExtra("name", assignmentList.getBiodata().getName().toString());
//                                intent1.putExtra("title", assignmentList.getTitle().toString());
                                intent1.putExtra("startDate", assignmentList.getStartDate().toString());
                                intent1.putExtra("endDate", assignmentList.getEndDate().toString());
//                                intent1.putExtra("description", assignmentList.getNotes().toString());
                                context.startActivity(intent1);
                                return true;
                            case R.id.assignmentMenuDelete:
                                //Toast.makeText(context, "Anda Menekan Action Deactive pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                DeleteQuestion(assignmentList,position,context);
                                return true;
                            case R.id.assignmentMenuHold:
//                                Toast.makeText(context, "Anda Menekan Action Hold pada Posisi: " + position, Toast.LENGTH_SHORT).show();
                                HoldQuestion(assignmentList, position, context);
                                return true;
                            case R.id.assignmentMenuDone:
//                                Toast.makeText(context, "Anda Menekan Action Done pada Posisi: " + position, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, DoneAssignmentActivity.class);
                                intent.putExtra("id", assignmentList.getId().toString());
                                context.startActivity(intent);
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

    private void DeleteQuestion(final AssignmentList assignmentList, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Menghapus " + assignmentList.getBiodata().getName() + "?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DeleteSuccessNotification(context);
                        dialog.dismiss();
                        DeleteAssignmentAPI(assignmentList, position, context);
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

    private void DeleteAssignmentAPI(final AssignmentList assignmentList, final int position, final Context context){
        apiServices = APIUtilities.getAPIServices();
        id = assignmentList.getId();

        apiServices.deleteAssignmnet("application/json", SessionManager.getToken(context), id)
                .enqueue(new Callback<ModelAssignment>() {
                    @Override
                    public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {
                        if (response.code() == 200) {
                            String message = response.body().getMessage();
                            if (message != null) {
                                DeleteSuccessNotification(context, message);
                            } else {
                                DeleteSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAssignment> call, Throwable t) {
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

    private void HoldQuestion(final AssignmentList assignmentList, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Hold " + assignmentList.getBiodata().getName() + "?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        HoldSuccessNotification(context);
                        dialog.dismiss();
                        HoldAssignmentAPI(assignmentList, position, context);
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

    private void HoldAssignmentAPI(final AssignmentList assignmentList, final int position, final Context context){
        apiServices = APIUtilities.getAPIServices();
        id = assignmentList.getId();

        apiServices.holdAssigment("application/json", SessionManager.getToken(context), id)
                .enqueue(new Callback<ModelAssignment>() {
                    @Override
                    public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {
                        if (response.code() == 200) {
                            String message = response.body().getMessage();
                            if (message != null) {
                                HoldSuccessNotification(context, message);
                            } else {
                                HoldSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAssignment> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void HoldSuccessNotification(final Context context, String message) {
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