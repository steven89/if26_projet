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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link FormButtonFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class FormButtonFragment extends Fragment implements FormInputFragment{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String TEXT = "text";
	public static final String UriPath = "application/formButtonFragment";


	// TODO: Rename and change types of parameters
	private String innerText;
	private OnFragmentInteractionListener mListener;
	private Button submitBtn;
	private ProgressBar loader;
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment FormButtonFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static FormButtonFragment newInstance(String innerText) {
		FormButtonFragment fragment = new FormButtonFragment();
		Bundle args = new Bundle();
		args.putString(TEXT, innerText);
		fragment.setArguments(args);
		return fragment;
	}

	public FormButtonFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			innerText = getArguments().getString(TEXT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_form_button, container, false);
		this.submitBtn = (Button) rootView.findViewById(R.id.form_submit_btn);
		this.loader = (ProgressBar) rootView.findViewById(R.id.form_submit_loader);
		this.initListeners();
		return rootView;
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
	public String getValue() {
		return this.innerText;
	}
	
	public void initListeners(){
		this.submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onFragmentInteraction(Uri.parse("click://"+UriPath+"#"+v.getId()));
			}
		});
	}

	@Override
	public boolean isInputValide() {
		// TODO Auto-generated method stub
		return this.submitBtn.isEnabled();
	}
	
	public void displayLoader(){
		this.submitBtn.setVisibility(View.GONE);
		this.loader.setVisibility(View.VISIBLE);
	}
	
	public void hideLoader(){
		this.submitBtn.setVisibility(View.VISIBLE);
		this.loader.setVisibility(View.GONE);
	}
}
