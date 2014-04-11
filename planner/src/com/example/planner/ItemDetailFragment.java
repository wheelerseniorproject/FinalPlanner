package com.example.planner;

import java.util.Collections;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.planner.ClassContent;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	
	static View rootView;
	
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private ClassContent.ClassItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = ClassContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_item_detail, container,
				false);

		return rootView;
	}

	public void onStart() {
		super.onStart();

		int index = -1;

		// Find active class
		String id = ItemListActivity.getIdOfSelected();
		for (int i = 0; i < ClassContent.ITEMS.size(); i++) {
			if (ClassContent.ITEMS.get(i).id.equals(id)) {
				index = i;
			}
		}

		final LinearLayout linLayout = (LinearLayout) getActivity()
				.findViewById(R.id.detail_fragment_container);

		RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		RelativeLayout.LayoutParams divParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		LayoutParams divPar = new LayoutParams(LayoutParams.MATCH_PARENT, 2);

		// Iterate through assignments of selected class and draw assignments
		for (int i = 0; i < ClassContent.ITEMS.get(index).ASSIGNMENTS.size(); i++) {

			RelativeLayout innerLayout = new RelativeLayout(getActivity());

			// Make date, text, and divider View
			TextView text = createText(""
					+ ClassContent.ITEMS.get(index).ASSIGNMENTS.get(i).name,
					android.R.style.TextAppearance_Large, 16);
			TextView date = createText(
					"Due: "
							+ ClassContent.ITEMS.get(index).ASSIGNMENTS.get(i).date
									.toString(),
					android.R.style.TextAppearance_Holo_Medium, 10);

			View divider = new View(getActivity().getApplicationContext());
			divider.setBackgroundColor(Color.DKGRAY);

			// Set them rules
			textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			date.setPadding(0, 60, 0, 0);

			// Create button, make red
			final Button priorityButton = new Button(getActivity()
					.getApplicationContext());

			priorityButton.setPadding(0, 20, 0, 0);

			switch (ClassContent.ITEMS.get(index).ASSIGNMENTS.get(i).priority) {
			case 0:
				priorityButton.getBackground().setColorFilter(Color.GREEN,
						PorterDuff.Mode.MULTIPLY);
				break;
			case 1:
				priorityButton.getBackground().setColorFilter(
						Color.rgb(195, 255, 0), PorterDuff.Mode.MULTIPLY);
				break;
			case 2:
				priorityButton.getBackground().setColorFilter(
						Color.rgb(255, 255, 0), PorterDuff.Mode.MULTIPLY);
				break;
			case 3:
				priorityButton.getBackground().setColorFilter(
						Color.rgb(255, 200, 0), PorterDuff.Mode.MULTIPLY);
				break;
			case 4:
				priorityButton.getBackground().setColorFilter(
						Color.rgb(255, 100, 0), PorterDuff.Mode.MULTIPLY);
				break;
			case 5:
				priorityButton.getBackground().setColorFilter(Color.RED,
						PorterDuff.Mode.MULTIPLY);
				break;
			default:
				priorityButton.getBackground().setColorFilter(Color.YELLOW,
						PorterDuff.Mode.MULTIPLY);
			}

			
			View div2 = new View(getActivity().getApplicationContext());
			RelativeLayout.LayoutParams div2Params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			
			
			// Add text, date, and divider
			innerLayout.addView(text, textParams);

			dateParams.addRule(RelativeLayout.BELOW, text.getId());
			innerLayout.addView(date, dateParams);
			
			innerLayout.addView(div2, div2Params);

			divParams.addRule(RelativeLayout.BELOW, date.getId());
			innerLayout.addView(divider, divParams);

			innerLayout.addView(priorityButton, buttonParams);

			linLayout.addView(innerLayout);

			Collections.sort(ClassContent.ITEMS.get(index).ASSIGNMENTS);

			final ClassContent.Assignment currentAssignment = ClassContent.ITEMS
					.get(index).ASSIGNMENTS.get(i);
			OnClickListener list = new OnClickListener() {
				public void onClick(View v) {
					ClassContent.Assignment.markAsCompleted(currentAssignment);
					priorityButton.getBackground().setColorFilter(Color.GREEN,
							PorterDuff.Mode.MULTIPLY);
					linLayout.invalidate();
				}
			};

			priorityButton.setOnClickListener(list);

		}
	}

	public TextView createText(String viewText, int appearance, int topPadding) {
		TextView text = new TextView(getActivity().getApplicationContext());
		text.setText(viewText);
		text.setPadding(4, 0, 0, 2);
		text.setTextAppearance(getActivity().getApplicationContext(),
				appearance);
		text.setTextColor(Color.BLACK);

		return text;
	}
}
