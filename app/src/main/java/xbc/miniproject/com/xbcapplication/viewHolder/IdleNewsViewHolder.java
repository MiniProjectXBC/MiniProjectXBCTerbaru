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
import xbc.miniproject.com.xbcapplication.EditIdleNewsActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.ShareIdleNewsActivity;
import xbc.miniproject.com.xbcapplication.model.idleNews.IdleNewsList;
import xbc.miniproject.com.xbcapplication.model.idleNews.ModelIdleNews;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class IdleNewsViewHolder extends RecyclerView.ViewHolder {
    TextView listIdleNewsTextViewTitle,
            listIdleNewsTextViewCategory;

    ImageView listIdleNewsButtonAction;

    RequestAPIServices apiServices;

    int id;

    public IdleNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        listIdleNewsButtonAction = (ImageView) itemView.findViewById(R.id.listIdleNewsButtonAction);
        listIdleNewsTextViewTitle = (TextView) itemView.findViewById(R.id.listIdelNewsTextViewTitle);
        listIdleNewsTextViewCategory = (TextView) itemView.findViewById(R.id.listIdleNewsTextViewCategory);
    }

    public void setModelIdle(final IdleNewsList idleNewsList, final int position, final Context context){
        listIdleNewsTextViewTitle.setText(idleNewsList.getTitle());
        listIdleNewsTextViewCategory.setText(idleNewsList.getCategory().getName());

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
                                intent.putExtra("title", idleNewsList.getTitle().toString());
                                intent.putExtra("category", idleNewsList.getCategory().getName().toString());
//                                intent.putExtra("content", idleNewsList.getContent().toString());
                                ((Activity)context).startActivity(intent);
                                return true;
                            case R.id.idleNewsMenuPublish:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                PublishQuestion(idleNewsList,position,context);
                                return true;
                            case R.id.idleNewsMenuShare:
                                //Toast.makeText(context, "Anda Menekan Action Edit pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(context, ShareIdleNewsActivity.class);
                                ((Activity)context).startActivity(intent1);
                                return true;
                            case R.id.idleNewsMenuDelete:
                                //Toast.makeText(context, "Anda Menekan Action Deactive pada Posisi: "+position,Toast.LENGTH_SHORT).show();
                                DeleteQuestion(idleNewsList,position,context);
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

    private void DeleteQuestion(final IdleNewsList idleNewsList, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Menghapus "+ idleNewsList.getTitle()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DeleteSuccessNotification(context);
                        dialog.dismiss();
                        DeleteIdleNewsAPI(idleNewsList, position, context);
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

    private void DeleteIdleNewsAPI(final IdleNewsList idleNewsList, final int position, final Context context){
        apiServices = APIUtilities.getAPIServices();
        id = idleNewsList.getId();

        apiServices.deleteIdleNews("application/json", SessionManager.getToken(context), id)
                .enqueue(new Callback<ModelIdleNews>() {
                    @Override
                    public void onResponse(Call<ModelIdleNews> call, Response<ModelIdleNews> response) {
                        if (response.code() == 200){
                            String message = response.body().getMessage();
                            if (message != null){
                                DeleteSuccessNotification(context, message);
                            }
                            else {
                                DeleteSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelIdleNews> call, Throwable t) {
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
                .setCancelable(false).show();
    }

    private void PublishQuestion(final IdleNewsList idleNewsList, final int position, final Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Publish "+ idleNewsList.getTitle()+"?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        dialog.dismiss();
                        PublishIdleNewsAPI(idleNewsList, position, context);
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

    private void PublishIdleNewsAPI(final IdleNewsList idleNewsList, final int position, final Context context) {
            apiServices = APIUtilities.getAPIServices();
            id = idleNewsList.getId();

            apiServices.publishIdleNews("application/json", SessionManager.getToken(context), id)
                    .enqueue(new Callback<ModelIdleNews>() {
                        @Override
                        public void onResponse(Call<ModelIdleNews> call, Response<ModelIdleNews> response) {
                            if (response.code()==200){
                                String message = response.body().getMessage();
                                if (message != null){
                                    PublishSuccessNotification(context, message);
                                }
                                else {
                                    PublishSuccessNotification(context, "Message Gagal Diambil");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelIdleNews> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
    }

    private void PublishSuccessNotification(final Context context, String message) {
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
                .setCancelable(false).show();
    }
}
