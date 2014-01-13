package fr.utt.if26.uttcoins.fragment;

import org.bson.BasicBSONObject;

import com.mongodb.ServerError;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.error.CustomErrorListener;
import fr.utt.if26.uttcoins.model.User;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class UserSoldeFragment extends CustomFragment implements CustomBasicBSONCallback, CustomErrorListener{



	public static final String TAG = "UserSoldeFragment";

	public static final String UriPath = "application/TransactionFragment";


	// TODO: Rename and change types of parameters
	private String mParam1;

	private OnFragmentInteractionListener mListener;
	private TextView userAccountBalance;

	public static UserSoldeFragment newInstance() {
		UserSoldeFragment fragment = new UserSoldeFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public UserSoldeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_solde, container,
				false);
		this.userAccountBalance = (TextView) view.findViewById(R.id.user_solde_value);
		ServerHelper.getUserSolde(ServerHelper.BSON_REQUEST, this);
		return view;
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public Object call(BasicBSONObject bsonResponse) {
		if(bsonResponse.containsField(ServerHelper.SERVER_BALANCE_KEY)){
			this.userAccountBalance.setText(bsonResponse.getString(ServerHelper.SERVER_BALANCE_KEY));
		}else{
			Log.e("ERROR", "no balance in : " + bsonResponse.toString());
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
		new AlertDialog.Builder(this.getActivity())
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
}
