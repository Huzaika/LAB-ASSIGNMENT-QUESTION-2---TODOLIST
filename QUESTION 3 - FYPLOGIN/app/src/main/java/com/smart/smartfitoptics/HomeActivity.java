package com.smart.smartfitoptics;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;


import com.smart.smartfitoptics.models.CurrentUser;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    TextView welcome;

    ArrayAdapter<String> arrayAdapter;

    Toolbar toolbar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar_custom);

        setSupportActionBar(toolbar);


//        ListView listView = findViewById(R.id.home_list);
//        List<String> mylist= new ArrayList<>();

//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
//
//        listView.setAdapter(arrayAdapter);




        welcome = findViewById(R.id.welcome);

        CurrentUser user = Paper.book().read("current_user");
        welcome.setText("Welcome "+ user.getName() +"!");


    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}