package fr.utt.if26.uttcoins;

import org.bson.BasicBSONObject;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.error.CustomErrorListener;
import fr.utt.if26.uttcoins.fragment.OnFragmentInteractionListener;
import fr.utt.if26.uttcoins.fragment.TransactionListFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormButtonFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormEmailFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormInputFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormPasswordFragment;
import fr.utt.if26.uttcoins.fragment.formulaire.FormSimpleInputFragment;
import fr.utt.if26.uttcoins.model.User;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.server.bson.BasicBSONHttpRequest;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;

public class SignupActivity extends ActionBarActivity implements OnFragmentInteractionListener, 
CustomBasicBSONCallback, CustomErrorListener{

	private FormSimpleInputFragment nameInput, firstNameInput, tagInput;
	private FormEmailFragment emailInput; 
	private FormPasswordFragment passwordInput;
	private FormButtonFragment signUpBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		this.nameInput = (FormSimpleInputFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_name_input);
		this.nameInput.setHint(R.string.name_input_placeholder);
		this.firstNameInput = (FormSimpleInputFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_first_name_input);
		this.firstNameInput.setHint(R.string.first_name_input_placeholder);
		this.tagInput = (FormSimpleInputFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_tag_input);
		this.tagInput.setHint(R.string.tag_input_placeholder);
		this.emailInput = (FormEmailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_email_input);
		this.passwordInput = (FormPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_password_input);
		this.signUpBtn = (FormButtonFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_signup_btn);
		this.initFragments();
		this.showFragment(R.id.fragment_email_input, this.emailInput);
		this.showFragment(R.id.fragment_password_input, this.passwordInput);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		switch((Integer.parseInt(uri.getFragment()))){
			case R.id.form_submit_btn :
				this.signUp();
				break;
		}
	}
	
	protected void signUp(){
		if(this.isFormValide()){
			//String url = "http://10.0.2.2:8080/_SERVEUR/User";
//			String url = "http://88.186.76.236/_SERVEUR/User";
//			//BasicBSONHttpRequest request = new BasicBSONHttpRequest("POST", url, this);
//	        request.putParam("nom", this.nameInput.getValue());
//			request.putParam("prenom", this.firstNameInput.getValue());
//			request.putParam("email", this.emailInput.getValue());
//			request.putParam("pass", this.passwordInput.getValue());
//			request.putParam("tag", this.tagInput.getValue());
//			request.execute();
			Bundle userData = User.getFormatedData(this.nameInput.getValue(),
					this.firstNameInput.getValue(), 
					this.tagInput.getValue(), 
					this.emailInput.getValue(), 
					this.passwordInput.getValue());
			ServerHelper.signUp(userData, this);
		}else{
			this.showInvalidFormMessage();
		}
	}
	
	protected boolean isFormValide(){
		//Log.i("VALIDE", "email is valide ? "+this.emailInput.isInputValide());
		//Log.i("VALIDE", "email is valide ? "+this.passwordInput.isInputValide());
		return (this.emailInput.isInputValide() && this.passwordInput.isInputValide());
	}
	
	protected void showInvalidFormMessage(){
		String message = "";
		if(!this.emailInput.isInputValide()){
			message += "Les emails ne correspondent pas !\n";
		}
		if(!this.passwordInput.isInputValide()){
			message += "Les mots de passe ne correspondent pas !";
		}
		new AlertDialog.Builder(this)
		.setTitle("Erreur")
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
	
	protected void initFragments(){
		final FragmentManager fragManager = getSupportFragmentManager();
		//pour chaque frag dynamique
		this.emailInput = (FormEmailFragment) fragManager.findFragmentByTag(FormEmailFragment.TAG);
		if(this.emailInput == null)
			this.emailInput = FormEmailFragment.newInstance(true);
		this.passwordInput = (FormPasswordFragment) fragManager.findFragmentByTag(FormPasswordFragment.TAG);
		if(this.passwordInput == null)
			this.passwordInput = FormPasswordFragment.newInstance(true);
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
	public Object call(BasicBSONObject bsonResponse) {
		if(bsonResponse.getString(ServerHelper.RESQUEST_TAG) == ServerHelper.SIGN_UP_TAG){
			this.signUpBtn.hideLoader();
			NavUtils.navigateUpFromSameTask(this);
		}
		return null;
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
	public void beforeCall() {
		this.signUpBtn.displayLoader();
	}

	@Override
	public void onError(Bundle errorObject) {
		this.signUpBtn.hideLoader();
		this.showCustomErrorMessage(errorObject.getString(ErrorHelper.ERROR_TITLE_KEY), 
				errorObject.getString(ErrorHelper.ERROR_MSG_KEY));
	}
}
