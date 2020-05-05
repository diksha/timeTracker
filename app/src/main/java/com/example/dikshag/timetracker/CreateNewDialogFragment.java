package com.example.dikshag.timetracker;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewDialogFragment extends AppCompatDialogFragment {
    private TimeTrackerNode timeTrackerNode;
    private Button confirmDialog;
    private Button cancelDialog;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.create_new_dialog, container, false);
        confirmDialog = view.findViewById(R.id.confirm_dialog);
        cancelDialog = view.findViewById(R.id.cancel_dialog);
        confirmDialog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmClicked();
                    }
                }
        );
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.rootView = view;
        return view;
    }

    private void confirmClicked() {
        EditText ettrackername = (EditText) rootView.findViewById(R.id.time_tracker_name);
        String trackername = ettrackername.getText().toString();
         if(TextUtils.isEmpty(trackername)) {
            ettrackername.setError("Task name cannot be empty.");
            return;
         }
        timeTrackerNode.addChild(new TimeTrackerNode(timeTrackerNode, trackername));
        ((MainActivity)this.getActivity()).notifyDataSetChanged();
        dismiss();
    }

    public void initTimeTrackerNode(TimeTrackerNode timeTrackerNode) {
        this.timeTrackerNode = timeTrackerNode;
    }
}