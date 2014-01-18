package fr.utt.if26.uttcoins;

import org.bson.BasicBSONObject;

import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment;
import fr.utt.if26.uttcoins.fragment.TransactionListFragment;
import fr.utt.if26.uttcoins.fragment.PaymentConfirmationDialogFragment.PaymentConfirmationDialogListener;
import fr.utt.if26.uttcoins.fragment.UserSoldeFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPaiementFragment;
import fr.utt.if26.uttcoins.model.Transaction;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class PaiementActivity extends NavDrawerActivity implements PaymentConfirmationDialogListener{

	private static String USER_BALANCE_FRAGMENT_TAG = "userBalanceFragment";
	private static String FORM_PAYMENT_FRAGMENT_TAG = "formPaymentFragment";
	private static final int INNER_LIST_VIEW_CONTAINER_ID = R.id.formPaymentFragContainer;
	private static final int INNER_HEADER_FRAG_CONTAINER_ID = R.id.user_solde_container;

	public final static int positionInDrawer = 1;
	
	private FormPaiementFragment formPaymentFragment;
	private UserSoldeFragment userSoldeFragment;
	private ScrollView innerScrollViewContainer;
	private FrameLayout userBalanceContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("NAV", "PaiementActivity has started");
		this.formPaymentFragment.setArguments(this.getPresetData(savedInstanceState));
	}

	//on récupère les éventuelles données pré-remplies d'une transaction
	private Bundle getPresetData(Bundle savedInstanceState) {
		Bundle extra = (this.getIntent().getExtras() != null) ? this.getIntent().getExtras() : new Bundle();
		savedInstanceState = (savedInstanceState != null) ? savedInstanceState : new Bundle();
		//Log.i("ACTIVITY", "got PaiementActivity's Bundle");
		Bundle preSetData = new Bundle();
		String transactionReceiver = savedInstanceState.getString(ServerHelper.TRANSACTION_RECEIVER_KEY);
		transactionReceiver = (transactionReceiver != null) ? 
				transactionReceiver : extra.getString(ServerHelper.TRANSACTION_RECEIVER_KEY);
		int transactionAmount = savedInstanceState.getInt(ServerHelper.TRANSACTION_AMOUNT_KEY);
		transactionAmount = (transactionAmount != 0) ?
				transactionAmount : extra.getInt(ServerHelper.TRANSACTION_AMOUNT_KEY);
		preSetData.putString(ServerHelper.TRANSACTION_RECEIVER_KEY, transactionReceiver);
		preSetData.putInt(ServerHelper.TRANSACTION_AMOUNT_KEY, transactionAmount);
		return preSetData;
	}
	
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      savedInstanceState.putInt(ServerHelper.TRANSACTION_AMOUNT_KEY, this.formPaymentFragment.getTransactionAmount());
      savedInstanceState.putString(ServerHelper.TRANSACTION_RECEIVER_KEY, this.formPaymentFragment.getTransactionReceiver());
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
		//Log.i("ACTIVITY", "formPaymentFragment initialized !");
		this.userSoldeFragment = (UserSoldeFragment) fm.findFragmentByTag(USER_BALANCE_FRAGMENT_TAG);
		if(this.userSoldeFragment == null)
			this.userSoldeFragment = UserSoldeFragment.newInstance();
		this.showFragment(INNER_HEADER_FRAG_CONTAINER_ID, this.userSoldeFragment);
		this.showFragment(INNER_LIST_VIEW_CONTAINER_ID, this.formPaymentFragment);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		switch(Integer.parseInt(uri.getFragment())){
			case R.id.payment_confirmation_button :
				if(this.formPaymentFragment.isTransactionValide()){
					this.showConfirmPaymentDialog();
				}
				break;
		}
	}

	@Override
	protected void initInnerContentLayout(ViewGroup container) {
		RelativeLayout.LayoutParams lp_userSoldeContainer = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.userBalanceContainer = new FrameLayout(this);
		this.userBalanceContainer.setLayoutParams(lp_userSoldeContainer);
		this.userBalanceContainer.setId(INNER_HEADER_FRAG_CONTAINER_ID);
		this.userBalanceContainer.setVisibility(View.VISIBLE);
		
		RelativeLayout.LayoutParams lp_innerListViewContainer = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp_innerListViewContainer.addRule(RelativeLayout.BELOW, INNER_HEADER_FRAG_CONTAINER_ID);
		this.innerScrollViewContainer = new ScrollView(this);
		this.innerScrollViewContainer.setId(INNER_LIST_VIEW_CONTAINER_ID);
		this.innerScrollViewContainer.setLayoutParams(lp_innerListViewContainer);
		
		container.addView(this.userBalanceContainer);
		container.addView(this.innerScrollViewContainer);
	}
	
	private void showConfirmPaymentDialog() {
		PaymentConfirmationDialogFragment confirmDialogFragment = new PaymentConfirmationDialogFragment();
		Bundle initialState = new Bundle();
		initialState.putInt(ServerHelper.TRANSACTION_AMOUNT_KEY, this.formPaymentFragment.getTransactionAmount());
		initialState.putString(ServerHelper.TRANSACTION_RECEIVER_KEY, this.formPaymentFragment.getTransactionReceiver());
		confirmDialogFragment.setArguments(initialState);
		confirmDialogFragment.show(getSupportFragmentManager(), "confirmation");
	}
	
	@Override
	public void onPaymentConfirmationDialogPositiveClick(PaymentConfirmationDialogFragment dialog) {
		Transaction transactionToPost = new Transaction(dialog.getTransactionReceiver(), dialog.getTransactionAmount());
		ServerHelper.postNewTransactions(transactionToPost, this);
	}

	@Override
	public void onPaymentConfirmationDialogNegativeClick(PaymentConfirmationDialogFragment dialog) {
		dialog.dismiss();
	}
	
	//callback du paiement
	@Override
	public Object call(BasicBSONObject bsonResponse){
		super.call(bsonResponse);
		//si la requête envoyée correspond à la création d'une nouvelle transaction (un post de transaction)
		if(bsonResponse.getString(ServerHelper.RESQUEST_TAG) == ServerHelper.POST_TRANSACTION_TAG){
			//on retourne sur la page "wallet"
			Intent walletActivity = new Intent(getApplicationContext(), WalletActivity.class);
			walletActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(walletActivity);	
			this.finish();
		}
		this.formPaymentFragment.hideLoader();
		return null;
	}
	
	//avant d'envoyer les reqêtes
	@Override
	public void beforeCall(){
		super.beforeCall();
		this.formPaymentFragment.showLoader();
	}
	
	//lorsqu'une erreur est survenue dans une requête
	@Override
	public void onError(Bundle errorObject){
		super.onError(errorObject);
		this.formPaymentFragment.hideLoader();
	}
	
	@Override
	protected void refresh(){
		this.userSoldeFragment.refreshData();
	}
}
