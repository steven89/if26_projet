package fr.utt.if26.uttcoins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.adapter.NavDrawerContentListAdapter;
import fr.utt.if26.uttcoins.error.CustomErrorListener;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.server.json.CustomJSONCallback;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

//la classe m�re de toute activity contenant un menu de navigation lat�ral
public abstract class NavDrawerActivity extends ActionBarActivity 
implements AdapterView.OnItemClickListener, OnFragmentInteractionListener, CustomBasicBSONCallback, CustomErrorListener{
	
	protected ArrayList<JSONObject> drawerListItem;
	protected DrawerLayout drawerLayout;
	protected ListView drawerList;
	protected ActionBarDrawerToggle drawerToggler;
	protected CharSequence title;
	//listing des cat�gories du menu de navigation
	//apr�s reflexion, un listing des Class aurait �t� mieu
	protected static final String[] categories = {
			//"Activit�s", 
			"Mon argent", 
			//"Mes transactions", 
			"Nouveau paiement"
			};
	protected static final int[] icons = {
			//R.drawable.test_icon, // icone de test pour "Activit�s"
			R.drawable.test_icon, // icone de test pour "Mon argent"
			//R.drawable.test_icon, // icone de test pour "Mes transactions"
			R.drawable.test_icon, // icone de test pour "Nouveau paiement"
	};
	
	//identifie la classe dans le menu de navigation (par rapport au tableau "categories",
	//destin� � �tre override dans les classes filles.
	public final static int positionInDrawer = 0;
	
	protected abstract void initFragments();
	protected abstract void initInnerContentLayout(ViewGroup container);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		//si recoit des params (depuis le login notamment)
		Bundle session = (extras!=null) ? extras.getBundle("session") : null;
		if(session != null && session.containsKey(ServerHelper.SERVER_EMAIL_KEY) 
				&& session.containsKey(ServerHelper.SERVER_TAG_KEY) 
				&& session.containsKey(ServerHelper.SERVER_TOKEN_KEY))
			//on set la session
			ServerHelper.startSession(session);
		setContentView(R.layout.activity_default_nav_drawer);
		this.onCreateNavigationDrawer();
		this.initInnerContentLayout((ViewGroup) findViewById(R.id.content_layout));
		this.initFragments();
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
		this.drawerList.setItemChecked(this.getPositionInDrawer(), true);
	}
	
	//permet de r�cup�rer la position de la classe dans le menu de navigation 
	protected int getPositionInDrawer(){
		int realPositionInDrawer = 0;
		try {
			//un correspond � un "this.positionInDrawer", 
			//mais permet de s'assurer que le champs appel� est bien celui de la classe fille courrante
			realPositionInDrawer = this.getClass().getField("positionInDrawer").getInt(null);
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
		return realPositionInDrawer;
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
        //boolean drawerOpen = this.drawerLayout.isDrawerOpen(this.drawerList);
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
    protected void onPostResume(){
    	super.onPostResume();
    	if(ServerHelper.getSession() == null){
    		this.logout();
    	}
    		
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
				return true;
			case R.id.refresh_action : 
				this.refresh();
				return true;
    	}
        return super.onOptionsItemSelected(item);
    }

	protected void refresh() {		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//TODO que faire au click sur une categorie ? nouveau fragment ? nouvelle activity ?
		this.drawerList.setItemChecked(position, true);
		int refPositionInDrawer = -1;
		try {
			//r�cup�rer la v�ritable valeur de "positionInDrawer", r�ecrite dans les classes filles
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
			//Log.v("positionInDrawer", Integer.toString(refPositionInDrawer));
			this.loadActivity(position);
		}
	}
	
	private void loadActivity(int position){
		Intent newActivity;
		//Log.v("POSITION", Integer.toString(position));
		switch(position){
			case 0:
				newActivity = new Intent(getApplicationContext(), WalletActivity.class);
				break;
			case 1:
				newActivity = new Intent(getApplicationContext(), PaiementActivity.class);
				break;
//			case 2:
//				newActivity = new Intent(getApplicationContext(), TransactionsActivity.class);
//				break;
//			case 3:
//				newActivity = new Intent(getApplicationContext(), ActivitiesActivity.class);
//				break;
			default :
				//cas impr�vu ? screw you
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
	
	protected void showExitMessage(){
		final String callee = this.getClass().getCanonicalName();
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Exiting the application")
		.setMessage("Do you really want to quit this application (this will disconnect you) ?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Log.i("Class", callee);
				logout();
			}
		})
		.setNegativeButton("No", null)
		.show();
	}
	
	protected void exitApplication(){
		this.logout();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void logout(){
		ServerHelper.logout(this);
	}
	
	@Override
	public Object call(BasicBSONObject bsonResponse){
		if(bsonResponse.getString(ServerHelper.RESQUEST_TAG) == ServerHelper.LOGOUT_TAG){
			Intent loadLogin = new Intent(getApplicationContext(), LoginActivity.class);
			loadLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			this.startActivity(loadLogin);
			this.finish();
		}
		return null;
	}
	
	@Override
	public void onError(Bundle errorObject) {
		this.showCustomErrorMessage(errorObject.getString(ErrorHelper.ERROR_TITLE_KEY), 
				errorObject.getString(ErrorHelper.ERROR_MSG_KEY));
	}

	@Override
	public void beforeCall() {	
		
	}
	
	public void showCustomErrorMessage(String title, String message){
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.show();
	}
	
	protected int showFragment(int containerID, final Fragment fragment){
		if(fragment == null){
			return 0;
		}
		final FragmentManager fragManager = getSupportFragmentManager();
		final FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.replace(containerID, fragment);
		return fragTransaction.commit();
	}
	
	protected int removeFragment(final Fragment fragment){
		if(fragment == null){
			return 0;
		}
		final FragmentManager fragManager = getSupportFragmentManager();
		final FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.remove(fragment);
		return fragTransaction.commit();
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
		//Log.i("DATA", "REFRESH");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
//		//need un moyen plus directe et s�cure de r�cup�rer le nombre d'activit�
//		ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE );
//		//r�cup�re les 10 derni�re running task (fonctionne � 99%, mais aucun moyen d'�tre
//		//certain que notre app soit dans les 10 derni�res lanc�s)
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
