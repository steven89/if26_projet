package fr.utt.if26.uttcoins;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;


public class TransactionsActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
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

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case android.R.id.home:
//				NavUtils.navigateUpFromSameTask(this);
//				return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
