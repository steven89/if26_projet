package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.CustomFragment;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment;
import fr.utt.if26.uttcoins.fragment.UserSoldeFragment;
import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment.PaymentConfirmationDialogListener;
import fr.utt.if26.uttcoins.model.Transaction;
import fr.utt.if26.uttcoins.model.TransactionList;
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
	
	private int transactionAmount;
	private String transactionReceiver;
	
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
			Bundle args = getArguments();
			this.tag = args.getString(TAG);
			this.transactionAmount = args.getInt(Transaction.TRANSACTION_AMOUNT_KEY, 0);
			this.transactionReceiver = args.getString(Transaction.TRANSACTION_RECEIVER_KEY);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_form_paiement, container,
				false);
		Log.i("STATE", "(FormPaiementFragment) OnCreateView");
		this.transactionReceiverInput = (EditText) view.findViewById(R.id.payment_receiver);
		this.transactionAmountInput = (EditText) view.findViewById(R.id.payment_amount);
		this.payment_confirmation_button = (Button) view.findViewById(R.id.payment_confirmation_button);
		this.initListener();
		this.initContent(container, savedInstanceState);
		return view;
	}
	
	protected void initListener(){
		this.payment_confirmation_button.setOnClickListener(this);
	}
	
	protected void initContent(View v, Bundle savedInstanceState){
    	savedInstanceState = (savedInstanceState != null)? savedInstanceState : new Bundle();
    	this.transactionAmount = savedInstanceState.getInt(Transaction.TRANSACTION_AMOUNT_KEY, this.transactionAmount);
		this.transactionReceiver = (savedInstanceState.getString(Transaction.TRANSACTION_RECEIVER_KEY) != null) ?
				savedInstanceState.getString(Transaction.TRANSACTION_RECEIVER_KEY): this.transactionReceiver;
		if(this.transactionAmount != 0)
			this.transactionAmountInput.setText(Integer.toString(transactionAmount), BufferType.EDITABLE);
		if(this.transactionReceiver != null)
    		this.transactionReceiverInput.setText(transactionReceiver);
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      savedInstanceState.putInt(Transaction.TRANSACTION_AMOUNT_KEY, this.getTransactionAmount());
      savedInstanceState.putString(Transaction.TRANSACTION_RECEIVER_KEY, this.getTransactionReceiver());
    }
    
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	public String getTransactionReceiver(){
		return this.transactionReceiverInput.getText().toString();
	}
	
	public int getTransactionAmount(){
		int transactionAmountValue; 
	    try{
	    	transactionAmountValue = Integer.parseInt(this.transactionAmountInput.getText().toString());
	    }catch(NumberFormatException e){
	    	transactionAmountValue = 0;
	    }
		return transactionAmountValue;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.payment_confirmation_button :
				this.mListener.onFragmentInteraction(Uri.parse("click://"+UriPath+"#"+v.getId()));;
				break;
		}
	}

	public boolean isTransactionValide() {
		// TODO Auto-generated method stub
		return true;
	}
}
