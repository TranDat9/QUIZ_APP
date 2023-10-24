package com.example.exampratice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.exampratice.DataBase.g_catList;
import static com.example.exampratice.DataBase.loadquestions;

public class StartTestActivity extends AppCompatActivity {
    TextView catName , testNo,totalQ,bestScore , time   ;
    Button startTestB;
    ImageView backB;
    private Dialog prgressDialog;
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        addControls();

        prgressDialog = new Dialog(StartTestActivity.this);
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("LOADING....");
        prgressDialog.show();

        loadquestions(new MyCompleteListener() {
            @Override
            public void onSucces() {
                  setData();
                  prgressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                prgressDialog.dismiss();
                Toast.makeText(StartTestActivity.this,"wrong!",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setData() {
           catName.setText(g_catList.get(DataBase.g_selected_test_index).getName());
           testNo.setText("Test No."+(DataBase.g_selected_test_index+1) );
           totalQ.setText(String.valueOf(DataBase.g_quesList.size()));
           bestScore.setText(String.valueOf(DataBase.g_testList.get(DataBase.g_selected_test_index).getTopScore()));
           time.setText(String.valueOf(DataBase.g_testList.get(DataBase.g_selected_test_index).getTime()));

    }


    private void addControls() {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);

        startTestB = findViewById(R.id.start_testB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });
        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}