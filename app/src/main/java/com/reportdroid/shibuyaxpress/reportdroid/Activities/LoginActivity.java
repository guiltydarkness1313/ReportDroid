package com.reportdroid.shibuyaxpress.reportdroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.reportdroid.shibuyaxpress.reportdroid.Models.User;
import com.reportdroid.shibuyaxpress.reportdroid.R;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiService;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiServiceGenerator;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button login,register;
    EditText editUsername,editPassword;

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.btnLogin);
        register=findViewById(R.id.btnRegister);
        editUsername=findViewById(R.id.txt_username);
        editPassword=findViewById(R.id.txt_password);

    }

    public void Login(View view){
        String username=editUsername.getText().toString();
        String password=editPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nombre y Precio son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService service= ApiServiceGenerator.createService(ApiService.class);

        Call<User> call=null;
        RequestBody usernamePart=RequestBody.create(MultipartBody.FORM,username);
        RequestBody passwordPart=RequestBody.create(MultipartBody.FORM,password);
        call=service.Login(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {


                        User responseMessage = response.body();
                        assert responseMessage != null;
                        User.getInstance().setId(responseMessage.getId());
                        User.getInstance().setEmail(responseMessage.getEmail());
                        User.getInstance().setProfile(responseMessage.getProfile());
                        User.getInstance().setUsername(responseMessage.getUsername());
                        Log.d(TAG, "responseMessage: " + responseMessage);
                        Log.d(TAG, "responseMessage: "+ responseMessage);

                        Toast.makeText(LoginActivity.this, "exito", Toast.LENGTH_LONG).show();
                        Intent launcher=new Intent(LoginActivity.this,MenuActivity.class);
                        startActivity(launcher);


                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable ignored) {
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Register(View view){
        Intent launcher=new Intent(this,RegisterActivity.class);
        startActivityForResult(launcher,1);
    }
}
