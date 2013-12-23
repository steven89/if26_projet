package fr.utt.if26.uttcoins.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.R;
import android.R.color;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerContentListAdapter extends ArrayAdapter<JSONObject>{
	public static final String iconId = "ICON_ID";
	public static final String categoryName = "CATEGORY_NAME";
	
	private Context context;
	private ArrayList<JSONObject> itemList;

	public NavDrawerContentListAdapter(Context context, ArrayList<JSONObject> itemList) {
		super(context, R.layout.nav_drawer_content_row, itemList);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.itemList = itemList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.nav_drawer_content_row, parent, false);

        // 3. Get the two text view from the rowView
        ImageView icone = (ImageView) rowView.findViewById(R.id.iconCategory);
        TextView catName = (TextView) rowView.findViewById(R.id.categoryName);
        Resources resManager = this.context.getResources();
        try {
			icone.setImageDrawable(resManager.getDrawable(this.itemList.get(position).getInt(iconId)));
			catName.setText(this.itemList.get(position).getString(categoryName));
			catName.setTextColor(Color.WHITE);
        } catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return rowView;
	}
}