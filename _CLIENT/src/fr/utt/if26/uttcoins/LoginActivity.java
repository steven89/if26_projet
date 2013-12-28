package fr.utt.if26.uttcoins;

import org.json.JSONObject;

import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.formulaire.FormButtonFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormEmailFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPasswordFragment;
import fr.utt.if26.uttcoins.utils.JsonCallback;
import fr.utt.if26.uttcoins.utils.JsonHttpRequest;
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

public class LoginActivity extends ActionBarActivity implements OnFragmentInteractionListener{

	
	private TextView forgottenAccountLink;
	private ActionBar actionBar;
	private FormButtonFragment connexionBtnFragment;
	private FormEmailFragment loginInputFragment;
	private FormPasswordFragment passwordInputFragment;
	
	private static final String emailPattern = "[a-zA-Z0-9]*@[a-zA-Z0-9]\\.[a-z]{0,5}";
	
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
				this.login();
				break;
		}
	}
	
	private void startSignupActivity(){
		Intent loadSuActivity = new Intent(getApplicationContext(), SignupActivity.class);
		startActivity(loadSuActivity);		
	}
	
	private void login(){
		boolean isInputsValide = (this.loginInputFragment.isInputValide() && this.passwordInputFragment.isInputValide());
		if(isInputsValide){
			JsonHttpRequest request = new JsonHttpRequest(new JsonCallback() {
				@Override
				public JSONObject call(JSONObject jsonResponse) {
					// TODO Auto-generated method stub
					try {
						connexionBtnFragment.hideLoader();
						if(!jsonResponse.has("error")){
							Intent loadWallet = new Intent(getApplicationContext(), WalletActivity.class);
							loadWallet.putExtra("token", jsonResponse.getString("token"));
							startActivity(loadWallet);
						}else{
							Log.e("REQUEST", jsonResponse.toString());
							showErrorMessage("Erreur", "Mot de passe ou identifiant invalide.");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			});
			Log.i("ACTION","CLICKED");
			this.connexionBtnFragment.displayLoader();
			//String url = "http://train.sandbox.eutech-ssii.com/messenger/login.php?email="+loginInputFragment.getValue()+"&password="+passwordInputFragment.getValue();
			String url = "http://localhost:8080/_SERVEUR/Login";
			request.putParam("email", loginInputFragment.getValue());
			request.putParam("pass", passwordInputFragment.getValue());
			request.execute("PUT", url);
		}
	}
	
	private void showErrorMessage(String Title, String Message){
		new AlertDialog.Builder(this)
		.setTitle(Title)
		.setMessage(Message)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.show();
	}
}
