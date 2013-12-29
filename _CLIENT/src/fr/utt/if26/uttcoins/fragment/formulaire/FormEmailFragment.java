package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
	private static final String NEED_CONFIRMATION = "needConfirmation";
	public static final String TAG = "FormEmailFragment";

	private static final String ARG_PARAM2 = "param2";
	private static final String emailPattern = "^[a-zA-Z0-9_\\.\\-]*@([a-zA-Z0-9_]*\\.[a-z]{1,3})+$";
	
	// TODO: Rename and change types of parameters
	private boolean needConfirmation;
	private String mParam2;
	
	private TextView email_error;
	private EditText email_input;
	private TextView email_confirmation_error;
	private EditText email_confirmation_input;

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
	public static FormEmailFragment newInstance(boolean needConfirmation) {
		FormEmailFragment fragment = new FormEmailFragment();
		Bundle args = new Bundle();
		args.putBoolean(NEED_CONFIRMATION, needConfirmation);
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
			needConfirmation = getArguments().getBoolean(NEED_CONFIRMATION);
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
		RelativeLayout fragmentInnerContainer = (RelativeLayout) rootView.findViewById(R.id.fragment_form_email_container);
		this.attachPasswordConfirmation(fragmentInnerContainer);
		this.initListeners();
		return rootView;
	}

	private void initListeners() {
		this.email_input.setOnFocusChangeListener(new OnFocusChangeListener() {
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
		boolean isValide = this.email_input.getText().toString().matches(emailPattern);
		if(this.needConfirmation){
			isValide = (isValide && (this.email_confirmation_input.getText().toString().equals(this.email_input.getText().toString())));
			if(!isValide){
				this.email_confirmation_error.setText(R.string.email_confirmation_error_text);
				this.email_confirmation_error.setVisibility(View.VISIBLE);
			}else{
				this.email_confirmation_error.setVisibility(View.GONE);
			}
		}else{
			if(!isValide){
				this.email_error.setText(R.string.email_format_error_text);
				this.email_error.setVisibility(View.VISIBLE);
			}else{
				this.email_error.setVisibility(View.GONE);
			}
		}
		return isValide;
	}
	
	public String getValue(){
		return this.email_input.getText().toString();
	}
	
	private void attachPasswordConfirmation(ViewGroup containerView){
		if(this.needConfirmation){
			Context ctx = getActivity();

			this.email_confirmation_error = new TextView(ctx);
			this.email_confirmation_input = new EditText(ctx);
			
//			LinearLayout containerLayout = new LinearLayout(getActivity());
//			containerLayout.setOrientation(LinearLayout.VERTICAL);
			
			RelativeLayout.LayoutParams lp_error = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp_error.addRule(RelativeLayout.BELOW, R.id.email_input);
			lp_error.addRule(RelativeLayout.CENTER_HORIZONTAL);
			
//			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
//					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			this.email_confirmation_error.setLayoutParams(lp_error);
			this.email_confirmation_error.setId(R.id.email_confirmation_error);
			this.email_confirmation_error.setTextColor(getResources().getColor(R.color.text_error));
			this.email_confirmation_error.setText(R.string.email_confirmation_error_text);
			this.email_confirmation_error.setVisibility(View.GONE);
			
			RelativeLayout.LayoutParams lp_input = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp_input.addRule(RelativeLayout.BELOW, R.id.email_confirmation_error);
			lp_input.addRule(RelativeLayout.CENTER_HORIZONTAL);
				
			this.email_confirmation_input.setLayoutParams(lp_input);
			this.email_confirmation_input.setId(R.id.email_confirmation_input);
			this.email_confirmation_input.setHint(R.string.email_confirmation_input_placeholder);
			this.email_confirmation_input.setVisibility(View.VISIBLE);
			
			containerView.addView(this.email_confirmation_error);
			containerView.addView(this.email_confirmation_input);
			//containerView.addView(containerLayout);
		}
	}
}
