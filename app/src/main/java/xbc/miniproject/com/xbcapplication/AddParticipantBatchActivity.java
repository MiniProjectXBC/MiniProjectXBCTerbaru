package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.batch.ModelBatch;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.DataList;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.ModelBatchTechnologyAutoComplete;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddParticipantBatchActivity extends Activity {
    private Context context = this;
    private AutoCompleteTextView addParticipantBatchEditTextName;

    private Button addParticipantBatchButtonSave, addParticipantBatchButtonCancel;
    KArrayAdapter<DataList> adapter;
    private List<DataList> listBatch = new ArrayList<>();

    private RequestAPIServices apiServices;

    private boolean isNameSelected;
    String idBiodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant_batch);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Participant Batch");

        addParticipantBatchEditTextName = (AutoCompleteTextView) findViewById(R.id.addParticipantBatchEditTextName);
        addParticipantBatchEditTextName.setThreshold(1);
        addParticipantBatchEditTextName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isNameSelected = true;
                addParticipantBatchEditTextName.setError(null);

                DataList selected = (DataList) parent.getAdapter().getItem(position);
                int ff = selected.getId();
                idBiodata = ff + "";
                Toast.makeText(context, "idnya ini cuy" + ff, Toast.LENGTH_LONG).show();
            }
        });
        addParticipantBatchEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameSelected = false;
                addParticipantBatchEditTextName.setError("Name must from the list!");
                listBatch = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(addParticipantBatchEditTextName.getText().toString().trim().length() != 0){
                    String keyword = addParticipantBatchEditTextName.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });


        addParticipantBatchButtonSave = (Button) findViewById(R.id.addParticipantBatchButtonSave);
        addParticipantBatchButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editValidation();
            }
        });
        addParticipantBatchButtonCancel = (Button) findViewById(R.id.addParticipantBatchButtonCancel);
        addParticipantBatchButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAutoCompleteAPI(String keyword){
        apiServices = APIUtilities.getAPIServices();
        apiServices.getAutoCompleteBatchTechnologyList(SessionManager.getToken(context), keyword)
                .enqueue(new Callback<ModelBatchTechnologyAutoComplete>() {
                    @Override
                    public void onResponse(Call<ModelBatchTechnologyAutoComplete> call, Response<ModelBatchTechnologyAutoComplete> response3) {
                        if(response3.code() == 200) {
                            listBatch = response3.body().getDataList();
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
        addParticipantBatchEditTextName.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void editValidation(){
        if(addParticipantBatchEditTextName.getText().toString().trim().length()==0){
            Toast.makeText(context,"Participant Field still empty!",Toast.LENGTH_SHORT).show();
        }else{
//            saveSuccessfullyNotification();
            CallAPIAddParticipant(addParticipantBatchEditTextName.getText().toString());
        }
    }

    private void CallAPIAddParticipant(String technology){

        String contenType = Constanta.CONTENT_TYPE_API;
        String token = SessionManager.getToken(context);
        String JSON = APIUtilities.generateBatchAddParticipant(idBiodata);
        RequestBody requestBody = RequestBody.create(APIUtilities.mediaType(), JSON);
        apiServices = APIUtilities.getAPIServices();

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
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
