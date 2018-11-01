package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.Data;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.ModelMonitoringGetOne;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddPlacementMonitoringActivity extends Activity {
    Context context = this;

    EditText placementEditTextPlacementDate,
            placementEditTextPlacementAt,
            placementEditTextNotes;

    Button placementButtonSave,
            placementButtonCancel;

    Calendar calendar;
    private String idMonitoring;
    private RequestAPIServices apiServices;
    private ProgressDialog loadingInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_placement_monitoring);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Input Placement");

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        placementEditTextPlacementDate = (EditText) findViewById(R.id.placementEditTextPlacementDate);
        placementEditTextPlacementDate.setFocusable(false);
        placementEditTextPlacementDate.setClickable(true);
        placementEditTextPlacementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        placementEditTextPlacementAt = (EditText) findViewById(R.id.placementEditTextPlacementAt);
        placementEditTextNotes = (EditText) findViewById(R.id.placementEditTextNotes);

        placementButtonSave = (Button) findViewById(R.id.placementButtonSave);
        placementButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        placementButtonCancel = (Button) findViewById(R.id.placementButtonCancel);
        placementButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int aidi = getIntent().getIntExtra("idAutoComplete", 0);
        idMonitoring = aidi + "";
        getOneFromAPI(idMonitoring);
    }

    private void getOneFromAPI(String idMonitoring) {
        final ProgressDialog loading = LoadingClass.loadingAnimationAndText(context,
                "Sedang Memuat Data . . .");
        loading.show();
        apiServices = APIUtilities.getAPIServices();
        String contentType = "application/json";
        String tokenAuthorization = SessionManager.getToken(context);

        apiServices.getOneMonitoring(tokenAuthorization, idMonitoring).enqueue(new Callback<ModelMonitoringGetOne>() {
            @Override
            public void onResponse(Call<ModelMonitoringGetOne> call, Response<ModelMonitoringGetOne> response) {
                loading.dismiss();
                if (response.code() == 200) {
                    Data data = response.body().getData();
                    if (data.getPlacementDate() != null) {
                        placementEditTextPlacementDate.setText(data.getPlacementDate().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelMonitoringGetOne> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(context, "Get One Monitoring onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        placementEditTextPlacementDate.setText(sdf.format(calendar.getTime()));
    }

    private void inputValidation() {
        if (placementEditTextPlacementDate.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Placement Date Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (placementEditTextPlacementAt.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Placement At Field still empty!", Toast.LENGTH_SHORT).show();
        } else {
            loadingInput = LoadingClass.loadingAnimationAndText(context,
                    "Sedang Mengupload Data . . .");
            loadingInput.show();
            callAPIAddPlacement();
//            SaveSuccessNotification();
        }
    }

    private void callAPIAddPlacement() {
        String contentType = "application/json";
        String tokenAuthorization = SessionManager.getToken(context);
        String placementDate = placementEditTextPlacementDate.getText().toString();
        String placementAt = placementEditTextPlacementAt.getText().toString();
        String notes = placementEditTextNotes.getText().toString();

        String json = APIUtilities.generatedPlacementBody(idMonitoring,placementDate,placementAt,notes);
        RequestBody requestBody = RequestBody.create(APIUtilities.mediaType(),json);

        apiServices = APIUtilities.getAPIServices();
        apiServices.addPlacementMonitoring(contentType,tokenAuthorization,requestBody).enqueue(new Callback<ModelMonitoringGetOne>() {
            @Override
            public void onResponse(Call<ModelMonitoringGetOne> call, Response<ModelMonitoringGetOne> response) {
                loadingInput.dismiss();
                if (response.code() == 200){
//                    Toast.makeText(context, "Result : "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    SaveSuccessNotification(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ModelMonitoringGetOne> call, Throwable t) {
                loadingInput.dismiss();
                Toast.makeText(context, "Create Monitoring onFailure : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SaveSuccessNotification(String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage(message + "!")
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
