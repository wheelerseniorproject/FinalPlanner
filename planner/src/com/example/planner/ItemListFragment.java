package com.example.planner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.planner.ClassContent;
import com.example.planner.ClassContent.ClassItem;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	URL url;
	ArrayAdapter adapter;

	static ArrayAdapter staticAdap;
	static ArrayList<Integer> idList = new ArrayList<>();
	static String STORED_CLASSES = " ";
	static File storedFile;
	static String filePath = "/";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemListFragment() {
	}

	Dialog test;
	static FragmentActivity fragAct;
	static ArrayAdapter deleteAdap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: replace with a real list adapter.
		adapter = new ArrayAdapter<ClassContent.ClassItem>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, ClassContent.ITEMS);

		setListAdapter(adapter);
		staticAdap = adapter;
		deleteAdap = adapter;

		this.setHasOptionsMenu(true);

		storedFile = new File(getActivity().getFilesDir().getAbsoluteFile()
				+ filePath, "storedClasses.ser");

		getClassData();
		fragAct = getActivity();

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("Delete Class");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.actionbar_stuff, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.add_assignment:
			switchToAddAss();
			return super.onOptionsItemSelected(item);
		case R.id.add_class:
			switchToAddClass();
			return super.onOptionsItemSelected(item);
		case R.id.delete_class:
			String idOfDelte = ItemListActivity.getIdOfSelected();
			modifyClasses("DELETE", idOfDelte);
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static void modifyClasses(String doWhat, String idOrName) {
		if (doWhat.equals("ADD")) {
			idList.add(new Integer(idList.size()));

			ClassContent.addItem(new ClassItem(idList.get(idList.size() - 1)
					.toString(), idOrName, "of"));

			STORED_CLASSES += idOrName + " ";

			staticAdap.notifyDataSetChanged();
		} else if (doWhat.equals("DELETE")) {
			for (ClassItem item : ClassContent.ITEMS) {
				if (item.id.equals(idOrName)) {
					ClassContent.ITEMS.remove(item);
					staticAdap.notifyDataSetChanged();
				}
			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
		System.out.println("FILES 3: "
				+ containedFiles(getActivity().getFilesDir().getAbsoluteFile()
						+ filePath) + "\n \n");
		writeClassData();
		System.out.println("FILES 4: "
				+ containedFiles(getActivity().getFilesDir().getAbsoluteFile()
						+ filePath) + "\n \n");
	}

	public void onPause() {
		super.onPause();

		System.out.println("FILES 0: "
				+ containedFiles(getActivity().getFilesDir().getAbsoluteFile()
						+ filePath) + "\n \n");
		writeClassData();
		System.out.println("FILES 1: "
				+ containedFiles(getActivity().getFilesDir().getAbsoluteFile()
						+ filePath) + "\n \n");
	}

	public String containedFiles(String path) {
		File test = new File(path);
		String list = "";
		File[] files = test.listFiles();

		for (int i = 0; i < files.length; i++) {
			list += files[i].getName() + ", ";
		}

		return list;
	}

	public void writeClassData() {
		ArrayList<ClassItem> items = (ArrayList<ClassItem>) ClassContent.ITEMS;

		try {

			System.out.println(filePath);

			storedFile = new File(
					getActivity().getFilesDir().getAbsoluteFile(),
					"storedClasses.ser");

			System.out.println("asdfdfsdf");

			FileOutputStream fosOut = new FileOutputStream(storedFile);

			System.out.println("DATA BEING SAVED 8");

			ObjectOutputStream objOut = new ObjectOutputStream(fosOut);

			objOut.writeObject(items);
			objOut.close();

			fosOut.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("FILE NOT FOUND EXEPCETIONS");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		}
	}

	public void getClassData() {
		ArrayList<ClassItem> testList = new ArrayList<>();

		try {
			FileInputStream fosIn = new FileInputStream(storedFile);

			ObjectInputStream objIn = new ObjectInputStream(fosIn);

			ArrayList<ClassItem> cat = new ArrayList<ClassItem>();
			System.out.println("BEFORE READ");

			cat = (ArrayList<ClassItem>) objIn.readObject();

			System.out.println("AFTER READ");
			for (ClassItem items : cat) {
				System.out.println("READING: " + items.content);
			}

			ClassContent.updateList(cat);

			System.out.println("LIST UPDATED");
			objIn.close();
			fosIn.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR IN READ");
		} catch (StreamCorruptedException e) {
			System.out.println("READ CORRUPT");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO EXCEPTIED");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("");
			e.printStackTrace();
		}
	}

	public void onResume() {
		super.onResume();
		System.out.println("ON RESUME called");

		getClassData();

		getActivity().findViewById(R.id.item_detail_container).invalidate();
	}

	public void switchToAddClass() {
		AddClassFragment frag = new AddClassFragment();
		android.support.v4.app.FragmentManager fragMan = getFragmentManager();
		fragMan.beginTransaction().replace(R.id.item_detail_container, frag)
				.commit();
	}

	public void switchToAddAss() {
		AddAssignmentFragment frag = new AddAssignmentFragment();
		android.support.v4.app.FragmentManager fragMan = getFragmentManager();
		fragMan.beginTransaction().replace(R.id.item_detail_container, frag)
				.commit();
	}

	public static void alertMess(String title, String content, String buttonText) {
		final AlertDialog alertDialog = new AlertDialog.Builder(fragAct)
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(content);
		alertDialog.setButton(buttonText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.hide();
					}
				});
		alertDialog.show();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(ClassContent.ITEMS.get(position).id);
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
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
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

}
