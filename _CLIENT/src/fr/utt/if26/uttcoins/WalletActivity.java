package fr.utt.if26.uttcoins;

import fr.utt.if26.uttcoins.fragment.TransactionListFragment;
import fr.utt.if26.uttcoins.fragment.TransactionListFragment.OnTransactionListFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.UserSoldeFragment;
import fr.utt.if26.uttcoins.model.Transaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WalletActivity extends NavDrawerActivity implements OnTransactionListFragmentInteractionListener{

	public final static int positionInDrawer = 0;
	
	private TransactionListFragment transactionsListFragment;
	private UserSoldeFragment userSoldeFragment;
	
	private static final int USER_SOLDE_CONTAINER_ID = R.id.user_solde_container;
	private static final int TRANSACTION_LIST_CONTAINER_ID = R.id.transaction_list_container;
	
	private FrameLayout userSoldeContainer;
	private FrameLayout transactionListContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initInnerContentLayout(ViewGroup container) {
		//Log.i("CONTENT", "Initializing wallet content layout !");
		
		RelativeLayout.LayoutParams lp_userSoldeContainer = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.userSoldeContainer = new FrameLayout(this);
		this.userSoldeContainer.setLayoutParams(lp_userSoldeContainer);
		this.userSoldeContainer.setId(USER_SOLDE_CONTAINER_ID);
		this.userSoldeContainer.setVisibility(View.VISIBLE);
		
		RelativeLayout.LayoutParams lp_transactionListContainer = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp_transactionListContainer.addRule(RelativeLayout.BELOW, USER_SOLDE_CONTAINER_ID);
		this.transactionListContainer = new FrameLayout(this);
		this.transactionListContainer.setLayoutParams(lp_transactionListContainer);
		this.transactionListContainer.setId(TRANSACTION_LIST_CONTAINER_ID);
		this.transactionListContainer.setVisibility(View.VISIBLE);
		
		container.addView(userSoldeContainer);
		container.addView(transactionListContainer);
	}

	@Override
	protected void initFragments(){
		final FragmentManager fragManager = getSupportFragmentManager();
		//pour chaque frag dynamique
		this.transactionsListFragment = (TransactionListFragment) fragManager.findFragmentByTag(TransactionListFragment.TAG);
		if(this.transactionsListFragment == null)
			this.transactionsListFragment = TransactionListFragment.newInstance();
		this.userSoldeFragment = (UserSoldeFragment) fragManager.findFragmentByTag(UserSoldeFragment.TAG);
		if(this.userSoldeFragment == null)
			this.userSoldeFragment = UserSoldeFragment.newInstance();
		
		this.showFragment(USER_SOLDE_CONTAINER_ID, userSoldeFragment);
		this.showFragment(TRANSACTION_LIST_CONTAINER_ID, transactionsListFragment);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	@Override
	public void onFragmentInteraction(Uri uri) {
		switch(Integer.parseInt(uri.getFragment())){
		}
	}

	@Override
	public void onTransactionListFragmentInteraction(Transaction selected_transaction) {
		
	}

}
