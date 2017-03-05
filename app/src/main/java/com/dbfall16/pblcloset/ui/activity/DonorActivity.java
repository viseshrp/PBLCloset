package com.dbfall16.pblcloset.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.dbfall16.pblcloset.PBLApp;
import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.adapters.DonorItemsAdapter;
import com.dbfall16.pblcloset.models.Item;
import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.ui.views.MyRecyclerView;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.MsgUtils;
import com.dbfall16.pblcloset.utils.NetworkUtil;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonorActivity extends AppCompatActivity {


    @BindView(R.id.donor_item_list)
    MyRecyclerView mDonorItemList;

    @BindView(R.id.emptyListLayout)
    RelativeLayout mEmptyListLayout;

    @BindView(R.id.no_internet_layout)
    RelativeLayout mNoInternetLayout;

    @BindView(R.id.download_progress)
    View mProgressView;

    private DownloadDonatedList downloadDonatedList = null;

    private boolean isSuccess;
    private int numberOfRetries = -1;

    private DonorItemsAdapter donorItemsAdapter;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DonorActivity.this, DonateItemActivity.class));
            }
        });

        userId = PreferencesUtils.getString(this, AppConstants.USER_ID, "");

        setupListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            downloadDonatedList();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListView() {

        mDonorItemList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        mDonorItemList.setLayoutManager(mLayoutManger);
        donorItemsAdapter = new DonorItemsAdapter(this, new DonorItemsAdapter.ItemTapListener() {
            @Override
            public void onTap(Item item) {

            }
        });

        mDonorItemList.setAdapter(donorItemsAdapter);
    }

    private void downloadDonatedList() {
        if (downloadDonatedList != null) {
            return;
        }

        showProgress(true);
        downloadDonatedList = new DownloadDonatedList(this, userId);
        downloadDonatedList.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mDonorItemList.setVisibility(show ? View.GONE : View.VISIBLE);
            mDonorItemList.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDonorItemList.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mDonorItemList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showEmptyList(boolean value) {
        if (mEmptyListLayout != null && mDonorItemList != null) {
            if (value) {
                //Show empty list layout
                mEmptyListLayout.setVisibility(View.VISIBLE);
                mDonorItemList.setVisibility(View.GONE);
            } else {
                //Show list view;
                mEmptyListLayout.setVisibility(View.GONE);
                mDonorItemList.setVisibility(View.VISIBLE);
            }
        }
    }

    private void enableNoInternetView(boolean value) {
        if (mNoInternetLayout != null && mDonorItemList != null) {
            if (value) {
                mNoInternetLayout.setVisibility(View.VISIBLE);
                mDonorItemList.setVisibility(View.GONE);
            } else {
                mNoInternetLayout.setVisibility(View.GONE);
                mDonorItemList.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onResume() {
        super.onResume();
        downloadDonatedList();
    }

    public class DownloadDonatedList extends AsyncTask<Void, Void, Boolean> {

        private final String userId;
        private Context context;

        DownloadDonatedList(Context context, String userId) {
            this.context = context;
            this.userId = userId;
        }

        private void setDataset(ArrayList<Item> itemArrayList) {
            donorItemsAdapter.setDataset(itemArrayList);
            donorItemsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            isSuccess = false;
            try {
                if (NetworkUtil.getConnectivityStatusString(context)) {
                    //enableNoInternetView(false);
                    PBLApp.get().getPblApi().getDonatedList(((int)Double.parseDouble(userId))+"", new Response.Listener<PblResponse>() {
                        @Override
                        public void onResponse(PblResponse response) {
                            showProgress(false);
                            Gson gson = new Gson();
                            String c = new Gson().toJson(response.getResponse(), List.class);
                            Log.d("C message",c+"");
                            Type type = new TypeToken<List<Item>>(){}.getType();
                            /*ItemList itemList = gson.fromJson(c,type);
                            ItemList itemList = new ItemList();
                            ArrayList<Item> items = new ArrayList<Item>();*/
                            ItemList itemList = new ItemList();
                            itemList.setItemList((ArrayList<Item>)gson.fromJson(c,type));

                            //itemList.getItemList().addAll((List<Item>) response.getResponse());
                            //ItemList itemList = (ItemList) response.getResponse();
                            if (response != null && itemList.getItemList() != null && !itemList.getItemList().isEmpty()) {
                                isSuccess = true;
                                setDataset(itemList.getItemList());
                            } else {
                                //showProgress(false);
                                //showEmptyList(true);
                                isSuccess = false;
                            }
                            onResponseOne(isSuccess);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (numberOfRetries < 4) {
                                downloadDonatedList();
                                numberOfRetries += 1;
                            } else {
                                //showProgress(false);
                                //showEmptyList(true);
                                MsgUtils.displayToast(context, R.string.error_generic);
                                isSuccess = false;
                            }
                            onResponseOne(false);
                        }
                    });
                } else {
                    isSuccess = false;
                    //showProgress(false);
                    //enableNoInternetView(true);
                    MsgUtils.displayToast(context, R.string.error_internet_unavailable);
                    onResponseOne(false);
                }

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {
            downloadDonatedList = null;
            numberOfRetries = -1;
            showProgress(false);
        }

        void onResponseOne(boolean success){
            downloadDonatedList = null;
            showProgress(false);

            if (!success) {
                if (numberOfRetries < 4) {
                    downloadDonatedList();
                    numberOfRetries += 1;
                } else {
                    showEmptyList(true);
                    MsgUtils.displayToast(context, R.string.error_generic);
                }
            }
        }
    }
}
