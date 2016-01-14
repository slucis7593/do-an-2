package com.vuduc.android.worksoptimization;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vuduc.android.worksoptimization.model.TaskContent;
import com.vuduc.android.worksoptimization.model.TaskItem;
import com.vuduc.android.worksoptimization.util.DateTimeUtils;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity
        implements ItemListFragment.Callbacks {

    private boolean mTwoPane;
    private FloatingActionButton mDeleteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setSubtitle("Ngày sắp xếp");

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.fab_add);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = System.currentTimeMillis();
                TaskContent.addItem(new TaskItem(id));
                onItemSelected(id);
            }
        });

        mDeleteFab = (FloatingActionButton) findViewById(R.id.fab_delete);
        mDeleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteFab.isActivated()) {
                    mDeleteFab.setActivated(false);
                    mDeleteFab.setImageResource(android.R.drawable.ic_delete);
                } else {
                    mDeleteFab.setActivated(true);
                    mDeleteFab.setImageResource(android.R.drawable.ic_menu_delete);
                    Snackbar.make(findViewById(android.R.id.content),
                            "Chọn item để xóa. Nhấn lần nữa để kết thúc",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        final Button btnChart = (Button) findViewById(R.id.task_list_btn_chart);
        btnChart.setVisibility(View.GONE);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemListActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        Button btnOptimization = (Button) findViewById(R.id.task_list_btn_optimization);
        btnOptimization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChart.setVisibility(View.VISIBLE);
                ((ItemListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_list)).onOptimization();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;

            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public void onItemSelected(Long id) {
        if (mDeleteFab.isActivated()) {
            TaskContent.deleteItem(id);
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list)).updateUI();
        } else {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putLong(ItemDetailFragment.ARG_ITEM_ID, id);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Intent detailIntent = new Intent(this, ItemDetailActivity.class);
                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void onFinishOptimization(Integer integer) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle("Ngày sắp xếp: " + DateTimeUtils.date2Text(System.currentTimeMillis()));
    }
}
