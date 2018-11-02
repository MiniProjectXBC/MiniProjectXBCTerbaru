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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.idleNews.ModelIdleNews;
import xbc.miniproject.com.xbcapplication.model.idleNews.autoComplete.DataList;
import xbc.miniproject.com.xbcapplication.model.idleNews.autoComplete.ModelAutoCompleteIdleNews;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;


public class AddIdleNewsActivity extends Activity {
    private Context context = this;

    EditText addIdleNewsEditTextTitle,
            addIdleNewsEditTextContent;

    AutoCompleteTextView addIdleNewsEditTextCategory;

    Button addIdleNewsButtonSave,
            addIdleNewsButtonCancel;

    private boolean isNameSelected;

    KArrayAdapter<DataList> adapter;

    private List<DataList> listIdleNews = new ArrayList<>();

    RequestAPIServices apiServices;

    int idAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idle_news);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Input Idle News");

        addIdleNewsEditTextTitle = (EditText) findViewById(R.id.addIdleNewsEditTextTitle);
        addIdleNewsEditTextContent = (EditText) findViewById(R.id.addIdleNewsEditTextContent);

        addIdleNewsEditTextCategory = (AutoCompleteTextView) findViewById(R.id.addIdleNewsEditTextCategory);
        addIdleNewsEditTextCategory.setThreshold(1);

        addIdleNewsEditTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (addIdleNewsEditTextCategory.getText().toString().trim().length() != 0){
//                    addIdleNewsEditTextCategory.showDropDown();
//                }
            }
        });

        addIdleNewsEditTextCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isNameSelected = true;
                addIdleNewsEditTextCategory.setError(null);

                DataList selected = (DataList) adapterView.getAdapter().getItem(i);
                idAutoComplete = selected.getId();
//                Toast.makeText(context,"idnya ini bos: "+aidi,Toast.LENGTH_LONG).show();
            }
        });

        addIdleNewsEditTextCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isNameSelected = false;
                addIdleNewsEditTextCategory.setError("Category must from the list!");
                listIdleNews = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (addIdleNewsEditTextCategory.getText().toString().trim().length() != 0){
                    String keyword = addIdleNewsEditTextCategory.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });


        addIdleNewsButtonSave =(Button) findViewById(R.id.addIdleNewsButtonSave);
        addIdleNewsButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValidation();
            }
        });

        addIdleNewsButtonCancel = (Button) findViewById(R.id.addIdleNewsButtonCancel);
        addIdleNewsButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAutoCompleteAPI(String keyword) {
//        final ProgressDialog loading = LoadingClass.loadingAnimationAndText(context,
//                "Sedang Memuat Auto Complete . . .");
//        loading.show();

        apiServices = APIUtilities.getAPIServices();
        apiServices.idleNewsAutoComplete("application/json",SessionManager.getToken(context), keyword)
                .enqueue(new Callback<ModelAutoCompleteIdleNews>() {
                    @Override
                    public void onResponse(Call<ModelAutoCompleteIdleNews> call, Response<ModelAutoCompleteIdleNews> response) {
                        if (response.code() == 200) {
//                    loading.dismiss();
                            List<DataList> tmp = response.body().getDataList();
                            listIdleNews = response.body().getDataList();
                            getAutoCompletAdapter();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAutoCompleteIdleNews> call, Throwable t) {
//                loading.dismiss();

                    }
                });
    }

    private void getAutoCompletAdapter() {
        adapter = new KArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, listIdleNews);
        addIdleNewsEditTextCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void inputValidation() {
        if (addIdleNewsEditTextTitle.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Title Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(addIdleNewsEditTextCategory.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Category Field still empty!",Toast.LENGTH_SHORT).show();
        } else if (!isNameSelected) {
            addIdleNewsEditTextCategory.setText("");
            Toast.makeText(context, "Category Field Must From the List!", Toast.LENGTH_SHORT).show();
        } else {
//            SaveSuccessNotification();
            callAPICreateIdleNews(idAutoComplete +"", addIdleNewsEditTextTitle.getText().toString(), addIdleNewsEditTextCategory.getText().toString(), addIdleNewsEditTextContent.getText().toString());
        }
    }

    private void callAPICreateIdleNews(String idAutocomplete, String title, String category, String content) {

        String contentType = "application/json";
        String json = APIUtilities.generateAddIdleNewsMap(idAutocomplete, title, category, content);
        RequestBody bodyRequest = RequestBody.create(APIUtilities.mediaType(), json);
        apiServices = APIUtilities.getAPIServices();

        apiServices.createNewIdleNews("application/json", bodyRequest)
                .enqueue(new Callback<ModelIdleNews>() {
                    @Override
                    public void onResponse(Call<ModelIdleNews> call, Response<ModelIdleNews> response) {
                        if (response.code() == 201) {
                            String message = response.body().getMessage();
                            if (message!=null){
                                SaveSuccessNotification(context, message);
                            } else{
                                Toast.makeText(context,"Message Gagal Diambil", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelIdleNews> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void SaveSuccessNotification(final Context context, String message){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage(message+"!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setCancelable(false).show();
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
