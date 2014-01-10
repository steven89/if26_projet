package fr.utt.if26.uttcoins;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.Menu;
import android.view.ViewGroup;

public class PaiementActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wallet, menu);
		return true;
	}

	@Override
	protected void initFragments() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initInnerContentLayout(ViewGroup container) {
		// TODO Auto-generated method stub
		
	}

}
