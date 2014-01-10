package fr.utt.if26.uttcoins.utils;

import org.w3c.dom.Text;

import fr.utt.if26.uttcoins.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PaymentConfirmationDialogFragment extends DialogFragment{
	
	private PaymentConfirmationDialogListener listener;
	
	private int transactionAmount = 0;
	private String transactionReceiver = "User not found";
	
	private TextView receiverNameText, 
					transactionAmountText,
					accountBalanceText;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.payment_confirmation_popup_layout, null);
        this.initContent(content);
        builder.setTitle(R.string.dialog_payment_confirm_title)
        		.setView(content)
        		.setPositiveButton(R.string.confirm_action_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       listener.onDialogPositiveClick(PaymentConfirmationDialogFragment.this);
                   }
               })
               	.setNegativeButton(R.string.cancel_action_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   listener.onDialogNegativeClick(PaymentConfirmationDialogFragment.this);
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
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public void setTransactionAmount(int amount){
    	this.transactionAmount = amount;
    }
    
    public void setTransactionReceiver(String receiver){
    	this.transactionReceiver = receiver;
    }
    
    private void initContent(View contentView){
    	this.receiverNameText = (TextView) contentView.findViewById(R.id.receiver_name_text);
    	this.receiverNameText.setText(this.transactionReceiver);
    	this.transactionAmountText = (TextView) contentView.findViewById(R.id.transaction_amount_text);
    	this.transactionAmountText.setText(Integer.toString(this.transactionAmount));
    	this.accountBalanceText = (TextView) contentView.findViewById(R.id.account_balance_value);
    	this.accountBalanceText.setText(Integer.toString(UserHelper.getAccountBalance() - this.transactionAmount));
    }
}
