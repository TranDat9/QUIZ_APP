package com.example.exampratice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {
    RecyclerView testview;
    Toolbar toolbar;
    private  TestAdapter adapter;
    private Dialog prgressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testview = findViewById(R.id.test_recycler_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DataBase.g_catList.get(DataBase.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prgressDialog = new Dialog(TestActivity.this);
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("LOADING....");
        prgressDialog.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        testview.setLayoutManager(linearLayoutManager);

          DataBase.loadTestData(new MyCompleteListener() {
              @Override
              public void onSucces() {
                /*  DataBase.loadMyScores(new MyCompleteListener() {
                      @Override
                      public void onSucces() {
                          adapter = new TestAdapter(DataBase.g_testList);
                          testview.setAdapter(adapter);
                          prgressDialog.dismiss();
                      }

                      @Override
                      public void onFailure() {
                          prgressDialog.dismiss();
                          Toast.makeText(TestActivity.this,"wrong!",Toast.LENGTH_LONG).show();
                      }
                  });
*/
                  adapter = new TestAdapter(DataBase.g_testList);
                  testview.setAdapter(adapter);
                  prgressDialog.dismiss();
              }

              @Override
              public void onFailure() {
                  prgressDialog.dismiss();
                  Toast.makeText(TestActivity.this,"wrong!",Toast.LENGTH_LONG).show();
              }
          });







    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}