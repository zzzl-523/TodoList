package com.example.todolist.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//todoItem은 entity니까 필요한 요소들 만들어주기
@Entity(tableName = "TODO")
public class TodoItem implements Comparable<TodoItem>{
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "checked")
    private boolean checked;
    @ColumnInfo(name = "start")
    private String start;
    @ColumnInfo(name = "due")
    private String due;
    @ColumnInfo(name = "memo")
    private String memo;

    public TodoItem(String title, String start, String due, String memo) {
        this.title = title;
        this.start = start;
        this.due = due;
        this.memo = memo;
        checked = false; //처음엔 비어있기
    }

    //Getter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getStart() {
        return start;
    }

    public String getDue() {
        return due;
    }

    public String getMemo() {
        return memo;
    }

    //Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public int compareTo(TodoItem item) {
        return this.due.compareTo(item.due);
    }
}
