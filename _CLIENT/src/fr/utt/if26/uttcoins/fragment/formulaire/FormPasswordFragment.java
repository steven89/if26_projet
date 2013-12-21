package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link FormPasswordFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link FormPasswordFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class FormPasswordFragment extends Fragment implements FormInputFragment{
	
	public static final String NEED_CONFIRMATION = "needConfirmation"; 
	
	private TextView password_error;
	private EditText password_input;
	private TextView passwordConfirmation_error;
	private EditText passwordConfirmation_input;
	private boolean needConfirmation;
	
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment FormPasswordFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static FormPasswordFragment newInstance(boolean needConfirmation) {
		FormPasswordFragment fragment = new FormPasswordFragment();
		Bundle args = new Bundle();
		args.putBoolean("needConfirmation", needConfirmation);
		fragment.setArguments(args);
		return fragment;
	}

	public FormPasswordFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			this.needConfirmation = getArguments().getBoolean(NEED_CONFIRMATION);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView =  inflater.inflate(R.layout.fragment_form_password, container,
				false);
		this.password_error = (TextView) rootView.findViewById(R.id.password_error);
		this.password_input = (EditText) rootView.findViewById(R.id.password_input);
		this.attachPasswordConfirmation(container);
		this.initListeners();
		return rootView;
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
	
	private void initListeners() {
		// TODO Auto-generated method stub
		this.password_input.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					checkPasswordInput();
				}
			}
		});
	}
	
	private void checkPasswordInput(){
		if(this.password_input.getText().toString().length() == 0){
			this.password_error.setText(R.string.password_required_error_text);
			this.password_error.setVisibility(View.VISIBLE);
		}else{
			this.password_error.setVisibility(View.GONE);
		}
	}
	
	private void attachPasswordConfirmation(ViewGroup containerView){
		if(this.needConfirmation){
			Context ctx = getActivity();

			this.passwordConfirmation_error = new TextView(ctx);
			this.passwordConfirmation_input = new EditText(ctx);
			
			RelativeLayout.LayoutParams lp_error = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp_error.addRule(RelativeLayout.ABOVE, R.id.password_input);
			lp_error.addRule(RelativeLayout.CENTER_HORIZONTAL);
			
			this.passwordConfirmation_error.setLayoutParams(lp_error);
			this.passwordConfirmation_error.setId(R.id.password_confirmation_error);
			this.passwordConfirmation_error.setTextColor(getResources().getColor(R.color.text_error));
			this.passwordConfirmation_error.setText(R.string.password_required_error_text);
			this.passwordConfirmation_error.setVisibility(View.GONE);
			
			RelativeLayout.LayoutParams lp_input = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp_input.addRule(RelativeLayout.ABOVE, R.id.password_confirmation_error);
			lp_input.addRule(RelativeLayout.CENTER_HORIZONTAL);
			
			this.passwordConfirmation_input.setLayoutParams(lp_input);
			this.passwordConfirmation_input.setId(R.id.password_confirmation_input);
			this.passwordConfirmation_input.setHint(R.string.password_confirmation_input_placeholder);
			this.passwordConfirmation_input.setVisibility(View.VISIBLE);
			
			containerView.addView(this.passwordConfirmation_error);
			containerView.addView(this.passwordConfirmation_input);
			
		}
	}
	
	public String getValue(){
		return this.password_input.getText().toString();
	}
}
