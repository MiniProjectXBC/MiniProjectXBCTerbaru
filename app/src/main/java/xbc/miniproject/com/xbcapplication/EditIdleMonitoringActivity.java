package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.monitoring.autoComplete.DataList;
import xbc.miniproject.com.xbcapplication.model.monitoring.autoComplete.ModelMonitoringAutoComplete;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.Data;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.ModelMonitoringGetOne;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class EditIdleMonitoringActivity extends Activity {
    Context context = this;

    AutoCompleteTextView editMonitoringEditTextName;

    EditText editMonitoringEditTextIdleDate,
            editMonitoringEditTextLastProjectAt,
            editMonitoringEditTextIdleReason;

    Button editMonitoringButtonSave,
            editMonitoringButtonCancel;

    boolean isNameSelected = true;

    Calendar calendar;

    String idMonitoring;
    String idBiodata;

    RequestAPIServices apiServices;

    private String[] names = {};
    private List<DataList> listMonitoring = new ArrayList<>();
    private ProgressDialog loadingInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idle_monitoring);

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

        editMonitoringEditTextName = (AutoCompleteTextView) findViewById(R.id.editMonitoringEditTextName);
        editMonitoringEditTextName.setThreshold(1);

        editMonitoringEditTextName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected = true;
                editMonitoringEditTextName.setError(null);

                DataList selected = (DataList) parent.getAdapter().getItem(position);
                int aidi = selected.getId();
                idBiodata = aidi+"";
            }
        });

        editMonitoringEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameSelected = false;
                editMonitoringEditTextName.setError("Name must from the list!");
                listMonitoring = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editMonitoringEditTextName.getText().toString().trim().length() != 0) {
                    String keyword = editMonitoringEditTextName.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });

        editMonitoringEditTextIdleDate = (EditText) findViewById(R.id.editMonitoringEditTextIdleDate);
        editMonitoringEditTextIdleDate.setFocusable(false);
        editMonitoringEditTextIdleDate.setClickable(true);
        editMonitoringEditTextIdleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        editMonitoringEditTextLastProjectAt = (EditText) findViewById(R.id.editMonitoringEditTextLastProjectAt);
        editMonitoringEditTextIdleReason = (EditText) findViewById(R.id.editMonitoringEditTextIdleReason);

        editMonitoringButtonSave = (Button) findViewById(R.id.editMonitoringButtonSave);
        editMonitoringButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        editMonitoringButtonCancel = (Button) findViewById(R.id.editMonitoringButtonCancel);
        editMonitoringButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Idle");

        int aidi = getIntent().getIntExtra("id", 0);
        idMonitoring = aidi+"";
        getOneFromAPI(idMonitoring);

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
//                loading.dismiss();

            }
        });
    }

    private void getAutoCompletAdapter() {
        KArrayAdapter adapter = new KArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, listMonitoring);
        editMonitoringEditTextName.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                    editMonitoringEditTextName.setText(data.getBiodata().getName());
                    editMonitoringEditTextIdleDate.setText(data.getIdleDate());
                    editMonitoringEditTextLastProjectAt.setText(data.getLastProject());
                    editMonitoringEditTextIdleReason.setText(data.getIdleReason());
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

        editMonitoringEditTextIdleDate.setText(sdf.format(calendar.getTime()));
    }

    private void inputValidation() {
        if (editMonitoringEditTextName.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Name Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (editMonitoringEditTextIdleDate.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Idle Date Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (!isNameSelected) {
            editMonitoringEditTextName.setText("");
            Toast.makeText(context, "Name Field Must From the List!", Toast.LENGTH_SHORT).show();
        } else {
//            SaveSuccessNotification();
            loadingInput = LoadingClass.loadingAnimationAndText(context,
                    "Sedang Mengupload Data . . .");
            loadingInput.show();
            callAPIEditMonitoring();
        }
    }

    private void callAPIEditMonitoring() {
        String contentType = "application/json";
        String tokenAuthorization = SessionManager.getToken(context);
        String idleDate = editMonitoringEditTextIdleDate.getText().toString();
        String lastProject = editMonitoringEditTextLastProjectAt.getText().toString();
        String idleReason = editMonitoringEditTextIdleReason.getText().toString();

        String json = APIUtilities.generateEditMonitoring(idMonitoring,idBiodata,idleDate,lastProject,idleReason);
        RequestBody requestBody = RequestBody.create(APIUtilities.mediaType(),json);

        apiServices = APIUtilities.getAPIServices();
        apiServices.editMonitoring(contentType,tokenAuthorization,requestBody).enqueue(new Callback<ModelMonitoringGetOne>() {
            @Override
            public void onResponse(Call<ModelMonitoringGetOne> call, Response<ModelMonitoringGetOne> response) {
                loadingInput.dismiss();
                if (response.code() == 200){
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
