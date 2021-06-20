package com.smart.smartfitoptics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    TextView loadingText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingText = findViewById(R.id.loadingText);


        Paper.init(this);







        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second
        final int[] time = {1};

        handler.postDelayed(new Runnable() {
            public void run() {
                time[0]++;
                loadingText.setText(loadingText.getText()+".");
                if(time[0] == 4){

                    if( Paper.book().contains("current_user")){

                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }

                }
                handler.postDelayed(this, delay);

            }
        }, delay);








    }
}