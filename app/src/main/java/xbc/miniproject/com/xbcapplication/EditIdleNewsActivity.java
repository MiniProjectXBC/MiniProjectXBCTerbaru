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

public class EditIdleNewsActivity extends Activity {
    private Context context = this;

    EditText editIdleNewsEditTextTitle,
            editIdleNewsEditTextContent;

    AutoCompleteTextView editIdleNewsEditTextCategory;

    Button editIdleNewsButtonSave,
            editIdleNewsButtonCancel;

    private boolean isNameSelected;

    KArrayAdapter<DataList> adapter;

    private List<DataList> listIdleNews = new ArrayList<>();

    RequestAPIServices apiServices;

    int idAutocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idle_news);

        String title, category, content;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Idle News");

        editIdleNewsEditTextTitle = (EditText) findViewById(R.id.editIdleNewsEditTextTitle);
        editIdleNewsEditTextContent = (EditText) findViewById(R.id.editIdleNewsEditTextContent);

        editIdleNewsEditTextCategory = (AutoCompleteTextView) findViewById(R.id.editIdleNewsEditTextCategory);
        editIdleNewsEditTextCategory.setThreshold(1);

        editIdleNewsEditTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (addIdleNewsEditTextCategory.getText().toString().trim().length() != 0){
//                    addIdleNewsEditTextCategory.showDropDown();
//                }
            }
        });

        editIdleNewsEditTextCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isNameSelected = true;
                editIdleNewsEditTextCategory.setError(null);

                DataList selected = (DataList) adapterView.getAdapter().getItem(i);
                idAutocomplete = selected.getId();
//                Toast.makeText(context,"idnya ini bos: "+aidi,Toast.LENGTH_LONG).show();
            }
        });

        editIdleNewsEditTextCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isNameSelected = false;
                editIdleNewsEditTextCategory.setError("Category must from the list!");
                listIdleNews = new ArrayList<>();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editIdleNewsEditTextCategory.getText().toString().trim().length() != 0){
                    String keyword = editIdleNewsEditTextCategory.getText().toString().trim();
                    getAutoCompleteAPI(keyword);
                }
            }
        });

        editIdleNewsButtonSave =(Button) findViewById(R.id.editIdleNewsButtonSave);
        editIdleNewsButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValidation();
            }
        });
        editIdleNewsButtonCancel = (Button) findViewById(R.id.editIdleNewsButtonCancel);
        editIdleNewsButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle data = getIntent().getExtras();
        if (data == null){
            title = null;
            category = null;
//            content = null;
        }
        else {
            title = data.getString("title");
            category = data.getString("category");
//            content = data.getString("content");
        }
        editIdleNewsEditTextTitle.setText(title);
        editIdleNewsEditTextCategory.setText(category);
//        editIdleNewsEditTextContent.setText(content);
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
        editIdleNewsEditTextCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void inputValidation() {
        if (editIdleNewsEditTextTitle.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Title Field still empty!",Toast.LENGTH_SHORT).show();
        } else if(editIdleNewsEditTextCategory.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Category Field still empty!",Toast.LENGTH_SHORT).show();
        } else if (!isNameSelected) {
            editIdleNewsEditTextCategory.setText("");
            Toast.makeText(context, "Category Field Must From the List!", Toast.LENGTH_SHORT).show();
        } else {
            inputEditIdleNewsAPI(idAutocomplete+"", editIdleNewsEditTextTitle.getText().toString(), editIdleNewsEditTextCategory.getText().toString(), editIdleNewsEditTextContent.getText().toString());
        }
    }

    private void inputEditIdleNewsAPI(String idAutocomplete, String title, String category, String content) {

        String contentType = "application/json";
        String json = APIUtilities.generateIdleNewsMap(idAutocomplete, title, category, content);
        RequestBody bodyRequest = RequestBody.create(APIUtilities.mediaType(), json);
        apiServices = APIUtilities.getAPIServices();

        apiServices.editIdleNews("application/json", SessionManager.getToken(context), bodyRequest)
                .enqueue(new Callback<ModelIdleNews>() {
                    @Override
                    public void onResponse(Call<ModelIdleNews> call, Response<ModelIdleNews> response) {
                        if (response.code() == 200) {
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