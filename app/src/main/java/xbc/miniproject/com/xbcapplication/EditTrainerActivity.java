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
import xbc.miniproject.com.xbcapplication.model.trainer.ModelTrainer;
import xbc.miniproject.com.xbcapplication.model.trainer.Trainer;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class EditTrainerActivity extends Activity {
    private Context context=this;
    private EditText editTrainerEditTextName,
            editTrainerEditTexNote;
    private Button editTrainerButtonSave,
            editTrainerButtonCancel;
    RequestAPIServices apiServices;
    int id, idData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trainer);


        ActionBar actionBar = getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Trainer");

        editTrainerEditTextName = (EditText) findViewById(R.id.editTrainerEditTextName);
        editTrainerEditTexNote = (EditText) findViewById(R.id.editTrainerEditTexNote);
        editTrainerButtonSave = (Button) findViewById(R.id.editTrainerButtonSave);
        editTrainerButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editValidation();
            }
        });


        editTrainerButtonCancel = (Button) findViewById(R.id.editTrainerButtonCancel);
        editTrainerButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        int id = getIntent().getIntExtra("id",0);
        id=getIntent().getIntExtra("id",0);
        getOneTrainerAPI(id);

    }

    private void getOneTrainerAPI(int id) {
        apiServices = APIUtilities.getAPIServices();
        apiServices.getOneTrainer(SessionManager.getToken(context),id).enqueue(new Callback<ModelTrainer>() {
            @Override
            public void onResponse(Call<ModelTrainer> call, Response<ModelTrainer> response) {
                if (response.code() == 200){
                    Trainer data = response.body().getData();
                     idData =data.getId();


                    editTrainerEditTextName.setText(data.getName());
                    editTrainerEditTexNote.setText(data.getNotes().toString());
                } else{
                    Toast.makeText(context, "Gagal Mendapatkan Trainer: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTrainer> call, Throwable t) {
                Toast.makeText(context, "Get Trainer onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editValidation(){
        if(editTrainerEditTextName.getText().toString().trim().length()==0){
            Toast.makeText(context,"Name Field still empty!",Toast.LENGTH_SHORT).show();
        }
//        else if(editTrainerEditTexNote.getText().toString().trim().length()==0){
//            Toast.makeText(context,"Note Field still empty!",Toast.LENGTH_SHORT).show();
//        }
        else{
            //hanya pesan
            callAPIEditTrainer();
        }
    }

    private void callAPIEditTrainer(){
        apiServices = APIUtilities.getAPIServices();

        Trainer data = new Trainer();
        data.setId(id);
        data.setName(editTrainerEditTextName.getText().toString());
        data.setNotes(editTrainerEditTexNote.getText().toString());

        apiServices.editTrainer(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context),
                data)
                .enqueue(new Callback<ModelTrainer>() {
                    @Override
                    public void onResponse(Call<ModelTrainer> call, Response<ModelTrainer> response) {
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
                    public void onFailure(Call<ModelTrainer> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public  void SaveSuccessNotification(String message){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Trainer Successfully Updated !")
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
