package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.dummyModel.MonitoringModel;
import xbc.miniproject.com.xbcapplication.model.monitoring.ModelMonitoring;
import xbc.miniproject.com.xbcapplication.model.monitoring.MonitoringBiodata;
import xbc.miniproject.com.xbcapplication.model.monitoring.MonitoringDataList;
import xbc.miniproject.com.xbcapplication.model.monitoring.autoComplete.DataList;
import xbc.miniproject.com.xbcapplication.model.monitoring.autoComplete.ModelMonitoringAutoComplete;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.ModelMonitoringGetOne;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddIdleMonitoringActivity extends Activity {
    private Context context = this;

    private EditText addMonitoringEditTextIdleDate,
            addMonitoringEditTextLastProjectAt,
            addMonitoringEditTextIdleReason;

    private AutoCompleteTextView addMonitoringEditTextName;

    private Button addMonitoringButtonSave,
            addMonitoringButtonCancel;

    private boolean isNameSelected;

    KArrayAdapter<DataList> adapter;

    private Calendar calendar;

    String idBiodata;

    private RequestAPIServices apiServices;
    private List<DataList> listMonitoring = new ArrayList<>();
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idle_monitoring);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Input Idle");

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

        addMonitoringEditTextName = (AutoCompleteTextView) findViewById(R.id.addMonitoringEditTextName);


        addMonitoringEditTextName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected = true;
                addMonitoringEditTextName.setError(null);

                DataList selected = (DataList) parent.getAdapter().getItem(position);
                int idLocal = selected.getId();
                idBiodata = idLocal+"";
            }
        });

        addMonitoringEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameSelected = false;
                addMonitoringEditTextName.setError("Name must from the list!");
                listMonitoring = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (addMonitoringEditTextName.getText().toString().trim().length() != 0) {
                    String keyword = addMonitoringEditTextName.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });

        addMonitoringEditTextIdleDate = (EditText) findViewById(R.id.addMonitoringEditTextIdleDate);
        addMonitoringEditTextIdleDate.setFocusable(false);
        addMonitoringEditTextIdleDate.setClickable(true);
        addMonitoringEditTextIdleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addMonitoringEditTextLastProjectAt = (EditText) findViewById(R.id.addMonitoringEditTextLastProjectAt);
        addMonitoringEditTextIdleReason = (EditText) findViewById(R.id.addMonitoringEditTextIdleReason);

        addMonitoringButtonSave = (Button) findViewById(R.id.addMonitoringButtonSave);
        addMonitoringButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        addMonitoringButtonCancel = (Button) findViewById(R.id.addMonitoringButtonCancel);
        addMonitoringButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getAutoCompleteAPI(String keyword) {

        apiServices = APIUtilities.getAPIServices();
        apiServices.getAutoCompleteMonitoringList(SessionManager.getToken(context), keyword).enqueue(new Callback<ModelMonitoringAutoComplete>() {
            @Override
            public void onResponse(Call<ModelMonitoringAutoComplete> call, Response<ModelMonitoringAutoComplete> response) {
                if (response.code() == 200) {
                    listMonitoring = response.body().getDataList();
                    getAutoCompletAdapter();
                }
            }

            @Override
            public void onFailure(Call<ModelMonitoringAutoComplete> call, Throwable t) {
            }
        });
    }

    private void getAutoCompletAdapter() {
        adapter = new KArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, listMonitoring);
        addMonitoringEditTextName.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        addMonitoringEditTextIdleDate.setText(sdf.format(calendar.getTime()));
    }

    private void inputValidation() {
        if (addMonitoringEditTextName.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Name Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addMonitoringEditTextIdleDate.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Idle Date Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (!isNameSelected) {
            addMonitoringEditTextName.setText("");
            Toast.makeText(context, "Name Field Must From the List!", Toast.LENGTH_SHORT).show();
        } else {
//            SaveSuccessNotification();
            loading = LoadingClass.loadingAnimationAndText(context,
                    "Sedang Mengupload Data . . .");
            loading.show();
            callAPICreateMonitoring();

        }
    }

    private void callAPICreateMonitoring() {
        String contentType = "application/json";
        String tokenAuthorization = SessionManager.getToken(context);
        String idleDate = addMonitoringEditTextIdleDate.getText().toString();
        String lastProject = addMonitoringEditTextLastProjectAt.getText().toString();
        String idleReason = addMonitoringEditTextIdleReason.getText().toString();

        String json = APIUtilities.generateMonitoring(idBiodata,idleDate,lastProject,idleReason);
        RequestBody requestBody = RequestBody.create(APIUtilities.mediaType(),json);

        apiServices = APIUtilities.getAPIServices();
        apiServices.createMonitoring(contentType,tokenAuthorization,requestBody).enqueue(new Callback<ModelMonitoringGetOne>() {
            @Override
            public void onResponse(Call<ModelMonitoringGetOne> call, Response<ModelMonitoringGetOne> response) {
                loading.dismiss();
                if (response.code() == 201){
//                    Toast.makeText(context, "Result : "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    SaveSuccessNotification(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ModelMonitoringGetOne> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(context, "Create Monitoring onFailure : "+t.getMessage(), Toast.LENGTH_SHORT).show();
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
