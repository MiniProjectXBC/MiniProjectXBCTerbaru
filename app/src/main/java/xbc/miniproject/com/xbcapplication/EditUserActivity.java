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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.model.role.DataListRole;
import xbc.miniproject.com.xbcapplication.model.role.ModelRole;
import xbc.miniproject.com.xbcapplication.model.user.ModelUser;
import xbc.miniproject.com.xbcapplication.model.user.User;
import xbc.miniproject.com.xbcapplication.model.user.UserGetOne.Data;
import xbc.miniproject.com.xbcapplication.model.user.UserGetOne.ModelUserGetOne;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

;

public class EditUserActivity extends Activity {
    private Context context =  this;
    private EditText editUserEditTexUsername,
            editUserEditTexPassword,
            editUserEditTexRetypePassword;
    private AutoCompleteTextView editUserEditTextRole;
    private Button editUserButtonSave;
    private  Button editUserButtonCancel;
    private KArrayAdapter<DataListRole> adapter;
    private List<DataListRole> dataListRoles =  new ArrayList<>();
    private RequestAPIServices apiServices;
    int id1;
    private boolean isRoleSelected;
    private Integer cariID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ActionBar actionBar =  getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit User");

        editUserEditTexUsername = (EditText) findViewById(R.id.editUserEditTexUsername);
        editUserEditTexPassword = (EditText) findViewById(R.id.editUserEditTexPassword);
        editUserEditTexRetypePassword = (EditText) findViewById(R.id.editUserEditTexRetypePassword);
        editUserEditTextRole = (AutoCompleteTextView) findViewById(R.id.editUserEditTextRole);

        //getRole(editUserEditTextRole.getText().toString().trim());
//        getRole(editUserEditTextRole.getText().toString().trim());

//        final ArrayAdapter<String> adapter=  new ArrayAdapter<String>(this,
//                android.R.layout.select_dialog_item, listRole);
//        editUserEditTextRole.setThreshold(0);
//        editUserEditTextRole.setAdapter(adapter);


        editUserEditTextRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        editUserEditTextRole.setThreshold(1);
        editUserEditTextRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRoleSelected = true;
                //id1 = position;
                editUserEditTextRole.setError(null);
                DataListRole selected = (DataListRole) parent.getAdapter().getItem(position);
                cariID =  selected.getId();
               // Toast.makeText(context,"idnya ini bos: "+cariID,Toast.LENGTH_LONG).show();
            }
        });
        editUserEditTextRole.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editUserEditTextRole.getText().toString().trim().length()==0){
                    editUserEditTextRole.setError(null);
                }else{
                    isRoleSelected = false;
                    editUserEditTextRole.setError("Role must from the list!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editUserEditTextRole.getText().toString().length()!=0){
                    String keyword = editUserEditTextRole.getText().toString().trim();
                    getRole(keyword);
                }
            }
        });
        editUserButtonSave =  (Button) findViewById(R.id.editUserButtonSave);
        editUserButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editValidation(id1);
            }
        });
        editUserButtonCancel =  (Button) findViewById(R.id.editUserButtonCancel);
        editUserButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id1 = getIntent().getIntExtra("id",0);
        getOneUserAPI(id1);
    }

    private  void getOneUserAPI(final int id){
     apiServices = APIUtilities.getAPIServices();
     apiServices.getOneUser(id).enqueue(new Callback<ModelUserGetOne>() {
         @Override
         public void onResponse(Call<ModelUserGetOne> call, Response<ModelUserGetOne> response) {
             if(response.code()==200){
                 Data data = response.body().getData();
                 editUserEditTextRole.setText(data.getRole().getName());
                 editUserEditTexUsername.setText(data.getUsername());
                editUserEditTexPassword.setText(data.getPassword());
                editUserEditTexRetypePassword.setText(data.getPassword());
             }else{
                 Toast.makeText(context,"Get Role Gagal", Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onFailure(Call<ModelUserGetOne> call, Throwable t) {
             Toast.makeText(context,"Get Role Gagal", Toast.LENGTH_SHORT).show();
         }
     });
    }

    private void getRole(String keyword){
        String contenType = Constanta.CONTENT_TYPE_API;
        String token = SessionManager.getToken(context);
        apiServices = APIUtilities.getAPIServices();
        apiServices.getListRole( token, keyword).enqueue(new Callback<ModelRole>() {
            @Override
            public void onResponse(Call<ModelRole> call, Response<ModelRole> response) {
                if(response.code()==200){
                    List<DataListRole> tmp = response.body().getDataList();
                    dataListRoles =  response.body().getDataList();
                    tampilkadData();
                }
            }

            @Override
            public void onFailure(Call<ModelRole> call, Throwable t) {
                Toast.makeText(context, "List User onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void tampilkadData(){
        adapter  =  new KArrayAdapter<>(context, android.R.layout.simple_list_item_1, dataListRoles);
        editUserEditTextRole.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private  void editValidation(final int position){
        if(editUserEditTextRole.getText().toString().trim().length()==0){
            Toast.makeText(context,  "Role field still empty !", Toast.LENGTH_SHORT).show();
        }else if(editUserEditTexUsername.getText().toString().trim().length()==0){
            Toast.makeText(context,  "Username field still empty !", Toast.LENGTH_SHORT).show();
        }else if(editUserEditTexPassword.getText().toString().trim().length()==0){
            Toast.makeText(context,  "Password field still empty !", Toast.LENGTH_SHORT).show();
        }else if(editUserEditTexRetypePassword.getText().toString().trim().length()==0){
            Toast.makeText(context,  "Please Retype password !", Toast.LENGTH_SHORT).show();
        }else if(isRoleSelected == false){
            Toast.makeText(context,  "Role must from the list !", Toast.LENGTH_SHORT).show();
        }else{
            final String pas = editUserEditTexPassword.getText().toString();
            final String repas = editUserEditTexRetypePassword.getText().toString();
            if(pas.equalsIgnoreCase(repas)){
                panggilEditAPI(position);
            }else{
                Toast.makeText(context, "Pasword tidak sama!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void panggilEditAPI(final  int position){
        User data = new User();
        String contentType = Constanta.CONTENT_TYPE_API;
        String token =  SessionManager.getToken(context);
        data.setId(id1);
        data.setUsername(editUserEditTexUsername.getText().toString());
        data.setPassword(editUserEditTexPassword.getText().toString());
        data.setMRoleId(cariID+"");

        apiServices.editUser(contentType,token, data)
                .enqueue(new Callback<ModelUser>() {
                    @Override
                    public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                        if(response.code()==200){
                            String message = response.body().getMessage();
                            if(message!=null){
                                saveDataNotification(message);
                            }else {
                                saveDataNotification("Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelUser> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void saveDataNotification(String message){
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
