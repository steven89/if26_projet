package fr.utt.if26.uttcoins;

import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment;
import fr.utt.if26.uttcoins.fragment.TransactionListFragment;
import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment.PaymentConfirmationDialogListener;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPaiementFragment;
import fr.utt.if26.uttcoins.model.Transaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class PaiementActivity extends NavDrawerActivity implements PaymentConfirmationDialogListener{

	public final static int positionInDrawer = 3;
	
	private FormPaiementFragment formPaymentFragment;
	private static String FORM_PAYMENT_FRAGMENT_TAG = "formPaymentFragment";
	private ScrollView innerListViewContainer;
	private static final int INNER_LIST_VIEW_CONTAINER_ID = R.id.formPaymentFragContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("NAV", "PaiementActivity has started");
		this.formPaymentFragment.setArguments(this.getPresetData(savedInstanceState));
	}

	private Bundle getPresetData(Bundle savedInstanceState) {
		Bundle extra = (this.getIntent().getExtras() != null) ? this.getIntent().getExtras() : new Bundle();
		savedInstanceState = (savedInstanceState != null) ? savedInstanceState : new Bundle();
		Log.i("ACTIVITY", "got PaiementActivity's Bundle");
		Bundle preSetData = new Bundle();
		String transactionReceiver = savedInstanceState.getString(Transaction.TRANSACTION_RECEIVER_KEY);
		transactionReceiver = (transactionReceiver != null) ? 
				transactionReceiver : extra.getString(Transaction.TRANSACTION_RECEIVER_KEY);
		int transactionAmount = savedInstanceState.getInt(Transaction.TRANSACTION_AMOUNT_KEY);
		transactionAmount = (transactionAmount != 0) ?
				transactionAmount : extra.getInt(Transaction.TRANSACTION_AMOUNT_KEY);
		preSetData.putString(Transaction.TRANSACTION_RECEIVER_KEY, transactionReceiver);
		preSetData.putInt(Transaction.TRANSACTION_AMOUNT_KEY, transactionAmount);
		return preSetData;
	}
	
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      savedInstanceState.putInt(Transaction.TRANSACTION_AMOUNT_KEY, this.formPaymentFragment.getTransactionAmount());
      savedInstanceState.putString(Transaction.TRANSACTION_RECEIVER_KEY, this.formPaymentFragment.getTransactionReceiver());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wallet, menu);
		return true;
	}

	@Override
	protected void initFragments() {
		FragmentManager fm = getSupportFragmentManager();
		this.formPaymentFragment = (FormPaiementFragment) fm.findFragmentByTag(FORM_PAYMENT_FRAGMENT_TAG);
		if(this.formPaymentFragment == null)
			this.formPaymentFragment = FormPaiementFragment.newInstance(FORM_PAYMENT_FRAGMENT_TAG);
		Log.i("ACTIVITY", "formPaymentFragment initialized !");
		this.showFragment(INNER_LIST_VIEW_CONTAINER_ID, this.formPaymentFragment);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		switch(Integer.parseInt(uri.getFragment())){
			case R.id.payment_confirmation_button :
				if(this.formPaymentFragment.isTransactionValide()){
					this.showConfirmPaymentDialog();
				}else{
					
				}
				break;
		}
	}

	@Override
	protected void initInnerContentLayout(ViewGroup container) {
		RelativeLayout.LayoutParams lp_innerListViewContainer = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.innerListViewContainer = new ScrollView(this);
		this.innerListViewContainer.setId(INNER_LIST_VIEW_CONTAINER_ID);
		this.innerListViewContainer.setLayoutParams(lp_innerListViewContainer);
		container.addView(this.innerListViewContainer);
	}
	
	private void showConfirmPaymentDialog() {
		PaymentConfirmationDialogFragment confirmDialogFragment = new PaymentConfirmationDialogFragment();
		Bundle initialState = new Bundle();
		initialState.putInt(Transaction.TRANSACTION_AMOUNT_KEY, this.formPaymentFragment.getTransactionAmount());
		initialState.putString(Transaction.TRANSACTION_RECEIVER_KEY, this.formPaymentFragment.getTransactionReceiver());
		confirmDialogFragment.setArguments(initialState);
		confirmDialogFragment.show(getSupportFragmentManager(), "confirmation");
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
				
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}
}
