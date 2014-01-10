package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.CustomFragment;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.UserSoldeFragment;
import fr.utt.if26.uttcoins.model.TransactionList;
import fr.utt.if26.uttcoins.utils.PaymentConfirmationDialogFragment;
import fr.utt.if26.uttcoins.utils.PaymentConfirmationDialogFragment.PaymentConfirmationDialogListener;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FormPaiementFragment extends CustomFragment implements android.view.View.OnClickListener{

	public static final String UriPath = "application/FormPaiementFragment";
	private static final String TAG = "TAG";

	private String tag;

	private OnFragmentInteractionListener mListener;

	private EditText transactionAmountInput;
	private EditText transactionReceiverInput;
	private Button payment_confirmation_button;
	
	private String preSetReceiver = "";
	private int preSetAmount = 0;

	public FormPaiementFragment() {
		// Required empty public constructor
	}
	
	public static FormPaiementFragment newInstance(String fragment_tag) {
		FormPaiementFragment fragment = new FormPaiementFragment();
		Bundle args = new Bundle();
		args.putString(TAG, fragment_tag);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			this.tag = getArguments().getString(TAG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_form_paiement, container,
				false);
		this.transactionReceiverInput = (EditText) view.findViewById(R.id.payment_receiver);
		if(this.preSetReceiver != "")
			this.transactionReceiverInput.setText(this.preSetReceiver);
		this.transactionAmountInput = (EditText) view.findViewById(R.id.payment_amount);
		if(this.preSetAmount != 0)
			this.transactionAmountInput.setText(Integer.toString(this.preSetAmount), BufferType.EDITABLE);
		this.payment_confirmation_button = (Button) view.findViewById(R.id.payment_confirmation_button);
		this.initListener();
		return view;
	}
	
	protected void initListener(){
		this.payment_confirmation_button.setOnClickListener(this);
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

	public void setTransactionReceiver(String reveiverName) {
		this.preSetReceiver = reveiverName;
	}

	public void setTransactionAmount(int transactionAmount) {
		Log.i("SET", "Set transactionAmout = "+Integer.toString(transactionAmount));
		this.preSetAmount = transactionAmount;
	}
	
	public String getTransactionReceiver(){
		return this.transactionReceiverInput.getText().toString();
	}
	
	public int getTransactionAmount(){
		return Integer.parseInt(this.transactionAmountInput.getText().toString());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.payment_confirmation_button :
				this.mListener.onFragmentInteraction(Uri.parse("click://"+UriPath+"#"+v.getId()));;
				break;
		}
	}
}
