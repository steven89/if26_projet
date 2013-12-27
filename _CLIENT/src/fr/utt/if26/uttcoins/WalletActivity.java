package fr.utt.if26.uttcoins;

import fr.utt.if26.uttcoins.fragment.TransactionListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public class WalletActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 1;
	
	private TransactionListFragment transactionsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//pourrait être déporté dans la classe mère avec de la "reflexivité", mais ajoute trop de try/catch
		this.drawerList.setItemChecked(positionInDrawer, true);
		this.initFragments();
		this.showFragment(R.id.transaction_list_container, transactionsFragment);
	}

	protected void initFragments(){
		final FragmentManager fragManager = getSupportFragmentManager();
		//pour chaque frag dynamique
		transactionsFragment = (TransactionListFragment) fragManager.findFragmentByTag(TransactionListFragment.TAG);
		if(this.transactionsFragment == null)
			this.transactionsFragment = TransactionListFragment.newInstance();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

}
