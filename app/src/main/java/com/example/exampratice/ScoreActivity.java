package com.example.exampratice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
   private TextView scoreTV , timeTV , totalQTV ,   correctQTV , wrongQTV , unattemptedQTV;
   Button leaderB , reAttemptB , viewAnsB;
   private long timeTaken;
   TextView dialogText;
   Dialog prgressDialog;

    int finalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prgressDialog = new Dialog(ScoreActivity.this);
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("LOADING....");
        prgressDialog.show();

        addControls();
        loadData();

        setBookMarks();

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(ScoreActivity.this,AnswersActivity.class);
              startActivity(intent);
            }
        });
  reAttemptB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          reAttempt();
      }
  });

    saveResult();



    }

    private void setBookMarks() {
        for(int i =0 ; i<DataBase.g_quesList.size();i++)
        {
            QuestionModel qustion = DataBase.g_quesList.get(i);
            if(qustion.isBookmarked())
            {
                if(!DataBase.g_bmIdList.contains(qustion.getqID()))
                {
                    DataBase.g_bmIdList.add(qustion.getqID());
                    DataBase.myProfile.setBookmarksCount(DataBase.g_bmIdList.size());
                }
            }
            else {
                if(DataBase.g_bmIdList.contains(qustion.getqID()))
                {
                    DataBase.g_bmIdList.remove(qustion.getqID());
                    DataBase.myProfile.setBookmarksCount(DataBase.g_bmIdList.size());
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
             ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveResult() {
          DataBase.saveResult(finalScore, new MyCompleteListener() {
              @Override
              public void onSucces() {
                  prgressDialog.dismiss();
              }

              @Override
              public void onFailure() {
                  Toast.makeText(ScoreActivity.this,"somthing wrong!!",Toast.LENGTH_LONG);
                  prgressDialog.dismiss();
              }
          });
    }

    private void reAttempt() {
           for(int i = 0 ;i <DataBase.g_quesList.size(); i++ )
           {
               DataBase.g_quesList.get(i).setSelectedAns(-1);
               DataBase.g_quesList.get(i).setStatus(DataBase.NOT_VISITED);
           }
        Intent intent = new Intent(ScoreActivity.this,StartTestActivity.class);
           startActivity(intent);
           finish();
    }

    private void loadData() {
        int correctQ = 0 , wrongQ = 0 , unattemptQ =0 ;
        for(int i=0 ; i< DataBase.g_quesList.size();i++)
        {
            if(DataBase.g_quesList.get(i).getSelectedAns() == -1)
            {
                unattemptQ++;
            }
            else {
                if(DataBase.g_quesList.get(i).getSelectedAns()== DataBase.g_quesList.get(i).getCorrectAns())
                {
                    correctQ++;
                }
                else {
                    wrongQ++;
                }
            }
        }

        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptQ));

        totalQTV.setText(String.valueOf(DataBase.g_quesList.size()));

         finalScore = (correctQ *100) / DataBase.g_quesList.size();
        scoreTV.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);
        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );
        timeTV.setText(time);
    }

    private void addControls() {
        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.time);
        totalQTV = findViewById(R.id.totalQ);
        wrongQTV = findViewById(R.id.wrongQ);
        correctQTV = findViewById(R.id.correctQ);
        unattemptedQTV = findViewById(R.id.un_attemptedQ);
        leaderB = findViewById(R.id.leaderB);
        reAttemptB = findViewById(R.id.reattemptB);
        viewAnsB = findViewById(R.id.view_answerB);

    }
}