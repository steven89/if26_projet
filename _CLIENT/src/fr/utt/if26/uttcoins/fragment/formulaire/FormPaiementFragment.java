package fr.utt.if26.uttcoins.fragment.formulaire;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.fragment.CustomFragment;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment;
import fr.utt.if26.uttcoins.fragment.UserSoldeFragment;
import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment.PaymentConfirmationDialogListener;
import fr.utt.if26.uttcoins.model.TransactionList;
import fr.utt.if26.uttcoins.model.User;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

	private EditText transactionAmountInput, transactionReceiverInput;
	private Button payment_confirmation_button;
	private ProgressBar loader;
	
	
	private TextView transactionAmountError, transactionReceiverError;
	
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
			this.transactionAmount = args.getInt(ServerHelper.TRANSACTION_AMOUNT_KEY, 0);
			this.transactionReceiver = args.getString(ServerHelper.TRANSACTION_RECEIVER_KEY);
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
		this.transactionAmountError = (TextView) view.findViewById(R.id.payment_amount_error);
		this.transactionReceiverError = (TextView) view.findViewById(R.id.payment_receiver_error);
		this.loader = (ProgressBar) view.findViewById(R.id.payment_inprogress_loader);
		this.initListener();
		this.initContent(container, savedInstanceState);
		return view;
	}
	
	protected void initListener(){
		this.payment_confirmation_button.setOnClickListener(this);
		TextWatcher validatorWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			@Override
			public void afterTextChanged(Editable s) {
				isTransactionValide();
			}
		};
		this.transactionReceiverInput.addTextChangedListener(validatorWatcher);
		this.transactionAmountInput.addTextChangedListener(validatorWatcher);
	}
	
	protected void initContent(View v, Bundle savedInstanceState){
    	savedInstanceState = (savedInstanceState != null)? savedInstanceState : new Bundle();
    	this.transactionAmount = savedInstanceState.getInt(ServerHelper.TRANSACTION_AMOUNT_KEY, this.transactionAmount);
		this.transactionReceiver = (savedInstanceState.getString(ServerHelper.TRANSACTION_RECEIVER_KEY) != null) ?
				savedInstanceState.getString(ServerHelper.TRANSACTION_RECEIVER_KEY): this.transactionReceiver;
		if(this.transactionAmount != 0)
			this.transactionAmountInput.setText(Integer.toString(transactionAmount), BufferType.EDITABLE);
		if(this.transactionReceiver != null)
    		this.transactionReceiverInput.setText(transactionReceiver);
		this.payment_confirmation_button.setEnabled(this.isTransactionValide());
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
      savedInstanceState.putInt(ServerHelper.TRANSACTION_AMOUNT_KEY, this.getTransactionAmount());
      savedInstanceState.putString(ServerHelper.TRANSACTION_RECEIVER_KEY, this.getTransactionReceiver());
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
	
	public boolean isTransactionReceiverValide(){
		String transactionReceiverText = this.getTransactionReceiver();
		Boolean valide = transactionReceiverText.matches(User.TAG_PATTERN);
		if(valide){
			this.transactionReceiverError.setVisibility(View.GONE);
		}else{
			this.transactionReceiverError.setVisibility(View.VISIBLE);
		}
		return valide;
	}
	
	public boolean isTransactionAmountValide(){
		int transactionAmountValue = this.getTransactionAmount();
		int UserAccountBalance = ServerHelper.getSession().getInt(ServerHelper.SERVER_BALANCE_KEY); //ServerHelper.getUserSolde(request_tag, callback);
		boolean amountValue = transactionAmountValue > 0;
		boolean enougth_uttCoins = transactionAmountValue <= UserAccountBalance;
		if(amountValue){
			this.transactionAmountError.setVisibility(View.GONE);
		}else{
			this.transactionAmountError.setText(R.string.payment_amount_required_error);
			this.transactionAmountError.setVisibility(View.VISIBLE);
		}
		if(!enougth_uttCoins){
			this.transactionAmountError.setText(R.string.not_enougth_uttCoins_error);
			this.transactionAmountError.setVisibility(View.VISIBLE);
		}
		return amountValue && enougth_uttCoins;
	}

	public boolean isTransactionValide() {
		//passe par des refs intermediaires pour forcer l'evaluation des deux expressions
		// (dans un '&&', l'evaluation stop a la premiere expression à false rencontre) 
		boolean isTransactionAmountValide = this.isTransactionAmountValide();
		boolean isTransactionReceiverValide = this.isTransactionReceiverValide();
		boolean valide =  isTransactionAmountValide && isTransactionReceiverValide;
		if(valide){
			this.payment_confirmation_button.setEnabled(true);
		}else{
			this.payment_confirmation_button.setEnabled(false);
		}
		return valide;
	}
	
	
	public void showLoader(){
		this.payment_confirmation_button.setVisibility(View.GONE);
		this.loader.setVisibility(View.VISIBLE);
	}
	
	public void hideLoader(){
		this.loader.setVisibility(View.GONE);
		this.payment_confirmation_button.setVisibility(View.VISIBLE);
	}
}
