package com.example.exampratice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static com.example.exampratice.DataBase.ANSWERED;
import static com.example.exampratice.DataBase.NOT_VISITED;
import static com.example.exampratice.DataBase.REVIEW;
import static com.example.exampratice.DataBase.UNANSWERED;
import static com.example.exampratice.DataBase.g_catList;
import static com.example.exampratice.DataBase.g_quesList;
import static com.example.exampratice.DataBase.g_selected_cat_index;
import static com.example.exampratice.DataBase.g_selected_test_index;
import static com.example.exampratice.DataBase.g_testList;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesID,timerTV , catNameTV;
    private Button submitB , markB , clearSelB;
    private ImageButton prevQuesB , nextQuesB;
    private ImageView quesListB;
    private int quesID;
    QuestionsAdapter questionsAdapter;
    private DrawerLayout drawer;
    private ImageButton drawerCloseB;
    private GridView quesListGV;
    private  ImageView markImage;
    QuestionGridAdapter gridAdapter;
    CountDownTimer timer;
    long timeLeft;

    private ImageView bookmarkB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);
        addControls();
         questionsAdapter =new QuestionsAdapter(g_quesList);
        questionsView.setAdapter(questionsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        gridAdapter = new QuestionGridAdapter(this,g_quesList.size());
        quesListGV.setAdapter(gridAdapter);

        setSnapHelper();
        setClickListeners();
        startTimer();


    }
    public void goToQuestion(int posion)
    {
        questionsView.smoothScrollToPosition(posion);
        if(drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
    }

    private void startTimer() {
        long totalTime  = g_testList.get(g_selected_test_index).getTime()*60*1000;
        timer = new CountDownTimer(totalTime+1000,1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                 String time = String.format("%02d:%02d min",
                         TimeUnit.MILLISECONDS.toMinutes(l),
                         TimeUnit.MILLISECONDS.toSeconds(l)-
                                 TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                         );
                 timerTV.setText(time);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class );

                long totalTime = g_testList.get(g_selected_test_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN",totalTime - timeLeft);

                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        };
            timer.start();
    }

    private void setClickListeners() {
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesID>0)
                {
                    questionsView.smoothScrollToPosition(quesID-1);
                }
            }
        });

        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesID<g_quesList.size()-1)
                {
                    questionsView.smoothScrollToPosition(quesID+1);
                }
            }
        });
        clearSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                g_quesList.get(quesID).setSelectedAns(-1);
                g_quesList.get(quesID).setStatus(UNANSWERED);
                markImage.setVisibility(View.GONE);
               questionsAdapter.notifyDataSetChanged();
            }
        });
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();
            }


        });
        quesListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawer.isDrawerOpen(GravityCompat.END))
                {
                    gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        drawerCloseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });
        markB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markImage.getVisibility()!=View.VISIBLE)
                {
                    markImage.setVisibility(View.VISIBLE);
                    g_quesList.get(quesID).setStatus(REVIEW);
                }
                else {
                    markImage.setVisibility(View.GONE);
                    if(g_quesList.get(quesID).getSelectedAns()!=-1)
                    {
                        g_quesList.get(quesID).setStatus(ANSWERED);
                    }
                    else {
                        g_quesList.get(quesID).setStatus(UNANSWERED);
                    }
                }
            }
        });
    bookmarkB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            addToBookmark();
        }
    });
    }

    private void addToBookmark() {
          if(g_quesList.get(quesID).isBookmarked())
          {
              g_quesList.get(quesID).setBookmarked(false);
              bookmarkB.setImageResource(R.drawable.ic_bookmark);
          }
          else {
              g_quesList.get(quesID).setBookmarked(true);
              bookmarkB.setImageResource(R.drawable.ic_bookmark_selected);
          }
    }

    private void submitTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);

        Button cancelB = view.findViewById(R.id.cancelB);
        Button comfirmB = view. findViewById(R.id.confirmB);
        builder.setView(view);
        AlertDialog alertDialog   = builder.create();
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        comfirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();

                alertDialog.dismiss();
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class );
                long totalTime = g_testList.get(g_selected_test_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN",totalTime - timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        });
         alertDialog.show();
    }
    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);
        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                if(g_quesList.get(quesID).getStatus()==NOT_VISITED)
                    g_quesList.get(quesID).setStatus(UNANSWERED);

                if(g_quesList.get(quesID).getStatus()==REVIEW)
                {
                    markImage.setVisibility(View.VISIBLE);
                }
                else {
                    markImage.setVisibility(View.GONE);
                }

                tvQuesID.setText(String.valueOf(quesID +1)+"/"+String.valueOf(g_quesList.size()));

                if(g_quesList.get(quesID).isBookmarked())
                {
                    bookmarkB.setImageResource(R.drawable.ic_bookmark_selected);
                }
                else {
                    bookmarkB.setImageResource(R.drawable.ic_bookmark);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void addControls() {
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        submitB = findViewById(R.id.submitB);
        markB = findViewById(R.id.markB);
        clearSelB = findViewById(R.id.clear_selB);
        prevQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
        quesListB = findViewById(R.id.ques_list_gridB);
        drawer= findViewById(R.id.drawer_layout);
        drawerCloseB = findViewById(R.id.drawerCloseB);
        quesListGV = findViewById(R.id.ques_list_gv);
        markImage = findViewById(R.id.mark_image);
        bookmarkB = findViewById(R.id.qa_bookmarkB);

        quesID = 0;
        tvQuesID.setText("1/"+String.valueOf(g_quesList.size()));
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName());
        g_quesList.get(0).setStatus(UNANSWERED);

        if(g_quesList.get(0).isBookmarked())
        {
             bookmarkB.setImageResource(R.drawable.ic_bookmark_selected);
        }
        else {
            bookmarkB.setImageResource(R.drawable.ic_bookmark);
        }
    }
}