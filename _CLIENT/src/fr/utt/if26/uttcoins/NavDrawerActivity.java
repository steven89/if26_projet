package fr.utt.if26.uttcoins;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.utils.NavDrawerContentListAdapter;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.widget.ListView;

public class NavDrawerActivity extends Activity {
	
	protected ArrayList<JSONObject> drawerListItem;
	protected DrawerLayout drawerLayout;
	protected ListView drawerList;
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
		this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.drawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
		this.drawerList.setAdapter(new NavDrawerContentListAdapter(this, this.drawerListItem));
	}
}
