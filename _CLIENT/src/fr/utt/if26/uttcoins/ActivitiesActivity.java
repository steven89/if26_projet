package fr.utt.if26.uttcoins;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivitiesActivity extends NavDrawerActivity {

	public final static int positionInDrawer = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//pourrait �tre d�port� dans la classe m�re avec de la "reflexivit�", mais ajoute trop de try/catch
		this.drawerList.setItemChecked(positionInDrawer, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

}
