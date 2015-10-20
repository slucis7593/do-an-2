package com.vuduc.android.worksoptimization;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vuduc.android.worksoptimization.model.TaskContent;
import com.vuduc.android.worksoptimization.model.TaskItem;
import com.vuduc.android.worksoptimization.util.DateTimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
        implements ItemDetailActivity.Callbacks {

    public static final String ARG_ITEM_ID = "item_id";

    @Bind(R.id.task_btn_deadline)
    Button mBtnDeadline;
    @Bind(R.id.task_et_task_detail)
    EditText mEtTaskDetail;
    @Bind(R.id.task_et_task_name)
    EditText mEtTaskName;
    @Bind(R.id.task_et_estimate_time_hour)
    EditText mEtEstimateTimeHour;
    @Bind(R.id.task_et_estimate_time_minute)
    EditText mEtEstimateTimeMinute;

    private TaskItem mTaskItem;

    // Temporary data
    private Long mDeadline;
    private long mTempDateDeadline;

    public ItemDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mTaskItem = TaskContent.ITEM_MAP.get(getArguments().getLong(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (toolbar != null) {
                if (mTaskItem != null)
                    toolbar.setTitle(mTaskItem.name != null ? mTaskItem.name : "");
            }
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
        mBtnDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(mDeadline));

                final int[] time = new int[] { cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) };

                DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                mTempDateDeadline = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();

                                com.wdullaer.materialdatetimepicker.time.TimePickerDialog
                                        .newInstance(new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                                mDeadline = mTempDateDeadline + (hourOfDay * 60 + minute) * 60 * 1000;
                                                mBtnDeadline.setText(DateTimeUtils.date2Text(mDeadline));
                                            }
                                        }, time[0], time[1], true)
                                        .show(getActivity().getFragmentManager(), "TimePickerDialog");
                            }
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
    }

    private void initUI() {
        // Show the dummy name as text in a TextView.
        if (mTaskItem != null) {
            mEtTaskName.setText(mTaskItem.name);
            mEtTaskDetail.setText(mTaskItem.details);

            int[] estimateTime = DateTimeUtils.millis2Time(mTaskItem.estimateTime);
            mEtEstimateTimeHour.setText(String.format("%02d", estimateTime[0]));
            mEtEstimateTimeMinute.setText(String.format("%02d", estimateTime[1]));

            mDeadline = mTaskItem.deadline;
            mBtnDeadline.setText(DateTimeUtils.date2Text(mTaskItem.deadline));
        }
    }

    private void saveData() {
        mTaskItem.name = mEtTaskName.getText().toString();
        mTaskItem.details = mEtTaskDetail.getText().toString();
        long hour = Long.parseLong(mEtEstimateTimeHour.getText() != null ? mEtEstimateTimeHour.getText().toString() : "0");
        long min = Long.parseLong(mEtEstimateTimeMinute.getText() != null ? mEtEstimateTimeMinute.getText().toString() : "0");
        mTaskItem.estimateTime = (hour * 60 + min) * 60 * 1000;
        mTaskItem.deadline = mDeadline;
    }

    @Override
    public boolean onSave(View v) {
        saveData();
        return true;
    }
}
