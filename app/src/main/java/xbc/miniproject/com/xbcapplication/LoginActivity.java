package xbc.miniproject.com.xbcapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.login.ModelLoginMessage;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class LoginActivity extends Activity {
    private Context context = this;

    private EditText inputUsername, inputPassword;
    private Button button;

    private RequestAPIServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.userName);
        inputPassword = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.loginButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLogin();
            }
        });
    }

    private void submitLogin() {
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(context, getResources().getString(R.string.userNameEmpty), Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(context, getResources().getString(R.string.passwordEmpty), Toast.LENGTH_SHORT).show();
        } else {
            goLoginApi(username, password);
        }
    }

    private void goLoginApi(final String username, String password) {
        String json = APIUtilities.generateLoginMap(username, password);
        RequestBody requestBody = RequestBody.create(APIUtilities.mediaType(), json);

        apiServices = APIUtilities.getAPIServices();
        apiServices.goLogin(Constanta.CONTENT_TYPE_API, requestBody).enqueue(new Callback<ModelLoginMessage>() {
            @Override
            public void onResponse(@NonNull Call<ModelLoginMessage> call, @NonNull Response<ModelLoginMessage> response) {
                if (response.code() == 200) {
                    if (response.body().getGeneratedToken() != null) {
                        SessionManager.saveLoginData(context,
                                username,
                                response.body().getGeneratedToken(),
                                true);

                        Intent intent = new Intent(context, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(context, "Login User Gagal : Pastikan username & password yang anda masukkan benar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelLoginMessage> call, @NonNull Throwable t) {
                Toast.makeText(context, "Login User onFailure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
    