package com.vuduc.android.worksoptimization;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vuduc.android.worksoptimization.model.TaskContent;
import com.vuduc.android.worksoptimization.util.DateTimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment
        implements ItemDetailActivity.Callbacks,
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    public static final String ARG_ITEM_ID = "item_id";

    @Bind(R.id.task_btn_deadline)
    Button mBtnDeadline;
    @Bind(R.id.task_btn_estimate_time)
    Button mBtnEstimateTime;
    @Bind(R.id.task_et_task_detail)
    TextView mEtTaskDetail;
    @Bind(R.id.task_et_task_name)
    TextView mEtTaskName;

    private TaskContent.TaskItem mItem;

    // Temporary data
    private Long mEstimateTime;
    private Long mDeadline;

    public ItemDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = TaskContent.ITEM_MAP.get(getArguments().getLong(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            toolbar.setTitle(mItem.name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        ButterKnife.bind(this, view);

        initUI();
        initEvent();

        return view;
    }

    private void initEvent() {
        mBtnEstimateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] time = DateTimeUtils.millis2Time(mEstimateTime);
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog
                        .newInstance(ItemDetailFragment.this, time[0], time[1], true)
                        .show(getActivity().getFragmentManager(), "TimePickerDialog");
            }
        });

        mBtnDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(mDeadline));
                DatePickerDialog.newInstance(
                        ItemDetailFragment.this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
    }

    private void initUI() {
        // Show the dummy name as text in a TextView.
        if (mItem != null) {
            mEtTaskName.setText(mItem.name);
            mEtTaskDetail.setText(mItem.details);
            mEstimateTime = mItem.estimateTime;
            mBtnEstimateTime.setText(DateTimeUtils.hour2Text(mItem.estimateTime));
            mDeadline = mItem.deadline;
            mBtnDeadline.setText(DateTimeUtils.date2Text(mItem.deadline));
        }
    }

    private void saveData() {
        mItem.name = mEtTaskName.getText().toString();
        mItem.details = mEtTaskDetail.getText().toString();
        mItem.estimateTime = mEstimateTime;
        mItem.deadline = mDeadline;
    }

    @Override
    public boolean onSave(View v) {
        saveData();
        return true;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mEstimateTime = (hourOfDay * 60l + minute) * 60 * 1000;
        mBtnEstimateTime.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mDeadline = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
        mBtnDeadline.setText(DateTimeUtils.date2Text(mDeadline));
    }
}
