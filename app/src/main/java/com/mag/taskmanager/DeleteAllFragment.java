package com.mag.taskmanager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.mag.taskmanager.Controller.LoginFragment;
import com.mag.taskmanager.Controller.TaskActivity;
import com.mag.taskmanager.Model.Exception.EmptyFieldException;
import com.mag.taskmanager.Model.Repository;
import com.mag.taskmanager.Model.Task;
import com.mag.taskmanager.Model.TaskStatus;
import com.mag.taskmanager.Var.Constants;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteAllFragment extends DialogFragment {

    private MaterialButton yesBtn, noBtn;

    public static DeleteAllFragment newInstance(String username) {

        Bundle args = new Bundle();
        args.putString("arg_username", username);

        DeleteAllFragment fragment = new DeleteAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DeleteAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_all, container, false);
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delete_all, null, false);

        final String username = getArguments().getString("arg_username");

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.task_app_dark))));


        yesBtn = view.findViewById(R.id.deleteAllFragment_yes);
        noBtn = view.findViewById(R.id.deleteAllFragment_no);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Repository.getInstance().getUserByUsername("amin").clearTasks();
                dismiss();
                startActivity(TaskActivity.newIntent(getActivity(), "amin"));
                getActivity().finish();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return dialog;
    }

}
