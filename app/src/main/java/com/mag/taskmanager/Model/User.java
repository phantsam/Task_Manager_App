package com.mag.taskmanager.Model;

import android.view.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID id;
    private String username;
    private String password;
    private List<Task> tasks;

    public User(String username, String password, List<Task> tasks) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.tasks = tasks;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getTaskByStatus(TaskStatus value) {
        List<Task> speceficTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskStatus() == value)
                speceficTasks.add(task);
        }
        return speceficTasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTaskWithId(UUID uuid) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId() == uuid){
                tasks.remove(i);
                break;
            }
        }
    }

    public void updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId() == task.getTaskId()){
                tasks.get(i).setTitle(task.getTitle());
                tasks.get(i).setDescription(task.getDescription());
                tasks.get(i).setDate(task.getDate());
                tasks.get(i).setTaskStatus(task.getTaskStatus());
                break;
            }
        }
    }


}
