package fr.utt.if26.uttcoins;

import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.error.CustomErrorListener;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.formulaire.FormButtonFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormEmailFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPasswordFragment;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.server.bson.BasicBSONHttpRequest;
import fr.utt.if26.uttcoins.server.json.CustomJSONCallback;
import fr.utt.if26.uttcoins.server.json.JsonHttpRequest;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends ActionBarActivity implements OnFragmentInteractionListener, 
CustomBasicBSONCallback, CustomErrorListener{

	
	private TextView forgottenAccountLink;
	private ActionBar actionBar;
	private FormButtonFragment connexionBtnFragment;
	private FormEmailFragment loginInputFragment;
	private FormPasswordFragment passwordInputFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// setup references on views
		this.forgottenAccountLink = (TextView) findViewById(R.id.forgotten_account_link);
		this.connexionBtnFragment = (FormButtonFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_connexion_button);
		this.loginInputFragment = (FormEmailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_userLogin_input);
		this.passwordInputFragment = (FormPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_userPassword_input);
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
	
	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		//Log.v("CLICK", "click on : "+uri.toString());
		switch((Integer.parseInt(uri.getFragment()))){
			case R.id.form_submit_btn :
				this.sendLogin();
				break;
		}
	}
	
	private void startSignupActivity(){
		Intent loadSuActivity = new Intent(getApplicationContext(), SignupActivity.class);
		startActivity(loadSuActivity);		
	}
	
	private void sendLogin(){
		boolean isInputsValide = (this.loginInputFragment.isInputValide() && this.passwordInputFragment.isInputValide());
		if(isInputsValide){
			//Log.i("ACTION","CLICKED");
			ServerHelper.logUser(loginInputFragment.getValue(), passwordInputFragment.getValue(), ServerHelper.BSON_REQUEST, this);
		}
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
	
	@Override
	public Object call(BasicBSONObject bsonResponse) {
		this.connexionBtnFragment.hideLoader();
		//Log.i("REQUEST", bsonResponse.toString());
		if(bsonResponse.containsField(ServerHelper.SERVER_TOKEN_KEY)
				&& bsonResponse.containsField(ServerHelper.SERVER_EMAIL_KEY)){
			Intent loadWallet = new Intent(getApplicationContext(), WalletActivity.class);
			Bundle session = new Bundle();
			session.putString(ServerHelper.SERVER_TOKEN_KEY, bsonResponse.getString(ServerHelper.SERVER_TOKEN_KEY));
			session.putString(ServerHelper.SERVER_EMAIL_KEY, bsonResponse.getString(ServerHelper.SERVER_EMAIL_KEY));
			session.putString(ServerHelper.SERVER_TAG_KEY, bsonResponse.getString(ServerHelper.SERVER_TAG_KEY));			
			loadWallet.putExtra("session", session);
			startActivity(loadWallet);
		}
		return null;
	}
	

//	@Override
//	public Object call(JSONObject jsonResponse) {
//		this.connexionBtnFragment.hideLoader();
//		//Log.i("REQUEST", jsonResponse.toString());
//		if(jsonResponse.has(ServerHelper.SERVER_TOKEN_KEY) 
//				&& jsonResponse.has(ServerHelper.SERVER_EMAIL_KEY)){
//			Intent loadWallet = new Intent(getApplicationContext(), WalletActivity.class);
//			Bundle session = new Bundle();
//			try {
//				session.putString(ServerHelper.SERVER_TOKEN_KEY, jsonResponse.getString(ServerHelper.SERVER_TOKEN_KEY));
//				session.putString(ServerHelper.SERVER_EMAIL_KEY, jsonResponse.getString(ServerHelper.SERVER_EMAIL_KEY));
//				session.putString(ServerHelper.SERVER_TAG_KEY, jsonResponse.getString(ServerHelper.SERVER_TAG_KEY));
//				loadWallet.putExtra("session", session);
//				startActivity(loadWallet);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		return null;
//	}

	@Override
	public void beforeCall() {
		this.connexionBtnFragment.displayLoader();
	}

	@Override
	public void onError(Bundle errorObject) {
		this.connexionBtnFragment.hideLoader();
		this.showCustomErrorMessage(errorObject.getString(ErrorHelper.ERROR_TITLE_KEY), 
				errorObject.getString(ErrorHelper.ERROR_MSG_KEY));
	}
}
