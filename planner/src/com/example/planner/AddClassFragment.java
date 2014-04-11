package com.example.planner;

import com.example.planner.ClassContent.ClassItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddClassFragment extends Fragment {

	String entName;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		return inflater.inflate(R.layout.add_class_view, container, false);

	}

	public void onStart() {
		super.onStart();

		Button button = (Button) getActivity().findViewById(R.id.classbut);

		OnClickListener list = new OnClickListener() {
			public void onClick(View v) {
				getText();
				if (entName.length() <= 0) {
					ItemListFragment.alertMess(getString(R.string.error),
							getString(R.string.error),
							getString(R.string.accept));
				} else {
					ItemListFragment.modifyClasses("ADD", entName);
				}
			}
		};

		button.setOnClickListener(list);
	}

	// Get text of fields of add_class_view
	public void getText() {
		EditText text = (EditText) getActivity().findViewById(R.id.name_entry);
		entName = text.getText().toString();
	}

}