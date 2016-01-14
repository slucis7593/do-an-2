package com.vuduc.android.worksoptimization;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vuduc.android.worksoptimization.model.TaskContent;
import com.vuduc.android.worksoptimization.model.TaskItem;
import com.vuduc.android.worksoptimization.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private static Callbacks sTaskCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Long id) {

        }

        @Override
        public void onFinishOptimization(Integer integer) {

        }
    };

    private Callbacks mCallbacks = sTaskCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;

    private TaskListAdapter mTaskListAdapter;
    private AlertDialog mAlertDialog;

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskListAdapter = new TaskListAdapter(TaskContent.ITEMS);
        setListAdapter(mTaskListAdapter);

        CharSequence[] items = new CharSequence[]{"Sắp xếp tối đa số lượng công việc", "Sắp xếp tối đa giá trị công việc", "Hủy"};
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Loại sắp xếp")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                onOptimizationByCount();
                                break;
                            case 1:
                                onOptimizationByValue();
                                break;
                            default:
                                mAlertDialog.dismiss();
                                break;
                        }
                    }
                }).create();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement its callbacks.
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sTaskCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(TaskContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void updateUI() {
        mTaskListAdapter.notifyDataSetChanged();
    }

    public void onOptimization() {
        mAlertDialog.show();
    }

    private void onOptimizationByValue() {
        ScheduleOptimizationByValue optimizationByValue = new ScheduleOptimizationByValue(TaskContent.ITEMS);
        ArrayList<TaskItem> optimizationTasks = optimizationByValue.run();
        TaskContent.setItems(optimizationTasks);
        updateUI();
        mCallbacks.onFinishOptimization(optimizationByValue.mNumberSuccessfulItems);
    }

    private void onOptimizationByCount() {
        ScheduleOptimization optimization = new ScheduleOptimization(TaskContent.ITEMS);
        optimization.run();
        TaskContent.mNumberSuccessfulItems = optimization.mNumberSuccessfulItems;
        mTaskListAdapter.notifyDataSetChanged();
        mCallbacks.onFinishOptimization(optimization.mNumberSuccessfulItems);

        //new OptimizationTask().execute(TaskContent.ITEMS);
    }

    public interface Callbacks {
        void onItemSelected(Long id);

        void onFinishOptimization(Integer numberSuccessfulItems);
    }

    class OptimizationTask extends AsyncTask<ArrayList<TaskItem>, Void, Integer> {
        @SafeVarargs
        @Override
        protected final Integer doInBackground(ArrayList<TaskItem>... params) {
            ScheduleOptimization optimization = new ScheduleOptimization(params[0]);
            optimization.run();
            return optimization.mNumberSuccessfulItems;
        }

        @Override
        protected void onPostExecute(Integer numberSuccessfulItems) {
            mTaskListAdapter.notifyDataSetChanged();
            mCallbacks.onFinishOptimization(numberSuccessfulItems);
        }
    }

    class TaskListAdapter extends ArrayAdapter<TaskItem> {

        public List<TaskItem> items;

        public TaskListAdapter(List<TaskItem> tasks) {
            super(getActivity(), 0, tasks);
            items = tasks;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task, null);
            }

            TaskItem item = getItem(position);

            TextView tvName = ((TextView) convertView.findViewById(R.id.list_item_tv_name));
            tvName.setText(item.name);

            ((TextView) convertView.findViewById(R.id.list_item_tv_detail)).setText(item.details);
            ((TextView) convertView.findViewById(R.id.list_item_tv_estimate_time)).setText(DateTimeUtils.hour2Text(item.estimateTime));
            ((TextView) convertView.findViewById(R.id.list_item_tv_value)).setText(item.value.toString());
            TextView tvDeadline = ((TextView) convertView.findViewById(R.id.list_item_tv_deadline));
            tvDeadline.setText(DateTimeUtils.date2Text(item.deadline));

            if (TaskContent.mNumberSuccessfulItems > 0) {
                if (position < TaskContent.mNumberSuccessfulItems)
                    tvDeadline.setTextColor(Color.GREEN);
                else
                    tvDeadline.setTextColor(Color.RED);
            } else {
                tvDeadline.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }
}
