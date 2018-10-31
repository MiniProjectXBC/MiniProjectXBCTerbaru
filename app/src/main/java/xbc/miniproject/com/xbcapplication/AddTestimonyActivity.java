package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import xbc.miniproject.com.xbcapplication.model.testimony.DataListTestimony;
import xbc.miniproject.com.xbcapplication.model.testimony.ModelTestimony;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;

public class AddTestimonyActivity extends Activity {
    private Context context = this;
    private EditText addTestimonyEditTexTitle,
            addTestimonyEditTexContent;
    private Button addTestimonyButtonSave,
            addTestimonyButtonCancel;
    private RequestAPIServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_testimony);
        ActionBar actionBar =  getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);
        addTestimonyEditTexTitle =  (EditText) findViewById(R.id.addTestimonyEditTextTitle);
        addTestimonyEditTexContent = (EditText) findViewById(R.id.addTestimonyEditTexContent);
        addTestimonyButtonSave = (Button) findViewById(R.id.addTestimonyButtonSave);
        addTestimonyButtonCancel = (Button) findViewById(R.id.addTestimonyButtonCancel);
        addTestimonyButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });
        addTestimonyButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void inputValidation(){
        if(addTestimonyEditTexTitle.getText().toString().trim().length()==0){
            Toast.makeText(context, "Title Field still Empty!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Testimony Successfully Added ! ", Toast.LENGTH_SHORT).show();
            //saveSuccesNotification();
            callAPICreateTestimony();
        }
    }
    private void callAPICreateTestimony() {
        apiServices = APIUtilities.getAPIServices();
        DataListTestimony data = new DataListTestimony();
        data.setTitle(addTestimonyEditTexTitle.getText().toString());
        data.setContent(addTestimonyEditTexContent.getText().toString());

        apiServices.createNewTestimony("application/json", data)
                .enqueue(new Callback<ModelTestimony>() {
                    @Override
                    public void onResponse(Call<ModelTestimony> call, Response<ModelTestimony> response) {
                        if (response.code() == 201) {
                            SaveSuccessNotification();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTestimony> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void SaveSuccessNotification(){
        final AlertDialog.Builder builder;
        builder=  new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage("Testimony Successfully Added !")
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
