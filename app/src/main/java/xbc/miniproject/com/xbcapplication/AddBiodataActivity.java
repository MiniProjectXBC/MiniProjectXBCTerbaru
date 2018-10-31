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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.biodata.BiodataList;
import xbc.miniproject.com.xbcapplication.model.biodata.ModelBiodata;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddBiodataActivity extends Activity {
    Context context = this;

    EditText addBiodataEditTextName,
            addBiodataEditTextLastEducation,
            addBiodataEditTextEducationLevel,
            addBiodataEditTextGraduationYear,
            addBiodataEditTextMajors,
            addBiodataEditTextGpa;

    Button addBiodataButtonSave,
            addBiodataButtonCancel;

    RequestAPIServices apiServices;

    String notification;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_biodata);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Input Biodata");

        addBiodataEditTextName = (EditText) findViewById(R.id.addBiodataEditTextName);
        addBiodataEditTextLastEducation = (EditText) findViewById(R.id.addBiodataEditTextLastEducation);
        addBiodataEditTextEducationLevel = (EditText) findViewById(R.id.addBiodataEditTextEducationLevel);
        addBiodataEditTextGraduationYear = (EditText) findViewById(R.id.addBiodataEditTextGraduationYear);
        addBiodataEditTextMajors = (EditText) findViewById(R.id.addBiodataEditTextMajors);
        addBiodataEditTextGpa = (EditText) findViewById(R.id.addBiodataEditTextGpa);

        addBiodataButtonSave = (Button) findViewById(R.id.addBiodataButtonSave);
        addBiodataButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();

            }
        });

        addBiodataButtonCancel = (Button) findViewById(R.id.addBiodataButtonCancel);
        addBiodataButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void inputValidation() {
        if (addBiodataEditTextName.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Name Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBiodataEditTextLastEducation.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Last Education Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBiodataEditTextEducationLevel.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Education Level Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBiodataEditTextGraduationYear.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Graduation Year Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBiodataEditTextMajors.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Majors Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBiodataEditTextGpa.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "GPA Field still empty!", Toast.LENGTH_SHORT).show();
        } else {
//            SaveSuccessNotification();
            loading = LoadingClass.loadingAnimationAndText(context,
                    "Sedang Mengupload Data . . .");
            loading.show();
            callAPICreateBiodata();
        }
    }

    private void callAPICreateBiodata() {
        apiServices = APIUtilities.getAPIServices();

        BiodataList data = new BiodataList();
        data.setName(addBiodataEditTextName.getText().toString());
        data.setLastEducation(addBiodataEditTextLastEducation.getText().toString());
        data.setEducationalLevel(addBiodataEditTextEducationLevel.getText().toString());
        data.setGraduationYear(addBiodataEditTextGraduationYear.getText().toString());
        data.setMajors(addBiodataEditTextMajors.getText().toString());
        data.setGpa(addBiodataEditTextGpa.getText().toString());

        apiServices.createNewBiodata(SessionManager.getToken(context),"application/json", data)
                .enqueue(new Callback<ModelBiodata>() {
                    @Override
                    public void onResponse(Call<ModelBiodata> call, Response<ModelBiodata> response) {
                        loading.dismiss();
                        if (response.code() == 201) {
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
                        loading.dismiss();
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

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
