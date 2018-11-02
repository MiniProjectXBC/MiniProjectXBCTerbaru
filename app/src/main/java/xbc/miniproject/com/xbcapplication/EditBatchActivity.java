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
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.ModelBatchTechnologyAutoComplete;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.ModelBatchTrainerAutoComplete;
import xbc.miniproject.com.xbcapplication.model.batch.getOne.Data;
import xbc.miniproject.com.xbcapplication.model.batch.getOne.ModelBatchAutoComplete;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class EditBatchActivity extends Activity {
    private Context context = this;

    private EditText editBatchEditTextName, editBatchEditTextPeriodForm,
            editBatchEditTextPeriodTo, editBatchEditTextRoom,
            editBatchEditTextNotes;

    private AutoCompleteTextView editBatchEditTextTechnology, editBatchEditTextTrainer;

    private Spinner spinnerBatchType;

    private Button editBatchButtonSave, editBatchButtonCancel;

    private RequestAPIServices apiServices;

    String idBatch;

    private boolean isNameSelected;
    private boolean isNameSelected2;

    String idBiodata;
    String idBiodata2;

    int id;

    String spinner;

    KArrayAdapter<DataList> adapter;
    KArrayAdapter<xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList> adapter2;

    private List<DataList> listBatch = new ArrayList<>();

    private List<xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList> listBatch2 = new ArrayList<>();

    String[] arrayType = {
            "- Pilih Type -","Gratis","Reguler"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_batch);
        idBatch = getIntent().getIntExtra("keyword", 0)+"";

        ActionBar actionBar = getActionBar();
        ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);

        editBatchEditTextTechnology = (AutoCompleteTextView) findViewById(R.id.editBatchEditTextTechnology);
        editBatchEditTextTechnology.setThreshold(1);
        editBatchEditTextTechnology.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected = true;
                editBatchEditTextTechnology.setError(null);

                xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList selected = (xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList) parent.getAdapter().getItem(position);
                int gg = selected.getId();
                idBiodata = gg+"";
                Toast.makeText(context,"idnya ini bos: "+gg,Toast.LENGTH_LONG).show();
            }
        });
        editBatchEditTextTechnology.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editBatchEditTextTechnology.getText().toString().trim().length() == 0){
                    editBatchEditTextTechnology.setError(null);
                } else{
                    isNameSelected = false;
                    editBatchEditTextTechnology.setError("Role must from the list!");
                }
                if(editBatchEditTextTrainer.getText().toString().trim().length() == 0){
                    editBatchEditTextTrainer.setError(null);
                } else{
                    isNameSelected2 = false;
                    editBatchEditTextTrainer.setError("Role must from the list!");
                }
