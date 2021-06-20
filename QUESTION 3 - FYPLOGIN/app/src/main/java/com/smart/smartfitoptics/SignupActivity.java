package com.smart.smartfitoptics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    Button signUpBtn;
    EditText nameEditText;
    EditText emailEdiText;
    EditText phoneEditText;
    TextView dobTextView;
    EditText passwordEditText;
    EditText confPasswordEditText;
    DatePickerDialog.OnDateSetListener mDateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpBtn = findViewById(R.id.btnSignup);
        nameEditText = findViewById(R.id.nameEditText);
        emailEdiText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dobTextView = findViewById(R.id.dobEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confPasswordEditText = findViewById(R.id.confPasswordEditText);

        mDateListner = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date  = dayOfMonth+"/"+month+"/"+year;
                dobTextView.setText(date);
            }
        };

            dobTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Calendar cal = Calendar.getInstance();
                     int year = cal.get(Calendar.YEAR);
                     int month = cal.get(Calendar.MONTH);
                     int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            SignupActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog,
                            mDateListner,
                            year, month, day
                    );
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();






                }
            });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(nameEditText) | !isValidEmail(emailEdiText) | !isEmpty(phoneEditText)){
                    return;
                }

            if(confPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
                ProgressDialog dialog = ProgressDialog.show(SignupActivity.this, "",
                        "Logging In...", true);
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
                    String URL = Global.api_url+"/register";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", nameEditText.getText().toString());
                    jsonBody.put("email", emailEdiText.getText().toString());
                    jsonBody.put("phone", phoneEditText.getText().toString());
                    jsonBody.put("dob", dobTextView.getText().toString());
                    jsonBody.put("password", passwordEditText.getText().toString());


                    final String requestBody = jsonBody.toString();

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VOLLEY", response.toString());
                            try {
                                String error = response.getString("error");

                                if(error.equals("true")){
                                    Toast.makeText(getApplicationContext(), "Error: "+response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Success, You can now Login", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            else{
                Toast.makeText(getApplicationContext(), "Password and Confirm Password does not match", Toast.LENGTH_SHORT).show();
            }
            }


        });
    }

    public  boolean isEmpty(EditText editText){
        String value = editText.getText().toString();
        if(value.isEmpty()){
            editText.setError("Value Should not be empty!");
            return false;
        }
        else{
            return true;
        }

    }
    public final static boolean isValidEmail(EditText editText) {
        String target = editText.getText().toString();
        if (target == null){
            editText.setError("Value Should not be empty!");
            return false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()){
            editText.setError("Enter a Valid Email");
            return false;
        }
        else{
            return  true;
        }

    }
}