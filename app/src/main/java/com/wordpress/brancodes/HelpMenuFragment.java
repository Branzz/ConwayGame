package com.wordpress.brancodes;

import android.opengl.Visibility;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpMenuFragment extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	// private static final String ARG_PARAM1 = "param1";
	// private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	// private String mParam1;
	// private String mParam2;

	public HelpMenuFragment() {
		System.out.println("Created");
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment helpFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static HelpMenuFragment newInstance(int i) {
		HelpMenuFragment fragment = new HelpMenuFragment();
		Bundle args = new Bundle();
		// args.putString(ARG_PARAM1, param1);
		// args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		fragment.keyed = i == 5;
		return fragment;
	}

	boolean keyed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (getArguments() != null) {
			// mParam1 = getArguments().getString(ARG_PARAM1);
			// mParam2 = getArguments().getString(ARG_PARAM2);
		// }
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// if (keyed)
		// 	view.setVisibility(View.VISIBLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_help, container, false);
	}

}
