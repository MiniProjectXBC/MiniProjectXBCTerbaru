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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.biodata.Biodata;
import xbc.miniproject.com.xbcapplication.model.biodata.BiodataList;
import xbc.miniproject.com.xbcapplication.model.biodata.ModelBiodata;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class EditBiodataActivity extends Activity {

    Context context = this;

    EditText editBiodataEditTextName,
            editBiodataEditTextLastEducation,
            editBiodataEditTextEducationLevel,
            editBiodataEditTextGraduationYear,
            editBiodataEditTextMajors,
            editBiodataEditTextGpa;

    Button editBiodataButtonSave,
            editBiodataButtonCancel;

    RequestAPIServices apiServices;

    int id;

    List<BiodataList> listBiodata = new ArrayList<BiodataList>();
    private ProgressDialog loadingInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_biodata);

        ActionBar actionBar = getActionBar();
        ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Biodata");

        editBiodataEditTextName = (EditText) findViewById(R.id.editBiodataEditTextName);
        editBiodataEditTextLastEducation = (EditText) findViewById(R.id.editBiodataEditTextLastEducation);
        editBiodataEditTextEducationLevel = (EditText) findViewById(R.id.editBiodataEditTextEducationLevel);
        editBiodataEditTextGraduationYear = (EditText) findViewById(R.id.editBiodataEditTextGraduationYear);
        editBiodataEditTextMajors = (EditText) findViewById(R.id.editBiodataEditTextMajors);
        editBiodataEditTextGpa = (EditText) findViewById(R.id.editBiodataEditTextGpa);

        editBiodataButtonSave = (Button) findViewById(R.id.editBiodataButtonSave);
        editBiodataButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        editBiodataButtonCancel = (Button) findViewById(R.id.editBiodataButtonCancel);
        editBiodataButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id = getIntent().getIntExtra("id",0);
        getOneBiodataAPI(id);


    }

    private void getOneBiodataAPI(int id) {
        final ProgressDialog loading = LoadingClass.loadingAnimationAndText(context,
            "Sedang Memuat Data . . .");
        loading.show();

        apiServices = APIUtilities.getAPIServices();
        apiServices.getOneBiodata(SessionManager.getToken(context),id).enqueue(new Callback<ModelBiodata>() {
            @Override
            public void onResponse(Call<ModelBiodata> call, Response<ModelBiodata> response) {
                loading.dismiss();
                if (response.code() == 200){
                    Biodata data = response.body().getData();
                    editBiodataEditTextName.setText(data.getName());
                    editBiodataEditTextLastEducation.setText(data.getLastEducation().toString());
                    editBiodataEditTextEducationLevel.setText(data.getEducationalLevel().toString());
                    editBiodataEditTextGraduationYear.setText(data.getGraduationYear().toString());
                    editBiodataEditTextMajors.setText(data.getMajors());
                    editBiodataEditTextGpa.setText(data.getGpa());
                } else{
                    Toast.makeText(context, "Gagal Mendapatkan Testimony Biodata: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelBiodata> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(context, "Get One Biodata onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void inputValidation() {
        if (editBiodataEditTextName.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Name Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(editBiodataEditTextLastEducation.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Last Education Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(editBiodataEditTextEducationLevel.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Education Level Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(editBiodataEditTextGraduationYear.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Graduation Year Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(editBiodataEditTextMajors.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Majors Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(editBiodataEditTextGpa.getText().toString().trim().length() == 0){
            Toast.makeText(context,"GPA Field still empty!",Toast.LENGTH_SHORT).show();
        } else{
            loadingInput = LoadingClass.loadingAnimationAndText(context,
                    "Sedang Mengupload Data . . .");
            loadingInput.show();
            inputEditBiodataAPI();
            //SaveSuccessNotification();
        }
    }

    private void inputEditBiodataAPI() {
        apiServices = APIUtilities.getAPIServices();

        Biodata data = new Biodata();
        data.setId(id);
        data.setName(editBiodataEditTextName.getText().toString());
        data.setLastEducation(editBiodataEditTextLastEducation.getText().toString());
        data.setEducationalLevel(editBiodataEditTextEducationLevel.getText().toString());
        data.setGraduationYear(editBiodataEditTextGraduationYear.getText().toString());
        data.setMajors(editBiodataEditTextMajors.getText().toString());
        data.setGpa(editBiodataEditTextGpa.getText().toString());

        apiServices.editBiodata(Constanta.CONTENT_TYPE_API,
                SessionManager.getToken(context),
                data)
                .enqueue(new Callback<ModelBiodata>() {
            @Override
            public void onResponse(Call<ModelBiodata> call, Response<ModelBiodata> response) {
                loadingInput.dismiss();
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
            public void onFailure(Call<ModelBiodata> call, Throwable t) {
                loadingInput.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SaveSuccessNotification(String message) {
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

        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
