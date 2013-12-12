package fr.utt.if26.uttcoins;

import org.json.JSONObject;

import fr.utt.if26.uttcoins.utils.JsonCallback;
import fr.utt.if26.uttcoins.utils.JsonHttpRequest;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends ActionBarActivity{

	private EditText loginInput;
	private EditText passwordInput;
	private TextView forgottenAccountLink;
	private Button connexionBtn;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// get references on views
		this.loginInput = (EditText) findViewById(R.id.login_input);
		this.passwordInput = (EditText) findViewById(R.id.password_input);
		this.forgottenAccountLink = (TextView) findViewById(R.id.forgotten_account_link);
		this.connexionBtn = (Button) findViewById(R.id.connexion_btn);
		// set Listeners on views
		this.connexionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JsonHttpRequest request = new JsonHttpRequest(new JsonCallback() {
					
					@Override
					public JSONObject call(JSONObject jsonResponse) {
						// TODO Auto-generated method stub
						try {
							if(!jsonResponse.getBoolean("error")){
								Intent loadWallet = new Intent(getApplicationContext(), WalletActivity.class);
								loadWallet.putExtra("token", jsonResponse.getString("token"));
								startActivity(loadWallet);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
				});
				Log.i("ACTION","CLICKED");
				request.execute("GET", "http://train.sandbox.eutech-ssii.com/messenger/login.php?email="+loginInput.getText().toString()+"&password="+passwordInput.getText().toString());
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.signup_action : 
				this.startSignupActivity();
				return true;
			case R.id.setting_action :
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void startSignupActivity(){
		Intent loadSuActivity = new Intent(getApplicationContext(), SignupActivity.class);
		startActivity(loadSuActivity);		
	}
}
