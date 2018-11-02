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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.model.role.DataListRole;
import xbc.miniproject.com.xbcapplication.model.role.ModelRole;
import xbc.miniproject.com.xbcapplication.model.user.DataList;
import xbc.miniproject.com.xbcapplication.model.user.ModelUser;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class AddUserActivity extends Activity {
    private Context context = this;
    private EditText addUserEditTexUsername,
            addUserEditTexPassword,
            addUserEditTexRetypePassword;
    private AutoCompleteTextView addUserEditTextRole;
    private Button addUserButtonSave;
    private Button addUserButtonCancel;
    private KArrayAdapter<DataListRole> adapter;
    private ArrayList<String> listUsername = new ArrayList<String>();
    private List<DataListRole> dataListRoles =  new ArrayList<>();
    private boolean isRoleSelected;
    private RequestAPIServices apiServices;
    private boolean isRegistered;
    int id1;
    private static final Pattern CEK_PASSWORD =
            Pattern.compile("^"+
                "(?=.*[a-z])"+
                "(?=.*[0-9])"+
                "(?=.*[A-Z])"+
                "(?=\\S+$)"+
                ".{4,}"+
                "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        ActionBar actionBar = getActionBar();
        ((ActionBar)actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add User");

        addUserEditTexUsername = (EditText) findViewById(R.id.addUserEditTexUsername);
        addUserEditTexPassword = (EditText) findViewById(R.id.addUserEditTexPassword);
        addUserEditTexRetypePassword = (EditText) findViewById(R.id.addUserEditTexRetypePassword);
        addUserEditTextRole =  (AutoCompleteTextView) findViewById(R.id.addUserEditTextRole);

        isRegistered = false;
        addUserEditTextRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addUserEditTextRole.setThreshold(1);
        addUserEditTextRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRoleSelected =  true;
                id1 =  position;
                addUserEditTextRole.setError(null);
                DataListRole selected =  (DataListRole) parent.getAdapter().getItem(position);
                int cariId= selected.getId();
            }
        });
        addUserEditTextRole.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isRoleSelected =  false;
                    getRole(addUserEditTextRole.getText().toString().trim());
                    dataListRoles = new ArrayList<>();
//               addUserEditTextRole.setError("Role Must from the list !");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(addUserEditTextRole.getText().toString().trim().length()!=0){
                    String keyword =  addUserEditTextRole.getText().toString().trim();
                    getRole(keyword);
                }
            }
        });
        addUserButtonSave  =  (Button) findViewById(R.id.addUserButtonSave);
        addUserButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataValidtaion(id1);
            }
        });
        addUserButtonCancel =  (Button) findViewById(R.id.addUserButtonCancel);
        addUserButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getRole(String keyword){
        String token = SessionManager.getToken(context);
        apiServices = APIUtilities.getAPIServices();
        apiServices.getListRole(token, keyword).enqueue(new Callback<ModelRole>() {
            @Override
            public void onResponse(Call<ModelRole> call, Response<ModelRole> response) {
                if(response.code()==200){
                        List<DataListRole> tmp = response.body().getDataList();
                        dataListRoles =  response.body().getDataList();
                        tampilkanData();
                    }
            }
            @Override
            public void onFailure(Call<ModelRole> call, Throwable t) {
                Toast.makeText(context, "List User onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void tampilkanData(){
        adapter = new KArrayAdapter<>(context, android.R.layout.simple_list_item_1, dataListRoles);
        addUserEditTextRole.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addDataValidtaion(final int position){
        if(addUserEditTextRole.getText().toString().trim().length()==0){
            Toast.makeText(context,"Role field still empty !", Toast.LENGTH_SHORT).show();
        }else if(addUserEditTexUsername.getText().toString().trim().length()==0){
            Toast.makeText(context,"Username field still empty !", Toast.LENGTH_SHORT).show();
        }else if(addUserEditTexUsername.getText().toString().trim().length()<8){
            Toast.makeText(context, "Username kurang dari 8 !", Toast.LENGTH_SHORT).show();
        }else if(addUserEditTexPassword.getText().toString().trim().length()==0){
            Toast.makeText(context, "Password field still empty !", Toast.LENGTH_SHORT).show();
        }else if(addUserEditTexRetypePassword.getText().toString().trim().length()== 0){
            Toast.makeText(context, "Please Retype password !", Toast.LENGTH_SHORT).show();
        }else if(isRoleSelected == false){
            Toast.makeText(context, "Role Must from the list!", Toast.LENGTH_SHORT).show();
        }else if(!CEK_PASSWORD.matcher(addUserEditTexPassword.getText().toString().trim()).matches()){
            Toast.makeText(context, "Password Kombinasi Huruf dan Angka", Toast.LENGTH_SHORT).show();
        }else{
            final String pas = addUserEditTexPassword.getText().toString();
            final String repas = addUserEditTexRetypePassword.getText().toString();
            if(pas.equalsIgnoreCase(repas)){
               // panggilValidasiUsername(addUserEditTexPassword.getText().toString().trim());
              //  if(isRegistered ==true){
                 //   Toast.makeText(context, "username sudah ada!", Toast.LENGTH_SHORT).show();
               // }else{
                    panggilAPI(position);
               // }
            }else{
                Toast.makeText(context, "Password Tidak Sama!", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    public void panggilValidasiUsername(String username){
//        apiServices = APIUtilities.getAPIServices();
//        apiServices.getListUsser().
//        isRegistered = false;
//
//    }

//    public void panggilValidasiChar(String data){
//        if(!CEK_PASSWORD.matcher(data).matches()){
//
//        }
//    }
    public void panggilAPI(final int position){
        DataList dataUser = new DataList();
        dataUser.setUsername(addUserEditTexUsername.getText().toString());
        dataUser.setPassword(addUserEditTexPassword.getText().toString());
        dataUser.setMRoleId(dataListRoles.get(position).getId());

        apiServices.createNewUser("application/json", dataUser)
                .enqueue(new Callback<ModelUser>() {
                    @Override
                    public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                        if(response.code()==201){
                            String message = response.body().getMessage();
                            if(message!=null){
                                saveDataNotification(message);
                            }else{
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