//                isNameSelected = false;
//                editBatchEditTextTechnology.setError("Name must from the list!");
//                listBatch = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editBatchEditTextTechnology.getText().toString().trim().length() != 0){
                    String keyword = editBatchEditTextTechnology.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });

        editBatchEditTextTrainer = (AutoCompleteTextView) findViewById(R.id.editBatchEditTextTrainer);
        editBatchEditTextTrainer.setThreshold(1);
        editBatchEditTextTrainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected2 = true;
                editBatchEditTextTrainer.setError(null);

                xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList selected2 = (xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.DataList) parent.getAdapter().getItem(position);
                int jj = selected2.getId();
                idBiodata2 = jj+"";
                //Toast.makeText(context,"idnya ini bos: "+jj,Toast.LENGTH_LONG).show();
            }
        });

        editBatchEditTextTrainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameSelected2 = false;
                editBatchEditTextTrainer.setError("Name must from the list! ");
                listBatch2 = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editBatchEditTextTrainer.getText().toString().trim().length() != 0){
                    String keyword = editBatchEditTextTrainer.getText().toString().trim();
                    getAutoCompleteAPI2(keyword);
                }
            }
        });

        editBatchEditTextName = (EditText) findViewById(R.id.editBatchEditTextName);

        Calendar today = Calendar.getInstance();
        final int yearStart = today.get(Calendar.YEAR);
        final int monthStart = today.get(Calendar.MONTH);
        final int dayStart = today.get(Calendar.DATE);
        editBatchEditTextPeriodForm = (EditText) findViewById(R.id.editBatchEditTextPeriodForm);
        editBatchEditTextPeriodForm.setFocusable(false);
        editBatchEditTextPeriodForm.setClickable(true);
        editBatchEditTextPeriodForm.setOnClickListener(new View.OnClickListener() {
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

                        editBatchEditTextPeriodForm.setText(periodForm);
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

        editBatchEditTextPeriodTo = (EditText) findViewById(R.id.editBatchEditTextPeriodTo);
        editBatchEditTextPeriodTo.setFocusable(false);
        editBatchEditTextPeriodTo.setClickable(true);
        editBatchEditTextPeriodTo.setOnClickListener(new View.OnClickListener() {
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

                        editBatchEditTextPeriodTo.setText(periodTo);
                    }
                }, yearStart, monthStart, dayStart
                );
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.show();
            }
        });

        editBatchEditTextRoom = (EditText) findViewById(R.id.editBatchEditTextRoom);

        editBatchEditTextNotes = (EditText) findViewById(R.id.editBatchEditTextNotes);

        editBatchButtonSave = (Button) findViewById(R.id.editBatchButtonSave);
        editBatchButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        editBatchButtonCancel = (Button) findViewById(R.id.editBatchButtonCancel);
        editBatchButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = getIntent().getIntExtra("id", 0);
        getOneFromAPI(idBatch);
    }

    private void getOneFromAPI(String idBatch){
        apiServices = APIUtilities.getAPIServices();
        String contentType = "application/json";
        String tokenAuthorization = SessionManager.getToken(context);

        apiServices.getOneBatch(tokenAuthorization, id+"")
                .enqueue(new Callback<ModelBatchAutoComplete>() {
                    @Override
                    public void onResponse(Call<ModelBatchAutoComplete> call, Response<ModelBatchAutoComplete> response) {
                        if (response.code() == 200){
                            Data data = response.body().getData();
                            editBatchEditTextTechnology.setText(data.getTechnology().getName());
                            editBatchEditTextTrainer.setText(data.getTrainer().getName());
                            editBatchEditTextName.setText(data.getName());
                            editBatchEditTextPeriodForm.setText(data.getPeriodFrom());
                            editBatchEditTextPeriodTo.setText(data.getPeriodTo());
                            editBatchEditTextRoom.setText(data.getRoom());
//                            spinnerBatchType.setTextDirection(data.getBootcampType());
                            ArrayAdapter myAdap = (ArrayAdapter) spinnerBatchType.getAdapter();
                            int spinnerPosition = myAdap.getPosition(data.getBootcampType());
                            spinnerBatchType.setSelection(spinnerPosition);
                            editBatchEditTextNotes.setText(data.getNotes());
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelBatchAutoComplete> call, Throwable t) {
                        Toast.makeText(context, "Get One Monitoring onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getAutoCompleteAPI(String keyword){
        apiServices = APIUtilities.getAPIServices();
        apiServices.getAutoCompleteBatchTechnologyList2(SessionManager.getToken(context), keyword).enqueue(new Callback<ModelBatchTechnologyAutoComplete>() {
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
        editBatchEditTextTechnology.setAdapter(adapter);
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
        editBatchEditTextTrainer.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

    }

    private void inputValidation() {
        if (editBatchEditTextTechnology.getText().toString().trim().length() == 0){
            Toast.makeText(context, "Technology Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (editBatchEditTextTrainer.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Trainer Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (editBatchEditTextName.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Name Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (editBatchEditTextPeriodForm.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Period form Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (editBatchEditTextPeriodTo.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Period to Field still empty!", Toast.LENGTH_SHORT).show();
        } else if (editBatchEditTextRoom.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Room Field still empty!", Toast.LENGTH_SHORT).show();
        }
//        if (editBatchEditTextNotes.getText().toString().trim().length() == 0) {
//            Toast.makeText(context, "Notes Field still empty!", Toast.LENGTH_SHORT).show();
//        }
        else if(isNameSelected == false) {
            Toast.makeText(context,  "Role must from the list !", Toast.LENGTH_SHORT).show();
        }else if(isNameSelected2 == false) {
            Toast.makeText(context,  "Role must from the list !", Toast.LENGTH_SHORT).show();
        }else{
//            SaveSuccessNotification();
            callAPICreateBatch(editBatchEditTextTechnology.getText().toString(),
                    editBatchEditTextTrainer.getText().toString(),
                    editBatchEditTextName.getText().toString(),
                    editBatchEditTextPeriodForm.getText().toString(),
                    editBatchEditTextPeriodTo.getText().toString(),
                    editBatchEditTextRoom.getText().toString(),
                    spinner,
                    editBatchEditTextNotes.getText().toString());
        }
    }

    private void callAPICreateBatch(String technology, String trainer, String
            name, String periodFrom, String periodTo, String room, String bootcampType, String note){

        String contenType = Constanta.CONTENT_TYPE_API;
        String token = SessionManager.getToken(context);
        String JSON =  APIUtilities.generateEditBatch(id+"",idBiodata, idBiodata2, name, periodFrom, periodTo,
                room,bootcampType, note);
        RequestBody requestBody =  RequestBody.create(APIUtilities.mediaType(), JSON);
        apiServices = APIUtilities.getAPIServices();
//        DataList data = new DataList();
//        Technology data2 = new Technology();
//        Trainer data3 = new Trainer();
//        data.getTechnology().setName(editBatchEditTextTechnology.getText().toString());
//        data.getTrainer().setName(editBatchEditTextTrainer.getText().toString());
//        data.setName(editBatchEditTextName.getText().toString());
//        data.setPeriodFrom(editBatchEditTextPeriodForm.getText().toString());
//        data.setPeriodTo(editBatchEditTextPeriodTo.getText().toString());
//        data.setRoom(editBatchEditTextRoom.getText().toString());
//        data.setBootcampType(spinnerBatchType.getAdapter().toString()); //Belum ada getText
//        data.setNotes(editBatchEditTextNotes.getText().toString());


        apiServices.editBatch(contenType,token, requestBody)
                .enqueue(new Callback<ModelBatch>() {
                    @Override
                    public void onResponse(Call<ModelBatch> call, Response<ModelBatch> response) {
                        if(response.code() == 200){
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