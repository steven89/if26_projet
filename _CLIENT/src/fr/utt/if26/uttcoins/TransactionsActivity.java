package fr.utt.if26.uttcoins;

import android.os.Bundle;
import android.view.Menu;


public class TransactionsActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 2;
	
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
