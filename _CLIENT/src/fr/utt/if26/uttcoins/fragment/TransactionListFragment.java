package fr.utt.if26.uttcoins.fragment;

import org.bson.BasicBSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import fr.utt.if26.uttcoins.PaiementActivity;
import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.R.layout;
import fr.utt.if26.uttcoins.adapter.TransactionListAdapter;
import fr.utt.if26.uttcoins.model.Transaction;
import fr.utt.if26.uttcoins.model.TransactionList;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.utils.ServerHelper;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TransactionListFragment extends Fragment implements AbsListView.OnItemClickListener,
CustomBasicBSONCallback{
	public static final String TAG = "TransactionFragment";
	public static final String UriPath = "application/TransactionFragment";
	
	private static final int TRANSACTION_LIST_ID = android.R.id.list;
	private static final int TRANSACTION_LIST_FRAGMENT_GROUP_ID = R.id.transaction_list_fragment_group;

	// TODO: Rename and change types of parameters
	private String mParam1;

	private OnTransactionListFragmentInteractionListener mListener;

	/**
	 * The fragment's ListView/GridView.
	 */
	private AbsListView mListView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private TransactionListAdapter mAdapter;

	// TODO: Rename and change types of parameters
	public static TransactionListFragment newInstance() {
		TransactionListFragment fragment = new TransactionListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		TransactionList.loadData();
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TransactionListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new TransactionListAdapter(getActivity(), TransactionList.ITEMS);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_transaction, container,
				false);
		// Set the adapter
		mListView = (AbsListView) view.findViewById(TRANSACTION_LIST_ID);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);
		registerForContextMenu(mListView);

		return view;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.getId() == TRANSACTION_LIST_ID){
		    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		    menu.setHeaderTitle("Transaction avec : "+TransactionList.ITEMS.get(info.position).getReceiver());
		    String[] menuItems = getResources().getStringArray(R.array.ctx_menu_transaction);
		    menu.add(TRANSACTION_LIST_FRAGMENT_GROUP_ID, R.id.new_transaction_action, 0, menuItems[0]);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		if(item.getGroupId() == TRANSACTION_LIST_FRAGMENT_GROUP_ID){
			Transaction selectedTransaction = TransactionList.ITEMS.get(info.position);
			switch(item.getItemId()){
				case R.id.new_transaction_action:
					Bundle args = new Bundle();
					args.putString(ServerHelper.TRANSACTION_RECEIVER_KEY, selectedTransaction.getReceiver());
					args.putInt(ServerHelper.TRANSACTION_AMOUNT_KEY, selectedTransaction.getAmount());
					this.goToPaymentActivity(args);
					break;
			}
		}
	  return true;
	}

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnTransactionListFragmentInteractionListener) activity;
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener.onTransactionListFragmentInteraction(TransactionList.ITEMS.get(position));
			this.showTransactionDetail(this.mAdapter.getItem(position));
			//Uri.parse("click://"+UriPath+"/transaction#"+TransactionList.ITEMS.get(position).id));
		}
	}
	
	private void showTransactionDetail(Transaction item) {
	}

	public void goToPaymentActivity(Bundle data){
		Intent loadPaymentActivity = new Intent(this.getActivity(), PaiementActivity.class);
		loadPaymentActivity.putExtras(data);
		//Log.i("NAV", "Starting PaiementActivity");
		startActivity(loadPaymentActivity);
	}
	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}
	
	public interface OnTransactionListFragmentInteractionListener{
		public void onTransactionListFragmentInteraction(Transaction selected_transaction);
	}

	@Override
	public Object call(BasicBSONObject bsonResponse) {
		if(bsonResponse.get(ServerHelper.RESQUEST_TAG) == ServerHelper.GET_TRANSACTION_TAG){
			this.mAdapter.notifyDataSetChanged();
		}
		return null;
	}

	@Override
	public void onError(Bundle errorObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeCall() {
		// TODO Auto-generated method stub
		
	}
}
