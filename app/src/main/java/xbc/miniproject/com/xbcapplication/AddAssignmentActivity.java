package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import xbc.miniproject.com.xbcapplication.model.assignment.ModelAssignment;
import xbc.miniproject.com.xbcapplication.model.assignment.autoComplete.DataList;
import xbc.miniproject.com.xbcapplication.model.assignment.autoComplete.ModelAutoCompleteAssignment;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddAssignmentActivity extends Activity {
    private Context context = this;

    private EditText addAssignmentEditTextTitle,
            addAssignmentEditTextStartDate,
            addAssignmentEditTextEndDate,
            addAssignmentEditTextDescription;

    private AutoCompleteTextView addAssignmentEditTextName;

    private Button addAssignmentButtonSave,
            addAssignmentButtonCancel;
    private Calendar calendar;

    private boolean isNameSelected;
    KArrayAdapter<DataList> adapter;
    private List<DataList> listAssignment = new ArrayList<>();
    RequestAPIServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Create Assignmnet");

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDate();
            }
        };

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDate();
            }
        };

        addAssignmentEditTextTitle = (EditText) findViewById(R.id.addAssignmentEditTextTitle);
        addAssignmentEditTextDescription = (EditText) findViewById(R.id.addAssignmentEditTextDescription);

        addAssignmentEditTextName = (AutoCompleteTextView) findViewById(R.id.addAssignmentEditTextName);
        addAssignmentEditTextName.setThreshold(1);

        addAssignmentEditTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addAssignmentEditTextName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isNameSelected = true;
                addAssignmentEditTextName.setError(null);

                DataList selected = (DataList) adapterView.getAdapter().getItem(i);
                int aidi = selected.getId();

            }
        });

        addAssignmentEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isNameSelected = false;
                addAssignmentEditTextName.setError("Name must from the list!");
                listAssignment = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (addAssignmentEditTextName.getText().toString().trim().length() != 0);
                String keyword = addAssignmentEditTextName.getText().toString().trim();
                getAutoCompleteAPI(keyword);
            }
        });


        addAssignmentEditTextStartDate = (EditText) findViewById(R.id.addAssignmentEditTextStartDate);
        addAssignmentEditTextStartDate.setFocusable(false);
        addAssignmentEditTextStartDate.setClickable(true);
        addAssignmentEditTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, startDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addAssignmentEditTextEndDate = (EditText) findViewById(R.id.addAssignmentEditTextEndDate);
        addAssignmentEditTextEndDate.setFocusable(false);
        addAssignmentEditTextEndDate.setClickable(true);
        addAssignmentEditTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, endDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addAssignmentButtonSave = (Button) findViewById(R.id.addAssignmentButtonSave);
        addAssignmentButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();

            }
        });

        addAssignmentButtonCancel = (Button) findViewById(R.id.addAssignmentButtonCancel);
        addAssignmentButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        addAssignmentEditTextStartDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        addAssignmentEditTextEndDate.setText(sdf.format(calendar.getTime()));
    }

    private void getAutoCompleteAPI(String keyword){
        apiServices = APIUtilities.getAPIServices();
        apiServices.assignmentAutoComplete("application/json", SessionManager.getToken(context), keyword)
                .enqueue(new Callback<ModelAutoCompleteAssignment>() {
                    @Override
                    public void onResponse(Call<ModelAutoCompleteAssignment> call, Response<ModelAutoCompleteAssignment> response) {
                        if (response.code() == 200){
                            List<DataList> tmp = response.body().getDataList();
                            listAssignment = response.body().getDataList();
                            getAutoCompletAdapter();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAutoCompleteAssignment> call, Throwable t) {

                    }
                });
    }

    private void getAutoCompletAdapter(){
        adapter = new KArrayAdapter<>(context, android.R.layout.simple_list_item_1, listAssignment);
        addAssignmentEditTextName.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void inputValidation() {

        String startDateText = addAssignmentEditTextStartDate.getText().toString();
        String[] tempStart = startDateText.split("-");
        Calendar tempCalStart = Calendar.getInstance();
        tempCalStart.set(Integer.parseInt(tempStart[2]),Integer.parseInt(tempStart[1]),Integer.parseInt(tempStart[0]));

        String endDateText = addAssignmentEditTextEndDate.getText().toString();
        String[] tempEnd = endDateText.split("-");
        Calendar tempCalEnd = Calendar.getInstance();
        tempCalEnd.set(Integer.parseInt(tempEnd[2]),Integer.parseInt(tempEnd[1]),Integer.parseInt(tempEnd[0]));

        if (addAssignmentEditTextName.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Name Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(addAssignmentEditTextTitle.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Title Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(addAssignmentEditTextStartDate.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Start Date Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(addAssignmentEditTextEndDate.getText().toString().trim().length() == 0){
            Toast.makeText(context,"End Date Field still empty!",Toast.LENGTH_SHORT).show();
        } else if (tempCalStart.after(tempCalEnd)) {
            Toast.makeText(context, "End Date Must greater than or equal to the Start Date!", Toast.LENGTH_SHORT).show();
        } else {
//            SaveSuccessNotification();
            callAPICreateAssignmnet(addAssignmentEditTextName.getText().toString(), addAssignmentEditTextTitle.getText().toString(), addAssignmentEditTextStartDate.getText().toString(), addAssignmentEditTextEndDate.getText().toString(), addAssignmentEditTextDescription.getText().toString());
        }
    }

    private void callAPICreateAssignmnet(String name, String title, String startDate, String endDate, String description){
        String contentType = "application/json";
        String json = APIUtilities.generateAssignmentMap(name, title, startDate, endDate, description);
        RequestBody bodyRequest = RequestBody.create(APIUtilities.mediaType(), json);
        apiServices = APIUtilities.getAPIServices();

        apiServices.createNewAssignment("application/json", bodyRequest)
                .enqueue(new Callback<ModelAssignment>() {
                    @Override
                    public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {
                        if (response.code() == 201){
                            String message = response.body().getMessage();
                            if (message != null){
                                SaveSuccessNotification(context, message);
                            }
                            else {
                                Toast.makeText(context,"Message Gagal Diambil", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAssignment> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void SaveSuccessNotification(final Context context, String message) {
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