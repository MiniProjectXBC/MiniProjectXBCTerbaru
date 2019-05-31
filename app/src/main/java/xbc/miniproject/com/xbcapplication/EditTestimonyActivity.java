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
import xbc.miniproject.com.xbcapplication.model.testimony.ModelTestimony;
import xbc.miniproject.com.xbcapplication.model.testimony.Testimony;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class EditTestimonyActivity extends Activity {
    private Context context = this;
    private EditText editTestimonyEditTextTitle,
            editTestimonyEditTexContent;
    private Button editTestimonyButtonSave,
            editTestimonyButtonCancel;
    private RequestAPIServices apiServices;
    int id, idData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_testimony);

        ActionBar actionBar = getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Testimony");
        editTestimonyEditTextTitle = (EditText) findViewById(R.id.editTestimonyEditTextTitle);
        editTestimonyEditTexContent = (EditText) findViewById(R.id.editTestimonyEditTexContent);
        editTestimonyButtonSave = (Button) findViewById(R.id.editTestimonyButtonSave);
        editTestimonyButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });
        editTestimonyButtonCancel =  (Button) findViewById(R.id.editTestimonyButtonCancel);
        editTestimonyButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        int id = getIntent().getIntExtra("id",0);
        id=getIntent().getIntExtra("id",0);
        getOneTestimonyAPI(id);
    }


    private void getOneTestimonyAPI(int id) {
        apiServices = APIUtilities.getAPIServices();
        apiServices.getOneTestimony(SessionManager.getToken(context),id).enqueue(new Callback<ModelTestimony>() {
            @Override
            public void onResponse(Call<ModelTestimony> call, Response<ModelTestimony> response) {
                if (response.code() == 200){
                    Testimony data = response.body().getData();
                    idData =data.getId();

                    editTestimonyEditTextTitle.setText(data.getTitle());
                    editTestimonyEditTexContent.setText(data.getContent().toString());
                } else{
                    Toast.makeText(context, "Gagal Mendapatkan Testimony: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTestimony> call, Throwable t) {
                Toast.makeText(context, "Get Testimony onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }






    private void inputValidation(){
        if(editTestimonyEditTextTitle.getText().toString().trim().length()==0){
            Toast.makeText(context, "Title Field still Empty!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Testimony Successfully Added ! ", Toast.LENGTH_SHORT).show();
            //saveSuccesNotification();
            callAPIEditTrainer();
        }
    }

    private void callAPIEditTrainer(){
        apiServices = APIUtilities.getAPIServices();

        Testimony data = new Testimony();
        data.setId(id);
        data.setTitle(editTestimonyEditTextTitle.getText().toString());
        data.setContent(editTestimonyEditTexContent.getText().toString());

        apiServices.editTestimony(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context), data)
                .enqueue(new Callback<ModelTestimony>() {
                    @Override
                    public void onResponse(Call<ModelTestimony> call, Response<ModelTestimony> response) {
                        if (response.code() == 200) {
                            String message = response.body().getMessage();
                            if (message!=null){
                                SaveSuccessNotification(message);
                            } else{
                                SaveSuccessNotification("Message Gagal Diambil");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTestimony> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public  void SaveSuccessNotification(String message){
        final AlertDialog.Builder builder;
        builder=  new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Testimony Successfully Updated !")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
