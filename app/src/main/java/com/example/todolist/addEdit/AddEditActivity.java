package com.example.todolist.addEdit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.main.MainActivity;
import com.example.todolist.room.MyDatabase;
import com.example.todolist.room.TodoItem;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddEditActivity extends AppCompatActivity {

    //*입력값을 저장해야 한다!!*
    private TextInputLayout til_title, til_sDate, til_dDate, til_memo;
    private ImageButton ib_sDate, ib_dDate;
    //sDate와 dDate 구분하기
    private final int START_DATE = 0;
    private final int DUE_DATE = 1;

    private int mode;

    //edit
    private int id;
    private TodoItem selectItem;    //item 가져오기


    //menu 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        //과제 : mode를 가져오기 (intent에서 add, edit 각각에 부여하는 number)
        mode = getIntent().getIntExtra("mode", -1111); //mode 데이터 가져오기
        Log.d("myTag", new Integer(mode).toString()); //Todo


        ActionBar actionBar = getSupportActionBar();    //mode에 따라 actionBar 가져오기
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(actionBar!=null) {   //actionBar가 존재할 때만
            if (mode == 1001) {
                actionBar.setTitle("Add Todo");
            } else if (mode == 1002) {
                actionBar.setTitle("Edit Todo");
            }
        }


        //연결해주기
        til_title = findViewById(R.id.addEdit_til_title);
        til_sDate = findViewById(R.id.addEdit_til_start);
        til_dDate = findViewById(R.id.addEdit_til_due);
        til_memo = findViewById(R.id.addEdit_til_memo);

        ib_sDate = findViewById(R.id.addEdit_ibtn_start);
        ib_dDate = findViewById(R.id.addEdit_ibtn_due);

        if(mode == 1002) { //1001==Add, 1002==Edit
            //Todo: edit

            //과제 : ID 가져오기
            id = getIntent().getIntExtra("item_id", -1);

            if(id == -1){   //id가져오기 실패 (default== -1)
                Log.d("todo_id", "item id wrong");
                Toast.makeText(AddEditActivity.this, "item id wrong", Toast.LENGTH_SHORT).show();
            }
            else{   //정상작동 경우

                MyDatabase myDatabase = MyDatabase.getInstance(AddEditActivity.this);

                try{
                    selectItem = myDatabase.todoDao().getTodo(getIntent().getIntExtra("item_id", -1)); //id로 item 찾기
                }catch(Exception e){
                    e.printStackTrace();
                }

                til_title.getEditText().setText(selectItem.getTitle());
                til_sDate.getEditText().setText(selectItem.getStart());
                til_dDate.getEditText().setText(selectItem.getDue());
                til_memo.getEditText().setText(selectItem.getMemo());
            }

        }

        //ibtn (눌렀을 때 달력 나오기)
        ib_sDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showCalender(START_DATE); //고유 숫자
            }
        });
        ib_dDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(DUE_DATE);
            }
        });

    }
    public void showCalender(final int mode){ //달력 보여주기
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        final int mMonth = cal.get(Calendar.MONTH);
        int mDate = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(AddEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayofMonth) {   //Month 한자리일 때 0이 붙는 (09) 거를 위해 작업해주기
                String s_month = new Integer(month+1).toString();
                String s_date = new Integer(dayofMonth).toString();
                if(month<10){
                    s_month = "0" + new Integer(month+1).toString();
                }
                if(dayofMonth<10){
                    s_date = "0" + new Integer(dayofMonth).toString();
                }

                String date = year + " / " + s_month + " / " + s_date;
                if(mode == START_DATE){
                    til_sDate.getEditText().setText(date);

                }
                else if(mode == DUE_DATE){
                    til_dDate.getEditText().setText(date);
                }
            }
        }, mYear, mMonth, mDate).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                finish();
                break;
            case R.id.save_todo:
                Log.d("save","SAVE");
                String title = til_title.getEditText().getText().toString();
                String sDate = til_sDate.getEditText().getText().toString();
                String dDate = til_dDate.getEditText().getText().toString();
                String memo = til_memo.getEditText().getText().toString();

                //Error 처리
                if(title.equals("")){ //비었을 때
                    til_title.setError("필수요소입니다!");
                }else{
                    til_title.setError(null);
                }
                if(title.equals("")) { //비었을 때
                    til_sDate.setError("필수요소입니다!");
                }else{
                    til_sDate.setError(null);
                }
                if(title.equals("")) { //비었을 때
                    til_dDate.setError("필수요소입니다!");
                }else{
                    til_dDate.setError(null);
                }


                if(!title.equals("") && !sDate.equals("") && !dDate.equals("")) { //아무것도 비어있지 않을 때 Edit 해줄 수 있다
                    if(sDate.compareTo(dDate)>0){ //s-d>0 (sDate이 dDate보다 크다면)
                        til_sDate.setError("시작 날짜가 더 느려요!");
                        til_dDate.setError("끝나는 날짜가 더 빨라요!");
                    }else{  //정상일 때는 저장해주기
                        MyDatabase myDatabase = MyDatabase.getInstance(AddEditActivity.this);

                        if(mode == 1001){
                            //추가 (새로운 것 저장)
                            TodoItem todoItem = new TodoItem(title, sDate, dDate, memo);
                            myDatabase.todoDao().insertTodo(todoItem);
                            Toast.makeText(AddEditActivity.this, "저장 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else if(mode == 1002){
                            //todo수정
                            selectItem.setTitle(title);
                            selectItem.setStart(sDate);
                            selectItem.setDue(dDate);
                            selectItem.setMemo(memo);

                            myDatabase.todoDao().editTodo(selectItem);
                            Toast.makeText(AddEditActivity.this, "저장 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }
        }

        return super.onOptionsItemSelected(item);
    }
}