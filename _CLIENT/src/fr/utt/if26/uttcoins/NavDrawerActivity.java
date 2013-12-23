package fr.utt.if26.uttcoins;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.utils.NavDrawerContentListAdapter;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
			R.drawable.test_icon, // icone de test
			R.drawable.test_icon, // icone de test
			R.drawable.test_icon, // icone de test
			R.drawable.test_icon, // icone de test
	};
	
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

        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//TODO que faire au click sur une categorie ? nouveau fragment ? nouvelle activity ?
	}
}
