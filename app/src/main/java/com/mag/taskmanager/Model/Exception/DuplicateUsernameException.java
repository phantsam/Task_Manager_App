package com.mag.taskmanager.Model.Exception;

import androidx.annotation.Nullable;

public class DuplicateUsernameException extends Exception {

    @Nullable
    @Override
    public String getMessage() {
        return "This user already exists.";
    }

}