package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
//fragment encapsulant les mécanismes de base d'un champ de saisie dans un formulaire
public class FormSimpleInputFragment extends Fragment implements FormInputFragment{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String simplePattern = "^.*[^a-zA-Z]+.*$";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private TextView simple_error;
	private EditText simple_input;

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

	

	public FormSimpleInputFragment() {
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
		View rootView = inflater.inflate(R.layout.fragment_form_simple_input, container,
				false);
		this.simple_input = (EditText) rootView.findViewById(R.id.simple_input);
		this.simple_error = (TextView) rootView.findViewById(R.id.simple_error);
		
		this.initListeners();
		
		return rootView;
	}


	private void initListeners() {
		this.simple_input.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					isInputValide();
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
	
	@Override
	public boolean isInputValide(){
		boolean isValide = !this.simple_input.getText().toString().matches(simplePattern);
		if(!isValide){
			this.simple_error.setText(R.string.email_format_error_text);
			this.simple_error.setVisibility(View.VISIBLE);
		}else{
			this.simple_error.setVisibility(View.GONE);
		}
		return isValide;
	}
	
	public String getValue(){
		return this.simple_input.getText().toString();
	}
	
	public void setHint(CharSequence hint){
		this.simple_input.setHint(hint);
	}
	
	public void setHint(int hintId){
		this.simple_input.setHint(getResources().getString(hintId));
	}
		
}
