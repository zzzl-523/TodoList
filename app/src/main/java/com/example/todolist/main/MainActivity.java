package com.example.todolist.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.todolist.R;
import com.example.todolist.addEdit.AddEditActivity;
import com.example.todolist.room.MyDatabase;
import com.example.todolist.room.TodoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView main_rev;
    private FloatingActionButton main_fab;
    //adapter 가져오기
    private MainAdapter adapter;

    //intent num
    //public static final int intent_addEdit = 1001;

    @Override   //메뉴 연결
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("TODO APP");
        }


        main_rev = findViewById(R.id.main_rev);
        main_fab = findViewById(R.id.main_fab);
        adapter = new MainAdapter();

        //어댑터) 이걸 해줘야 보인다
        main_rev.setAdapter(adapter);
        main_rev.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        //어댑터) 추가
        final MyDatabase myDatabase = MyDatabase.getInstance(this);
        List<TodoItem> list_item = myDatabase.todoDao().getAllTodo();   //모든 투두 받아오기
        adapter.submitAll(list_item);



        //*****과제 : Intent 사용해서 화면 바꾸기
        main_fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Click", "Click"); //Todo
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("mode",1001);
                startActivity(intent);
            }
        });

    }

    @Override   //delete 메뉴
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){  //item id로 case 구분
            case R.id.menu_delete_all:
                //전체 삭제
                adapter.removeAll();
                MyDatabase myDatabase = MyDatabase.getInstance(this);
                myDatabase.todoDao().deleteAllTodo();
                break;
                /*
                * 1. removeAll
                * 2. deleteAll
                *                  */
            case R.id.menu_delete_selected:
                //선택 삭제 (check되지 않은 것들로 list를 만들고, 전체 삭제를 한 후 이 list로 바꿔준다)
                MyDatabase myDatabase1 = MyDatabase.getInstance(this);
                List<TodoItem> itemList = myDatabase1.todoDao().getAllTodo();
                List<TodoItem> newList = new ArrayList<>(); //이걸로 바꿔줄 예정

                for(int i=0; i<itemList.size(); i++){
                    if(adapter.checkItem(itemList.get(i))){ //check된 것들 삭제
                        myDatabase1 = MyDatabase.getInstance(this); //이건 항상 해줘야 한다
                        myDatabase1.todoDao().deleteTodo(itemList.get(i));
                    }
                    else{   //check안 된 것들 newList에 추가
                        newList.add(itemList.get(i));
                    }

                }
                adapter.removeAllItem(newList);
                break;

                /*
                * 1. itemlist getAll
                * 2. newList
                * 3. for문 checkItem
                * 4. removeAll(new)
                 */
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    //onrestart, onresume


    @Override
    protected void onRestart() {
        super.onRestart();
        MyDatabase myDatabase = MyDatabase.getInstance(this);
        adapter.submitAll(myDatabase.todoDao().getAllTodo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDatabase myDatabase = MyDatabase.getInstance(this);
        adapter.submitAll(myDatabase.todoDao().getAllTodo());
    }
}