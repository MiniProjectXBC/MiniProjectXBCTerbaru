package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.idleNews.IdleNewsList;
import xbc.miniproject.com.xbcapplication.model.idleNews.ModelIdleNews;
import xbc.miniproject.com.xbcapplication.model.idleNews.shareIdleNews.ModelShareIdleNews;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class ShareIdleNewsActivity extends Activity {
    private Context context = this;

    EditText shareIdleNewsEditTextEmail;

    Button shareIdleNewsButtonSend,
            shareIdleNewsButtonCancel;

    RequestAPIServices apiServices;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_idle_news);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Send to Email");

        shareIdleNewsEditTextEmail = (EditText) findViewById(R.id.shareIdleNewsEditTextEmail);

        shareIdleNewsButtonSend =(Button) findViewById(R.id.shareIdleNewsButtonSend);
        shareIdleNewsButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValidation();
            }
        });
        shareIdleNewsButtonCancel = (Button) findViewById(R.id.shareIdleNewsButtonCancel);
        shareIdleNewsButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void inputValidation() {
        if (shareIdleNewsEditTextEmail.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Email Field still empty!",Toast.LENGTH_SHORT).show();
        } else{
//            SaveSuccessNotification();
            ShareQuestion();
        }
    }

    private void ShareQuestion() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Mengirim Data ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DeleteSuccessNotification(context);
                        dialog.dismiss();
                        ShareIdleNewsAPI();
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

    private void ShareIdleNewsAPI() {
        apiServices = APIUtilities.getAPIServices();
        ModelShareIdleNews data = new ModelShareIdleNews();
        data.setEmail(shareIdleNewsEditTextEmail.getText().toString());

        apiServices.shareNewIdleNews("application/json", SessionManager.getToken(context), data)
                .enqueue(new Callback<ModelIdleNews>() {
                    @Override
                    public void onResponse(Call<ModelIdleNews> call, Response<ModelIdleNews> response) {
                        if (response.code()==200){
                            String message = response.body().getMessage();
                            if (message != null){
                                ShareSuccessNotification(context, message);
                            }
                            else {
                                ShareSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelIdleNews> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void ShareSuccessNotification(final Context context, String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage(message+"!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}