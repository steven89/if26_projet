package fr.utt.if26.uttcoins;

import fr.utt.if26.uttcoins.fragment.TransactionListFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPaiementFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
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

public class PaiementActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 3;
	
	private FormPaiementFragment formPaymentFragment;
	private static String FORM_PAYMENT_FRAGMENT_TAG = "formPaymentFragment";
	private ScrollView innerListViewContainer;
	private static final int INNER_LIST_VIEW_CONTAINER_ID = R.id.formPaymentFragContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("NAV", "PaiementActivity has started");
		Bundle data = this.getIntent().getExtras();
		Log.i("ACTIVITY", "got PaiementActivity's Bundle");
		if( data != null){
			String preSetTransactionReceiver = data.getString(TransactionListFragment.TRANSACTION_REVEIVER_KEY);
			int preSetTransactionAmount = data.getInt(TransactionListFragment.TRANSACTION_AMOUNT_KEY);
			Log.i("ACTIVITY", "preset var initialized");
			if(preSetTransactionReceiver != null)
				this.formPaymentFragment.setTransactionReceiver(preSetTransactionReceiver);
			if(preSetTransactionAmount != 0)
				this.formPaymentFragment.setTransactionAmount(preSetTransactionAmount);
		}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initInnerContentLayout(ViewGroup container) {
		//Class layoutParamsClass = Class.forName(container.getClass().getCanonicalName() + ".LayoutParams");
		//ViewGroup.LayoutParams lp_innerListViewContainer = new (layoutParamsClass.getClass().getConstructor(int, int))(FrameLayout.LayoutParams.FILL_PARENT, 
		//		FrameLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams lp_innerListViewContainer = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.innerListViewContainer = new ScrollView(this);
		this.innerListViewContainer.setId(INNER_LIST_VIEW_CONTAINER_ID);
		this.innerListViewContainer.setLayoutParams(lp_innerListViewContainer);
		container.addView(this.innerListViewContainer);
	}

}
