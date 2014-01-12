package fr.utt.if26.uttcoins;

import org.bson.BSONObject;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.error.CustomErrorListener;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.formulaire.FormButtonFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormEmailFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPasswordFragment;
import fr.utt.if26.uttcoins.server.bson.BsonCallback;
import fr.utt.if26.uttcoins.server.bson.BsonHttpRequest;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
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
BsonCallback, CustomErrorListener{

	
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
		Log.v("CLICK", "click on : "+uri.toString());
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
			//String url = "http://10.0.2.2:8080/_SERVEUR/Login";
			String url = "http://88.186.76.236/_SERVEUR/Login";
			BsonHttpRequest request = new BsonHttpRequest("PUT", url, this);
			Log.i("ACTION","CLICKED");
			//String url = "http://train.sandbox.eutech-ssii.com/messenger/login.php?email="+loginInputFragment.getValue()+"&password="+passwordInputFragment.getValue();
			request.putParam("email", loginInputFragment.getValue());
			request.putParam("pass", passwordInputFragment.getValue());
			request.execute();
		}
	}
	
	public void showErrorMessage(String title, String message){
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
	public Object call(BSONObject bsonResponse) {
		this.connexionBtnFragment.hideLoader();
		if(bsonResponse.containsField("token")){
			Intent loadWallet = new Intent(getApplicationContext(), WalletActivity.class);
			loadWallet.putExtra("token", (String) bsonResponse.get("token"));
			startActivity(loadWallet);
		}
		return null;
	}

	@Override
	public void beforeCall() {
		this.connexionBtnFragment.displayLoader();
	}

	@Override
	public void onError(Bundle errorObject) {
		this.connexionBtnFragment.hideLoader();
		this.showErrorMessage(errorObject.getString(ErrorHelper.ERROR_TITLE_KEY), 
				errorObject.getString(ErrorHelper.ERROR_MSG_KEY));
	}
}
