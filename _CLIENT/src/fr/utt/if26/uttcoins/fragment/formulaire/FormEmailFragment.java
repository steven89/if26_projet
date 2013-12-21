package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link FormEmailFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link FormEmailFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class FormEmailFragment extends Fragment implements FormInputFragment{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String emailPattern = "^[a-zA-Z0-9_\\.\\-]*@([a-zA-Z0-9_]*\\.[a-z]{1,3})+$";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private TextView email_error;
	private EditText email_input;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment FormEmailFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static FormEmailFragment newInstance(String param1, String param2) {
		FormEmailFragment fragment = new FormEmailFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public FormEmailFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//inutile pour l'instant
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_form_email, container, false);
		this.email_error = (TextView) rootView.findViewById(R.id.email_error);
		this.email_input = (EditText) rootView.findViewById(R.id.email_input);
		
		this.initListeners();
		return rootView;
	}

	private void initListeners() {
		this.email_input.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					checkInputEmail();
				}
			}
		});		
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	private void checkInputEmail(){
		if(!this.email_input.getText().toString().matches(emailPattern)){
			this.email_error.setText(R.string.email_format_error_text);
			this.email_error.setVisibility(View.VISIBLE);
		}else{
			this.email_error.setVisibility(View.GONE);
		}
	}
	
	public String getValue(){
		return this.email_input.getText().toString();
	}
}
