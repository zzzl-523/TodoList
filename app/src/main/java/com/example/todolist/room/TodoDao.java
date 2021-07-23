package com.example.todolist.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insertTodo(TodoItem item);
    @Delete
    void deleteTodo(TodoItem item);
    @Query("delete FROM TODO")
    void deleteAllTodo();
    @Update
    void editTodo(TodoItem item);
    @Query("select * FROM TODO WHERE id = :id")
    TodoItem getTodo(int id);

    @Query("select * FROM TODO")
    List<TodoItem> getAllTodo();
}
