package com.smart.smartfitoptics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smart.smartfitoptics.models.CurrentUser;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    Button signupBtn;
    Button signInBtn;

    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupBtn = findViewById(R.id.btnSignup);
        signInBtn = findViewById(R.id.btnLogin);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Logging In...", true);
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    String URL = Global.api_url+"/login";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("email", emailEditText.getText().toString());
                    jsonBody.put("password", passwordEditText.getText().toString());
                    final String requestBody = jsonBody.toString();

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VOLLEY", response.toString());
                            try {
                                String error = response.getString("error");
                                Log.i("11111111111111111", error.toString());
                                if(error.equals("true")){
                                    Toast.makeText(getApplicationContext(), "Error: "+response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                   JSONObject data = response.getJSONArray("user").getJSONObject(0);


                                    CurrentUser user = new CurrentUser(data.getString("id"), data.getString("name"));

                                //    Paper.init(LoginActivity.this);
                                    Paper.book().write("current_user", user);


                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("name", user.getName());
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            Toast.makeText(getApplicationContext(), "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }


                    };

                    requestQueue.add(req);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

    }
}