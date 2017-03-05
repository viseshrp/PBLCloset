package com.dbfall16.pblcloset.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.dbfall16.pblcloset.PBLApp;
import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.MsgUtils;
import com.dbfall16.pblcloset.utils.NetworkUtil;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.dbfall16.pblcloset.utils.UserSessionUtils;
import com.dbfall16.pblcloset.utils.ValidationUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private UserSignupTask mSignupTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private AutoCompleteTextView mEmailSignupView;
    private EditText mPasswordSignupView;

    private View mProgressView;
    private View mLoginFormView;
    protected Toolbar mToolbar;

    private boolean isSuccess;
    private String userType;

    @BindView(R.id.login_form)
    ScrollView loginForm;

    @BindView(R.id.signup_form)
    LinearLayout signupForm;

    @BindView(R.id.sign_up_button)
    Button signupButton;

    @BindView(R.id.sign_up_done)
    Button signupDoneButton;

    @BindView(R.id.first_name)
    EditText firstNameView;

    @BindView(R.id.second_name)
    EditText lastNameView;

    @BindView(R.id.address)
    EditText addressView;

    @BindView(R.id.city)
    EditText cityView;

    @BindView(R.id.state)
    EditText stateView;

    @BindView(R.id.zip)
    EditText zipView;

    @BindView(R.id.phone)
    EditText phoneView;

    @BindView(R.id.country)
    EditText countryView;

    @BindView(R.id.dob)
    EditText dobView;

    @BindView(R.id.checkbox_one)
    CheckBox sub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        userType = PreferencesUtils.getString(LoginActivity.this, AppConstants.USER_TYPE, "");

        if (userType.equals(AppConstants.USER_TYPE_BUYER))
            sub.setVisibility(View.VISIBLE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_login);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        // Set up the login form.
        mEmailSignupView = (AutoCompleteTextView) findViewById(R.id.email_signup);
        populateAutoComplete();

        mPasswordSignupView = (EditText) findViewById(R.id.password_signup);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @OnClick(R.id.sign_up_button)
    void onSignUpClicked() {
        signupButton.setVisibility(View.GONE);
        signupForm.setVisibility(View.VISIBLE);
        signupDoneButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.sign_up_done)
    void onSignUpDoneClicked() {
        attemptSignup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mToolbar != null)
            mToolbar.setTitle(PBLApp.get().getString(R.string.title_activity_sign_in));
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!ValidationUtils.checkValidity(password, AppConstants.DATA_TYPE_PASSWORD, this)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!ValidationUtils.checkValidity(email, AppConstants.DATA_TYPE_EMAIL, this)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.
            isSuccess = false;
            try {
                // Simulate network access.
                if (NetworkUtil.getConnectivityStatusString(LoginActivity.this)) {
                    PBLApp.get().getPblApi().login(mEmail,
                            mPassword, new Response.Listener<PblResponse>() {
                                @Override
                                public void onResponse(PblResponse response) {
                                    if (response != null) {
                                        //if (response.getStatus().equals(AppConstants.SUCCESS)) {
                                        isSuccess = true;
//                                        Log.e("success", "success");
//                                        User user = (User) response.getResponse();
//                                        MsgUtils.displayToast(LoginActivity.this, "Welcome" + " " + user.getFirstName());
//                                        saveData(user);

                                        Gson gson = new Gson();
                                        String c = new Gson().toJson(response.getResponse(), LinkedTreeMap.class);
                                        Type type = new TypeToken<User>(){}.getType();
                                        User user = gson.fromJson(c,type);
                                        Log.d("User",user.getEmail());
                                        //User user = (User) response.getResponse();
                                        MsgUtils.displayToast(LoginActivity.this, "Welcome" + " " + user.getFirstName());
                                        saveData(user);
                                        onSignUpResponse(isSuccess);
                                        /*} else {
                                            MsgUtils.displayToast(LoginActivity.this, response.getMessage());
                                        }*/
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error", "response");
                                    MsgUtils.displayToast(LoginActivity.this, R.string.error_generic);
                                }
                            });
                } else {
                    MsgUtils.displayToast(LoginActivity.this, getString(R.string.error_internet_unavailable));
                }

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return isSuccess;
        }

        private void saveData(User response) {
            UserSessionUtils.saveUserLoginData(LoginActivity.this, response);
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        void onSignUpResponse(Boolean success){
            mSignupTask = null;
            showProgress(false);

            if (success) {
                if (PreferencesUtils.getBoolean(LoginActivity.this, AppConstants.IS_LOGGED_IN, false)) {
                    if (userType.equals(AppConstants.USER_TYPE_DONOR)) {
                        startActivity(new Intent(LoginActivity.this, DonorActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    MsgUtils.displayToast(LoginActivity.this, "There was an error while logging you in.");
                }
            } else {
                MsgUtils.displayToast(LoginActivity.this, R.string.error_generic_2);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void attemptSignup() {
        if (mSignupTask != null) {
            return;
        }

        // Reset errors.
        mEmailSignupView.setError(null);
        mPasswordSignupView.setError(null);

        // Reset errors.
        firstNameView.setError(null);
        lastNameView.setError(null);
        addressView.setError(null);
        cityView.setError(null);
        stateView.setError(null);
        zipView.setError(null);
        phoneView.setError(null);
        countryView.setError(null);
        dobView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailSignupView.getText().toString();
        String password = mPasswordSignupView.getText().toString();

        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String address = addressView.getText().toString();
        String city = cityView.getText().toString();
        String state = stateView.getText().toString();
        String zip = zipView.getText().toString();
        String phone = phoneView.getText().toString();
        String country = countryView.getText().toString();
        String dob = dobView.getText().toString();

        boolean subValue = false;
        subValue = sub.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!ValidationUtils.checkValidity(password, AppConstants.DATA_TYPE_PASSWORD, this)) {
            mPasswordSignupView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordSignupView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!ValidationUtils.checkValidity(email, AppConstants.DATA_TYPE_EMAIL, this)) {
            mEmailSignupView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailSignupView;
            cancel = true;
        }


        if (!ValidationUtils.checkValidity(firstName, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            firstNameView.setError(getString(R.string.error_enter_all_details));
            focusView = firstNameView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(lastName, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            lastNameView.setError(getString(R.string.error_enter_all_details));
            focusView = lastNameView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(address, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            addressView.setError(getString(R.string.error_enter_all_details));
            focusView = addressView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(city, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            cityView.setError(getString(R.string.error_enter_all_details));
            focusView = cityView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(state, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            stateView.setError(getString(R.string.error_enter_all_details));
            focusView = stateView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(zip, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            zipView.setError(getString(R.string.error_enter_all_details));
            focusView = zipView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(phone, AppConstants.DATA_TYPE_PHONE_NUMBER, this)) {
            phoneView.setError(getString(R.string.error_enter_all_details));
            focusView = lastNameView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(country, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            countryView.setError(getString(R.string.error_enter_all_details));
            focusView = countryView;
            cancel = true;
        }

        //check with backend
        if (!ValidationUtils.checkValidity(dob, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            dobView.setError(getString(R.string.error_enter_all_details));
            focusView = dobView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mSignupTask = new UserSignupTask(firstName, lastName, address, city, state, zip, phone, country, dob, email, password, subValue);
            mSignupTask.execute((Void) null);
        }
    }

    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String firstName;
        private final String lastName;
        private final String address;
        private final String city;
        private final String state;
        private final String zip;
        private final String phone;
        private final String country;
        private final String dob;
        private final String email;
        private final String password;
        private final boolean sub;


        UserSignupTask(String firstName, String lastName, String address, String city, String state,
                       String zip, String phone, String country, String dob, String email, String password, boolean sub) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.phone = phone;
            this.country = country;
            this.dob = dob;
            this.sub = sub;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.
            isSuccess = false;
            try {
                // Simulate network access.
                if (NetworkUtil.getConnectivityStatusString(LoginActivity.this)) {
                    PBLApp.get().getPblApi().signup(userType, firstName, lastName, address, city, state, zip, phone, country,
                            dob, email, password, sub, new Response.Listener<PblResponse>() {
                                @Override
                                public void onResponse(PblResponse response) {
                                    if (response != null) {
                                        //if (response.getStatus().equals(AppConstants.SUCCESS)) {
                                        isSuccess = true;

                                        Log.e("success", "success");
                                        Gson gson = new Gson();
                                        String c = new Gson().toJson(response.getResponse(), LinkedTreeMap.class);
                                        Type type = new TypeToken<User>(){}.getType();
                                        User user = gson.fromJson(c,type);
                                        Log.d("User",user.getEmail());
                                        //User user = (User) response.getResponse();
                                        MsgUtils.displayToast(LoginActivity.this, "Welcome" + " " + user.getFirstName());
                                        saveData(user);
                                        onSignUpResponse(isSuccess);
                                        /*} else {
                                            MsgUtils.displayToast(LoginActivity.this, response.getMessage());
                                        }*/
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error", "response");
                                    MsgUtils.displayToast(LoginActivity.this, R.string.error_generic);
                                }
                            });
                } else {
                    MsgUtils.displayToast(LoginActivity.this, getString(R.string.error_internet_unavailable));
                }

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
/*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
*/

            // TODO: register the new account here.
            return isSuccess;
        }

        private void saveData(User response) {
            UserSessionUtils.saveUserLoginData(LoginActivity.this, response);
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        void onSignUpResponse(Boolean success){
            mSignupTask = null;
            showProgress(false);

            if (success) {
                if (PreferencesUtils.getBoolean(LoginActivity.this, AppConstants.IS_LOGGED_IN, false)) {
                    if (userType.equals(AppConstants.USER_TYPE_DONOR)) {
                        startActivity(new Intent(LoginActivity.this, DonorActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    MsgUtils.displayToast(LoginActivity.this, "There was an error while logging you in.");
                }
            } else {
                MsgUtils.displayToast(LoginActivity.this, R.string.error_generic_2);
            }
        }

        @Override
        protected void onCancelled() {
            mSignupTask = null;
            showProgress(false);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

