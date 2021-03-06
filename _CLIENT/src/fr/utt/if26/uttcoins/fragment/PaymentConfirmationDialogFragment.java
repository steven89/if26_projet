package fr.utt.if26.uttcoins.fragment;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PaymentConfirmationDialogFragment extends DialogFragment{
	
	private PaymentConfirmationDialogListener listener;
	
	private TextView receiverNameText, 
					transactionAmountText,
					accountBalanceText;
	
	private int transactionAmount;
	private String transactionReceiver;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.payment_confirmation_popup_layout, null);
        this.initContent(content, savedInstanceState);
        builder.setTitle(R.string.dialog_payment_confirm_title)
        		.setView(content)
        		.setPositiveButton(R.string.confirm_action_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       listener.onPaymentConfirmationDialogPositiveClick(PaymentConfirmationDialogFragment.this);
                   }
               })
               	.setNegativeButton(R.string.cancel_action_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   listener.onPaymentConfirmationDialogNegativeClick(PaymentConfirmationDialogFragment.this);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (PaymentConfirmationDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement PaymentConfirmationDialogListener");
        }
    }

    public interface PaymentConfirmationDialogListener {
        public void onPaymentConfirmationDialogPositiveClick(PaymentConfirmationDialogFragment dialog);
        public void onPaymentConfirmationDialogNegativeClick(PaymentConfirmationDialogFragment dialog);
    }

    private void initContent(View contentView, Bundle savedInstanceState){
    	this.receiverNameText = (TextView) contentView.findViewById(R.id.receiver_name_text);
    	this.transactionAmountText = (TextView) contentView.findViewById(R.id.transaction_amount_text);
    	this.accountBalanceText = (TextView) contentView.findViewById(R.id.account_balance_value);
    	
    	Bundle arguments = (getArguments() != null)? getArguments() : new Bundle();
    	this.transactionAmount = arguments.getInt(ServerHelper.TRANSACTION_AMOUNT_KEY, 0);
		this.transactionReceiver = (arguments.getString(ServerHelper.TRANSACTION_RECEIVER_KEY) != null && arguments.getString(ServerHelper.TRANSACTION_RECEIVER_KEY) != "") ? 
				arguments.getString(ServerHelper.TRANSACTION_RECEIVER_KEY) : getResources().getString(R.string.userNotFound);
    	savedInstanceState = (savedInstanceState != null)? savedInstanceState : new Bundle();
    	this.transactionAmount = savedInstanceState.getInt(ServerHelper.TRANSACTION_AMOUNT_KEY, this.transactionAmount);
		this.transactionReceiver = (savedInstanceState.getString(ServerHelper.TRANSACTION_RECEIVER_KEY) != null && savedInstanceState.getString(ServerHelper.TRANSACTION_RECEIVER_KEY) != "") ?
				savedInstanceState.getString(ServerHelper.TRANSACTION_RECEIVER_KEY): this.transactionReceiver;
		this.transactionAmountText.setText(Integer.toString(transactionAmount));
		//if(this.transactionReceiver != null)
		this.receiverNameText.setText(transactionReceiver);
		int UserAccountBalance = ServerHelper.getSession().getInt(ServerHelper.SERVER_BALANCE_KEY) - this.transactionAmount;
    	this.accountBalanceText.setText(Integer.toString(UserAccountBalance));
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
    	// #paranoia
		int transactionAmountValue; 
		try{
			transactionAmountValue = Integer.parseInt(this.transactionAmountText.getText().toString());
		}catch(NumberFormatException e){
			transactionAmountValue = 0;
		}
		savedInstanceState.putInt(ServerHelper.TRANSACTION_AMOUNT_KEY, transactionAmountValue);
		savedInstanceState.putString(ServerHelper.TRANSACTION_RECEIVER_KEY, this.receiverNameText.getText().toString());
    }

	public String getTransactionReceiver() {
		return this.transactionReceiver;
	}

	public int getTransactionAmount() {
		return this.transactionAmount;
	}
}
