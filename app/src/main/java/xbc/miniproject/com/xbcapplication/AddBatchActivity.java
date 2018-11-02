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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.batch.ModelBatch;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.ModelBatchTechnologyAutoComplete;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.ModelBatchTrainerAutoComplete;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddBatchActivity extends Activity {
    private Context context = this;

    private EditText addBatchEditTextName, addBatchEditTextPeriodForm,
            addBatchEditTextPeriodTo, addBatchEditTextRoom,
            addBatchEditTextNotes;

    private AutoCompleteTextView addBatchEditTextTechnology, addBatchEditTextTrainer;

    private Spinner spinnerBatchType;

    private Button addBatchButtonSave, addBatchButtonCancel;

    private RequestAPIServices apiServices;

    private boolean isNameSelected;
    private boolean isNameSelected2;

    String idBiodata;
    String idBiodata2;

    String spinner;

    KArrayAdapter<xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList> adapter;
    KArrayAdapter<xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList> adapter2;

    private List<xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList> listBatch = new ArrayList<>();

    private List<xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList> listBatch2 = new ArrayList<>();

    String[] arrayType = {
            "- Pilih Type -","Gratis","Reguler"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch);

        ActionBar actionBar = getActionBar();
        ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);

        addBatchEditTextTechnology = (AutoCompleteTextView) findViewById(R.id.addBatchEditTextTechnology);
        addBatchEditTextTechnology.setThreshold(1);
        addBatchEditTextTechnology.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected = true;
                addBatchEditTextTechnology.setError(null);

                xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList selected = (xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList) parent.getAdapter().getItem(position);
                int gg = selected.getId();
                idBiodata = gg+"";
                Toast.makeText(context,"idnya ini cuy: "+gg,Toast.LENGTH_LONG).show();
            }
        });
        addBatchEditTextTechnology.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameSelected = false;
                addBatchEditTextTechnology.setError("Name must from the list!");
                listBatch = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(addBatchEditTextTechnology.getText().toString().trim().length() != 0){
                    String keyword = addBatchEditTextTechnology.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });

        addBatchEditTextTrainer = (AutoCompleteTextView) findViewById(R.id.addBatchEditTextTrainer);
        addBatchEditTextTrainer.setThreshold(1);
        addBatchEditTextTrainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected2 = true;
                addBatchEditTextTrainer.setError(null);

                xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList selected2 = (xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList) parent.getAdapter().getItem(position);
                int jj = selected2.getId();
                idBiodata2 = jj+"";
                //Toast.makeText(context,"idnya ini bos: "+jj,Toast.LENGTH_LONG).show();
            }
        });

        addBatchEditTextTrainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameSelected2 = false;
                addBatchEditTextTrainer.setError("Name must from the list! ");
                listBatch2 = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(addBatchEditTextTrainer.getText().toString().trim().length() != 0){
                    String keyword = addBatchEditTextTrainer.getText().toString().trim();
                    getAutoCompleteAPI2(keyword);
                }
            }
        });

        addBatchEditTextName = (EditText) findViewById(R.id.addBatchEditTextName);

        Calendar today = Calendar.getInstance();
        final int yearStart = today.get(Calendar.YEAR);
        final int monthStart = today.get(Calendar.MONTH);
        final int dayStart = today.get(Calendar.DATE);
        addBatchEditTextPeriodForm = (EditText) findViewById(R.id.addBatchEditTextPeriodForm);
        addBatchEditTextPeriodForm.setFocusable(false);
        addBatchEditTextPeriodForm.setClickable(true);
        addBatchEditTextPeriodForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        R.style.DatePickerPeriod, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selected = Calendar.getInstance();
                        selected.set(year,month,dayOfMonth);

                        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
                        String periodForm = formatDate.format(selected.getTime());

                        addBatchEditTextPeriodForm.setText(periodForm);
                    }
                }, yearStart, monthStart, dayStart
                );
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.show();
            }
        });

        spinnerBatchType = (Spinner) findViewById(R.id.spinnerBatchType);

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,
                arrayType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBatchType.setAdapter(adapterType);
        spinnerBatchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addBatchEditTextPeriodTo = (EditText) findViewById(R.id.addBatchEditTextPeriodTo);
        addBatchEditTextPeriodTo.setFocusable(false);
        addBatchEditTextPeriodTo.setClickable(true);
        addBatchEditTextPeriodTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        R.style.DatePickerPeriod, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selected = Calendar.getInstance();
                        selected.set(year,month,dayOfMonth);

                        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
                        String periodTo = formatDate.format(selected.getTime());

                        addBatchEditTextPeriodTo.setText(periodTo);
                    }
                }, yearStart, monthStart, dayStart
                );
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.show();
            }
        });

        addBatchEditTextRoom = (EditText) findViewById(R.id.addBatchEditTextRoom);

        addBatchEditTextNotes = (EditText) findViewById(R.id.addBatchEditTextNotes);

        addBatchButtonSave = (Button) findViewById(R.id.addBatchButtonSave);
        addBatchButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        addBatchButtonCancel = (Button) findViewById(R.id.addBatchButtonCancel);
        addBatchButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAutoCompleteAPI(String keyword){
        apiServices = APIUtilities.getAPIServices();
        apiServices.getAutoCompleteBatchTechnologyList2(SessionManager.getToken(context), keyword)
                .enqueue(new Callback<ModelBatchTechnologyAutoComplete>() {
            @Override
            public void onResponse(Call<ModelBatchTechnologyAutoComplete> call, Response<ModelBatchTechnologyAutoComplete> response) {
                if(response.code() == 200){
                    listBatch = response.body().getDataList();
                    getAutoCompleteAdapter();
                }
            }

            @Override
            public void onFailure(Call<ModelBatchTechnologyAutoComplete> call, Throwable t) {

            }
        });
    }
    private void getAutoCompleteAdapter(){
        adapter = new KArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, listBatch);
        addBatchEditTextTechnology.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getAutoCompleteAPI2(String keyword){

        apiServices = APIUtilities.getAPIServices();
        apiServices.getAutoCompleteBatchTrainerList(SessionManager.getToken(context), keyword).enqueue(new Callback<ModelBatchTrainerAutoComplete>() {
                    @Override
                    public void onResponse(Call<ModelBatchTrainerAutoComplete> call, Response<ModelBatchTrainerAutoComplete> response2) {
                        if(response2.code() == 200) {
                            listBatch2 = response2.body().getDataList();
                            getAutoCompletAdapter2();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelBatchTrainerAutoComplete> call, Throwable t) {

                    }
                });
    }

    private  void getAutoCompletAdapter2(){
        adapter2 = new KArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, listBatch2);
        addBatchEditTextTrainer.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

    }

    private void inputValidation() {
        if (addBatchEditTextTechnology.getText().toString().trim().length() == 0){
            Toast.makeText(context, "Technology Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBatchEditTextTrainer.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Trainer Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBatchEditTextName.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Name Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBatchEditTextPeriodForm.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Period form Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBatchEditTextPeriodTo.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Period to Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBatchEditTextRoom.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Room Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (addBatchEditTextNotes.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Notes Field still empty!", Toast.LENGTH_SHORT).show();
        } else{
//            SaveSuccessNotification();
            callAPICreateBatch(addBatchEditTextTechnology.getText().toString(),
                    addBatchEditTextTrainer.getText().toString(),
                    addBatchEditTextName.getText().toString(),
                    addBatchEditTextPeriodForm.getText().toString(),
                    addBatchEditTextPeriodTo.getText().toString(),
                    addBatchEditTextRoom.getText().toString(),
                    spinner,
                    addBatchEditTextNotes.getText().toString());
        }
    }

    private void callAPICreateBatch(String technology, String trainer, String
            name, String periodFrom, String periodTo, String room, String bootcampType, String note){

        String contenType = Constanta.CONTENT_TYPE_API;
        String token = SessionManager.getToken(context);
        String JSON =  APIUtilities.generateBatch(idBiodata, idBiodata2, name, periodFrom, periodTo,
                room,bootcampType, note);
        RequestBody requestBody =  RequestBody.create(APIUtilities.mediaType(), JSON);
        apiServices = APIUtilities.getAPIServices();
//        DataList data = new DataList();
//        Technology data2 = new Technology();
//        Trainer data3 = new Trainer();
//        data.getTechnology().setName(addBatchEditTextTechnology.getText().toString());
//        data.getTrainer().setName(addBatchEditTextTrainer.getText().toString());
//        data.setName(addBatchEditTextName.getText().toString());
//        data.setPeriodFrom(addBatchEditTextPeriodForm.getText().toString());
//        data.setPeriodTo(addBatchEditTextPeriodTo.getText().toString());
//        data.setRoom(addBatchEditTextRoom.getText().toString());
//        data.setBootcampType(spinnerBatchType.getAdapter().toString()); //Belum ada getText
//        data.setNotes(addBatchEditTextNotes.getText().toString());


        apiServices.createNewBatch(contenType, requestBody)
                .enqueue(new Callback<ModelBatch>() {
                    @Override
                    public void onResponse(Call<ModelBatch> call, Response<ModelBatch> response) {
                        if(response.code() == 201){
                            String message = response.body().getMessage();
                            if(message!=null){
                                SaveSuccessNotification(message);
                            } else {
                                SaveSuccessNotification("Data Gagal Ditambahkan");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelBatch> call, Throwable t) {
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