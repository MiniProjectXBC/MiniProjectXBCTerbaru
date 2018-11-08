package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.technology.DataList;
import xbc.miniproject.com.xbcapplication.model.technology.ModelTechnology;
import xbc.miniproject.com.xbcapplication.model.technology.Technology;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class EditTechnologyActivity extends Activity {
    private Context context=this;
    private EditText editTechnologyEditTextName,
            editTechnologyEditTexNote;
    private Button editTechnologyButtonSave,
            editTechnologyButtonCancel;
    private RequestAPIServices apiServices;
    int id;
    String idData;
    List<DataList> listTechnology = new ArrayList<DataList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_technology);

        ActionBar actionBar =  getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Technology");

        editTechnologyEditTextName = (EditText)findViewById(R.id.editTechnologyEditTextName);
        editTechnologyEditTexNote = (EditText) findViewById(R.id.editTechnologyEditTexNote);
        editTechnologyButtonSave = (Button) findViewById(R.id.editTechnologyButtonSave);
        editTechnologyButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editValidation();
            }
        });
        editTechnologyButtonCancel =(Button)findViewById(R.id.editTechnologyButtonCancel);
        editTechnologyButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id= getIntent().getIntExtra("id", 0);
        getOneTechnologyAPI(id);
    }
    private void getOneTechnologyAPI(int id){
        apiServices = APIUtilities.getAPIServices();
        apiServices.getOneTechnology(SessionManager.getToken(context),id).enqueue(new Callback<ModelTechnology>() {
            @Override
            public void onResponse(Call<ModelTechnology> call, Response<ModelTechnology> response) {
                if(response.code()==200){
                    Technology data = response.body().getData();
                    idData = data.getId();
                    editTechnologyEditTextName.setText(data.getName());
                    editTechnologyEditTexNote.setText(data.getNotes());
                }else {
                    Toast.makeText(context, "Gagal Mendapatkan Testimony Technology: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTechnology> call, Throwable t) {
                Toast.makeText(context, "Get Testimony Technology onFailure"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void editValidation(){
        if(editTechnologyEditTextName.getText().toString().trim().length()==0){
            Toast.makeText(context,"Name Field still empty!",Toast.LENGTH_SHORT).show();
        }else if(editTechnologyEditTexNote.getText().toString().trim().length()==0){
            Toast.makeText(context,"Note Field still empty!",Toast.LENGTH_SHORT).show();
        }else{
            inputEditTechnologyAPI(id);
            //hanya pesan
            //saveSuccessfullyNotification();
            //Toast.makeText(context,"Testimony successfully updated!",Toast.LENGTH_SHORT).show();
        }
    }
    private void inputEditTechnologyAPI(int id){
        apiServices =  APIUtilities.getAPIServices();

        Technology data = new Technology();
//        System.out.println(id+"");
        data.setId(idData);
        data.setName(editTechnologyEditTextName.getText().toString());
        data.setNotes(editTechnologyEditTexNote.getText().toString());

        apiServices.editTechnology(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context),
                data)
                .enqueue(new Callback<ModelTechnology>() {
                    @Override
                    public void onResponse(Call<ModelTechnology> call, Response<ModelTechnology> response) {
                        if(response.code()==200){
                            String message = response.body().getMessage();
                            if(message!=null){
                                saveSuccessfullyNotification(message);
                            }else {
                                saveSuccessfullyNotification("Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTechnology> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public  void saveSuccessfullyNotification(String message){
        final AlertDialog.Builder builder;
        builder =  new AlertDialog.Builder(context);
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
