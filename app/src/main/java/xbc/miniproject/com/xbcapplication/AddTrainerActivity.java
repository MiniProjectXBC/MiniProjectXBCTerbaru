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
import xbc.miniproject.com.xbcapplication.model.trainer.DataListTrainer;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;

public class AddTrainerActivity extends Activity {

    private Context context =this;
    private EditText addTrainerEditTextName
            ,addTrainerEditTexNote;
    private Button addTrainerButtonSave,
            addTrainerButtonCancel;


    RequestAPIServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trainer);

        ActionBar actionBar =  getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);

        addTrainerEditTextName = (EditText) findViewById(R.id.addTrainerEditTextName);
        addTrainerEditTexNote = (EditText) findViewById(R.id.addTrainerEditTexNote);
        addTrainerButtonSave = (Button) findViewById(R.id.addTrainerButtonSave);
        addTrainerButtonCancel = (Button) findViewById(R.id.addTrainerButtonCancel);

        //button save data
        addTrainerButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        //button cancel
        addTrainerButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void inputValidation(){
        if(addTrainerEditTextName.getText().toString().trim().length()==0){
            Toast.makeText(context,"Name Field still empty!",Toast.LENGTH_SHORT).show();
        }else if (addTrainerEditTexNote.getText().toString().trim().length()==0){
            Toast.makeText(context,"Note Field still empty!",Toast.LENGTH_SHORT).show();
        }else{
            callAPICreateTrainer();


            //saveSuccesNotification();
        }
    }

    private void callAPICreateTrainer() {
        apiServices = APIUtilities.getAPIServices();
        DataListTrainer data = new DataListTrainer();
        data.setName(addTrainerEditTextName.getText().toString());
        data.setNotes(addTrainerEditTexNote.getText().toString());

        apiServices.createNewTrainer("application/json", data)
                .enqueue(new Callback<DataListTrainer>() {
                    @Override
                    public void onResponse(Call<DataListTrainer> call, Response<DataListTrainer> response) {
                        if (response.code() == 201) {
                            SaveSuccessNotification();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataListTrainer> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void SaveSuccessNotification(){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Testimony Successfully Added! ")
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
