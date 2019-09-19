package com.mag.taskmanager.Controller;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mag.taskmanager.Controller.Adapters.TaskViewPagerAdapter;
import com.mag.taskmanager.Model.TaskStatus;
import com.mag.taskmanager.R;
import com.mag.taskmanager.Util.UiUtil;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTaskPagerFragment extends Fragment {

    public static final int REQUEST_CODE_FOR_ADD_DIALOG = 100;
    public static final String DIALOG_ERROR = "dialog_error";
    public static final String ADD_TASK_FRAGMENT = "add_task_fragment";
    public static final String TO_DO = "To Do";
    public static final String DOING = "Doing";
    public static final String DONE = "Done";
    public static final String HAS_ERROR = "has_error";
    public static final String SAVE_RECYCLER_EXISTANCE = "save_recycler_existance";
    public static final String SAVE_TASK_LIST_FRAGMENT = "save_task_list_fragment_";
    public static final String SAVE_TASK_VIEW_PAGER_ADAPTER = "save_task_view_pager";
    public static final String SAVE_VIEW_PAGER_ADAPTER_EXISTANCE = "save_view_pager_adapter_existance";

    private HashMap<TaskStatus, Fragment> taskListFragments = new HashMap<>();
    private TaskViewPagerAdapter taskViewPagerAdapter;

    private ConstraintLayout mainLayout;
    private TabLayout statusTabLayout;
    private ViewPager taskViewPager;
    private FloatingActionButton fab;

    private boolean recyclerExistance;
    private boolean viewPagerAdapterExistance;


    public static MainTaskPagerFragment newInstance() {

        Bundle args = new Bundle();

        MainTaskPagerFragment fragment = new MainTaskPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainTaskPagerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerExistance = false;
        viewPagerAdapterExistance = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_task_pager, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_CODE_FOR_ADD_DIALOG:

                if (resultCode == Activity.RESULT_OK) {
                    if (data.getIntExtra(HAS_ERROR, 0) == 1)
                        UiUtil.showSnackbar(mainLayout, data.getStringExtra(DIALOG_ERROR), getResources().getString(R.color.task_app_red));
                    else {
                        ((TaskListFragment) taskListFragments.get(TaskStatus.TODO)).update();
                        UiUtil.showSnackbar(mainLayout, getResources().getString(R.string.successfully_added), getResources().getString(R.color.task_app_green_dark));
                    }
                }

                break;
            default:
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bundle Data
        if (savedInstanceState != null) {
            recyclerExistance = savedInstanceState.getBoolean(SAVE_RECYCLER_EXISTANCE);
            viewPagerAdapterExistance = savedInstanceState.getBoolean(SAVE_VIEW_PAGER_ADAPTER_EXISTANCE);
        }

        mainLayout = view.findViewById(R.id.pagerFragment_mainLayout);

        // Floating Action bar

        fab = view.findViewById(R.id.taskActivity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskFragment addTaskFragment = AddTaskFragment.newInstance();
                addTaskFragment.setTargetFragment(MainTaskPagerFragment.this, REQUEST_CODE_FOR_ADD_DIALOG);
                addTaskFragment.show(getFragmentManager(), ADD_TASK_FRAGMENT);
            }
        });


        // Tab Layout

        statusTabLayout = view.findViewById(R.id.pagerFragment_statusTabLayout);

        statusTabLayout.addTab(statusTabLayout.newTab().setText(TO_DO));
        statusTabLayout.addTab(statusTabLayout.newTab().setText(DOING));
        statusTabLayout.getTabAt(1).select();
        statusTabLayout.addTab(statusTabLayout.newTab().setText(DONE));

        statusTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                taskViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        // 3 Recycler
        if (recyclerExistance) {
            taskListFragments.put(TaskStatus.TODO, (TaskListFragment) savedInstanceState.getSerializable(SAVE_TASK_LIST_FRAGMENT + TaskStatus.TODO));
            taskListFragments.put(TaskStatus.DOING, (TaskListFragment) savedInstanceState.getSerializable(SAVE_TASK_LIST_FRAGMENT + TaskStatus.DOING));
            taskListFragments.put(TaskStatus.DONE, (TaskListFragment) savedInstanceState.getSerializable(SAVE_TASK_LIST_FRAGMENT + TaskStatus.DONE));
        } else {

            for (int i = 0; i < 3; i++) {

                TaskListFragment.setGetView(new TaskListFragment.GetViews() {
                    @Override
                    public void updateTaskList() {
                        for (Fragment fragment : taskListFragments.values())
                            ((TaskListFragment) fragment).update();
                    }
                });

                taskListFragments.put(TaskStatus.values()[i], TaskListFragment.newInstance(TaskStatus.values()[i]));

                recyclerExistance = true;
            }
        }


        // View Pager
        taskViewPager = view.findViewById(R.id.pagerFragment_viewPager);
//        if (viewPagerAdapterExistance) {
//            taskViewPagerAdapter = (TaskViewPagerAdapter) savedInstanceState.getSerializable(SAVE_TASK_VIEW_PAGER_ADAPTER);
//        } else {
            taskViewPagerAdapter = new TaskViewPagerAdapter(getFragmentManager(), taskListFragments);
//            viewPagerAdapterExistance = true;
//        }
        taskViewPager.setAdapter(taskViewPagerAdapter);
        taskViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                statusTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        taskViewPager.setCurrentItem(1);
        Log.d("view_pager", "hello");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SAVE_RECYCLER_EXISTANCE, recyclerExistance);
        outState.putBoolean(SAVE_VIEW_PAGER_ADAPTER_EXISTANCE, viewPagerAdapterExistance);

        outState.putSerializable(SAVE_TASK_VIEW_PAGER_ADAPTER, taskViewPagerAdapter);

        for (TaskStatus taskStatus : taskListFragments.keySet()) {
            outState.putSerializable(SAVE_TASK_LIST_FRAGMENT + taskStatus, (TaskListFragment) taskListFragments.get(taskStatus));
        }

    }


}
