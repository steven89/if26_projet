package fr.utt.if26.uttcoins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.utils.NavDrawerContentListAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class NavDrawerActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
	
	protected ArrayList<JSONObject> drawerListItem;
	protected DrawerLayout drawerLayout;
	protected ListView drawerList;
	protected ActionBarDrawerToggle drawerToggler;
	protected CharSequence title;
	protected final String[] categories = {
			"Activités", 
			"Mon argent", 
			"Mes transactions", 
			"Nouveau paiement"
			};
	protected final int[] icons = {
			R.drawable.test_icon, // icone de test pour "Activités"
			R.drawable.test_icon, // icone de test pour "Mon argent"
			R.drawable.test_icon, // icone de test pour "Mes transactions"
			R.drawable.test_icon, // icone de test pour "Nouveau paiement"
	};
	
	public final static int positionInDrawer = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		this.onCreateNavigationDrawer();
	}
	
	protected void onCreateNavigationDrawer() {
		this.drawerListItem = new ArrayList<JSONObject>();
		for(int i = 0 ; i < categories.length; i++){
			JSONObject data = new JSONObject();
			try{
				data.put(NavDrawerContentListAdapter.categoryName, categories[i]);
				data.put(NavDrawerContentListAdapter.iconId, icons[i]);
			}catch(JSONException e){
				e.printStackTrace();
			}
			drawerListItem.add(data);
		}
		this.title = getTitle();
		this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.drawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
		this.drawerList.setAdapter(new NavDrawerContentListAdapter(this, this.drawerListItem));
		this.drawerList.setOnItemClickListener(this);
		this.drawerToggler = new ActionBarDrawerToggle(this, this.drawerLayout, 
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
			
			/** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            	getSupportActionBar().setTitle(title);
            	supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            
		};
		this.drawerLayout.setDrawerListener(this.drawerToggler);
		this.drawerLayout.setFocusableInTouchMode(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wallet, menu);
		return true;
	}
	
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = this.drawerLayout.isDrawerOpen(this.drawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.drawerToggler.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggler.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (this.drawerToggler.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...
    	switch(item.getItemId()){
			case R.id.setting_action :
				return true;
			case R.id.logout_action :
				this.logout();
				Intent loadLogin = new Intent(getApplicationContext(), LoginActivity.class);
				loadLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(loadLogin);
				return true;
    	}
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//TODO que faire au click sur une categorie ? nouveau fragment ? nouvelle activity ?
		this.drawerList.setItemChecked(position, true);
		int refPositionInDrawer = -1;
		try {
			//récupérer la véritable valeur de "positionInDrawer", réecrite dans les classes filles
			refPositionInDrawer = this.getClass().getField("positionInDrawer").getInt(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.drawerList.getCheckedItemPosition()!=refPositionInDrawer){
			Log.v("positionInDrawer", Integer.toString(refPositionInDrawer));
			this.loadActivity(position);
		}
	}
	
	private void loadActivity(int position){
		//Traitement quelconque sur la BD ?
		Intent newActivity;
		Log.v("POSITION", Integer.toString(position));
		switch(position){
			case 0:
				newActivity = new Intent(getApplicationContext(), ActivitiesActivity.class);
				break;
			case 1:
				newActivity = new Intent(getApplicationContext(), WalletActivity.class);
				break;
			case 2:
				newActivity = new Intent(getApplicationContext(), TransactionsActivity.class);
				break;
			case 3:
				newActivity = new Intent(getApplicationContext(), PaiementActivity.class);
				break;
			default :
				//cas imprévu ? screw you
				newActivity = null;
				break;
		}
		if(newActivity != null)
			newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(newActivity);
			
	}

	@Override
	public void onBackPressed() {
		if(!this.drawerLayout.isDrawerOpen(Gravity.LEFT)){
			this.drawerLayout.openDrawer(Gravity.LEFT);
		}else{
			this.showExitMessage();
		}
	}
	
	public void showExitMessage(){
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Exiting the application")
		.setMessage("Do you really want to quit this application (this will disconnect you) ?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		})
		.setNegativeButton("No", null)
		.show();
	}
	
	private void exitApplication(){
		this.logout();
		//autres trucs ?
	}
	
	private void logout(){
		Log.i("USER", "USER DISCONNECTED");
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Log.i("DATA", "REFRESH");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
//		//need un moyen plus directe et sécure de récupérer le nombre d'activité
//		ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE );
//		//récupère les 10 dernière running task (fonctionne à 99%, mais aucun moyen d'être
//		//certain que notre app soit dans les 10 dernières lancés)
//		List<RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
//		Iterator<RunningTaskInfo> itr = runningTaskInfoList.iterator();
//		while(itr.hasNext()){
//			RunningTaskInfo runningTaskInfo = (RunningTaskInfo)itr.next();
//			int id = runningTaskInfo.id;
//			if(id==getTaskId() && runningTaskInfo.numActivities <= 2){
//				this.exitApplication();
//			}
//		}
//		this.exitApplication();
	}
}
