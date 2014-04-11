package com.example.planner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.planner.ClassContent.Assignment;
import com.example.planner.ClassContent.ClassItem;

import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddAssignmentFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.add_assignment_view, container, false);
	}

	String name;
	SimpleDateFormat date;
	int priority;

	@Override
	public void onStart() {

		super.onStart();
		
		Button assignmentButton = (Button) getActivity()
				.findViewById(R.id.add_ass_button);

		OnClickListener list = new OnClickListener() {
			public void onClick(View v) {

				EditText text = (EditText) getActivity().findViewById(
						R.id.ass_name);
				name = text.getText().toString();
				EditText pString = (EditText) getActivity().findViewById(
						R.id.prior_text);
				
				if (name.length() > 0
						&& pString.getText().toString().length() > 0 && Integer.parseInt(pString.getText().toString()) <= 5 && Integer.parseInt(pString.getText().toString()) >= 0) {
					int dueYear, dueMonth, dueDay;

					DatePicker datePicked = (DatePicker) getActivity()
							.findViewById(R.id.due_date_picker);
					
					dueYear = datePicked.getYear();
					dueMonth = datePicked.getMonth();
					dueDay = datePicked.getDayOfMonth();

					ClassContent.SimpleDate simpleDate = new ClassContent.SimpleDate(
							dueYear, dueMonth, dueDay);

					priority = Integer.parseInt((pString.getText().toString()));

					String id = ItemListActivity.getIdOfSelected();

					for (int i = 0; i < ClassContent.ITEMS.size(); i++) {
						if (ClassContent.ITEMS.get(i).id.equals(id)) {
							Assignment assign = new Assignment(name,
									simpleDate, priority);
							ClassContent.ITEMS.get(i).ASSIGNMENTS.add(assign);

						}
					}
				} else {
					ItemListFragment.alertMess(getString(R.string.error), getString(R.string.error_text), getString(R.string.accept));
				}
			}
		};

		assignmentButton.setOnClickListener(list);
	}

}
