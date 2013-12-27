package fr.utt.if26.uttcoins;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PaiementActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//pourrait être déporté dans la classe mère avec de la "reflexivité", mais ajoute trop de try/catch
		this.drawerList.setItemChecked(positionInDrawer, true);
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

}
