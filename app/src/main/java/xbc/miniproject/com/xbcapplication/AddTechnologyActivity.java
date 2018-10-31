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
import xbc.miniproject.com.xbcapplication.model.technology.DataList;
import xbc.miniproject.com.xbcapplication.model.technology.ModelTechnology;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;

public class AddTechnologyActivity extends Activity {
    private Context context =this;
    private EditText addTechnologyEditTextName
            ,addTechnologyEditTexNote;
    private Button addTechnologyButtonSave,
            addTechnologyButtonCancel;
    private RequestAPIServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_technology);

        ActionBar actionBar = getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);

        addTechnologyEditTextName = (EditText) findViewById(R.id.addTechnologyEditTextName);
        addTechnologyEditTexNote = (EditText) findViewById(R.id.addTechnologyEditTexNote);
        addTechnologyButtonSave = (Button) findViewById(R.id.addTechnologyButtonSave);
        addTechnologyButtonCancel = (Button) findViewById(R.id.addTechnologyButtonCancel);

        //button save data
        addTechnologyButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        //button cancel
        addTechnologyButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void inputValidation(){
        if(addTechnologyEditTextName.getText().toString().trim().length()==0){
            Toast.makeText(context,"Name Field still empty!",Toast.LENGTH_SHORT).show();
        }else if (addTechnologyEditTexNote.getText().toString().trim().length()==0){
            Toast.makeText(context,"Note Field still empty!",Toast.LENGTH_SHORT).show();
        }else{
            panggilAPICreateTechnology();
        }
    }
    private void panggilAPICreateTechnology(){
        apiServices = APIUtilities.getAPIServices();
        DataList data = new DataList();
        data.setName(addTechnologyEditTextName.getText().toString());
        data.setNotes(addTechnologyEditTexNote.getText().toString());

        apiServices.createNewTechnology("application/json", data)
                .enqueue(new Callback<ModelTechnology>() {
                    @Override
                    public void onResponse(Call<ModelTechnology> call, Response<ModelTechnology> response) {
                        if(response.code()==201){
                            String message =  response.body().getMessage();
                            if(message!=null){
                                saveSuccesNotification(message);
                            }else{
                                saveSuccesNotification("Massage Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTechnology> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void saveSuccesNotification(String message){
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
