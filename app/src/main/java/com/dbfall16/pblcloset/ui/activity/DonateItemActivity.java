package com.dbfall16.pblcloset.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.dbfall16.pblcloset.PBLApp;
import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.models.Item;
import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.MsgUtils;
import com.dbfall16.pblcloset.utils.NetworkUtil;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.dbfall16.pblcloset.utils.UserSessionUtils;
import com.dbfall16.pblcloset.utils.ValidationUtils;
import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DonateItemActivity extends AppCompatActivity {

    @BindView(R.id.donate_form_main)
    ScrollView donateMain;

    @BindView(R.id.add_image)
    Button addImageButton;

    @BindView(R.id.donate)
    Button donateButton;

    @BindView(R.id.description)
    EditText descriptionView;

    @BindView(R.id.color)
    EditText colorView;

    @BindView(R.id.item_type)
    EditText item_type_view;

    @BindView(R.id.size)
    EditText sizeView;

    @BindView(R.id.brand)
    EditText brandView;

    @BindView(R.id.image_cam)
    ImageView imageView;

    private View mProgressView;

    private DonateItemTask mDonateItemTask = null;

    private boolean isSuccess;
    private String userId;

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Point mSize;

    private Uri photoUri;

    private String photoUrl;

    //Global Variables
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_item);

        ButterKnife.bind(this);

        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);


        userId = PreferencesUtils.getString(this, AppConstants.USER_ID, "");

        mProgressView = findViewById(R.id.donate_progress);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            photoUri = data.getData();

            storageReference = storage.getReference();
            StorageReference storageReference1 = storageReference.child("Chat/Images/" + System.currentTimeMillis() + Math.random() * 1000 + ".png");

            if (photoUri != null) {
                UploadTask uploadTask = storageReference1.putFile(photoUri);
                showProgress(true);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgress(false);
                        Uri imageURI = taskSnapshot.getDownloadUrl();
                        if (imageURI != null)
                            photoUrl = imageURI.toString();
                    }
                });

                // Get the bitmap in according to the width of the device
                Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
                ((ImageView) findViewById(R.id.image_cam)).setImageBitmap(bitmap);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.image_cam)
    public void requestForCameraPermission(View view) {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(DonateItemActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DonateItemActivity.this, permission)) {
                showPermissionRationaleDialog("Please give permissions to continue using the app", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(DonateItemActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DonateItemActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(DonateItemActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            donateMain.setVisibility(show ? View.GONE : View.VISIBLE);
            donateMain.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    donateMain.setVisibility(show ? View.GONE : View.VISIBLE);
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
            donateMain.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @OnClick(R.id.donate)
    void onDonateClicked() {
        donateItem();
    }


    private void donateItem() {
        if (mDonateItemTask != null) {
            return;
        }

        // Reset errors.
        descriptionView.setError(null);
        colorView.setError(null);
        item_type_view.setError(null);
        sizeView.setError(null);
        brandView.setError(null);

        String description = descriptionView.getText().toString();
        String color = colorView.getText().toString();
        String itemType = item_type_view.getText().toString();
        String size = sizeView.getText().toString();
        String brand = brandView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (!ValidationUtils.checkValidity(description, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            descriptionView.setError(getString(R.string.error_enter_all_details));
            focusView = descriptionView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(color, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            colorView.setError(getString(R.string.error_enter_all_details));
            focusView = colorView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(itemType, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            item_type_view.setError(getString(R.string.error_enter_all_details));
            focusView = item_type_view;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(size, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            sizeView.setError(getString(R.string.error_enter_all_details));
            focusView = sizeView;
            cancel = true;
        }

        if (!ValidationUtils.checkValidity(brand, AppConstants.DATA_TYPE_GENERAL_TEXT, this)) {
            brandView.setError(getString(R.string.error_enter_all_details));
            focusView = brandView;
            cancel = true;
        }

        if (photoUrl == null) {
            cancel = true;
            focusView = imageView;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else

        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgressDialog("Loading...");
            showProgress(true);
            mDonateItemTask = new DonateItemTask(description, color, itemType, size, brand, photoUrl);
            mDonateItemTask.execute((Void) null);
        }
    }

    public class DonateItemTask extends AsyncTask<Void, Void, Boolean> {

        private final String description;
        private final String color;
        private final String itemType;
        private final String size;
        private final String brand;
        private final String picture_url;

        public DonateItemTask(String description, String color, String itemType, String size, String brand, String picture_url) {
            this.description = description;
            this.color = color;
            this.itemType = itemType;
            this.size = size;
            this.brand = brand;
            this.picture_url = picture_url;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: attempt authentication against a network service.
            isSuccess = false;
            try {
                // Simulate network access.
                if (NetworkUtil.getConnectivityStatusString(DonateItemActivity.this)) {
                    PBLApp.get().getPblApi().donate(userId, description, color, itemType, size, brand, picture_url, new Response.Listener<Item>() {
                        @Override
                        public void onResponse(Item response) {
                            if (response != null) {
                                isSuccess = true;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            MsgUtils.displayToast(DonateItemActivity.this, R.string.error_generic);
                        }
                    });
                } else {
                    MsgUtils.displayToast(DonateItemActivity.this, getString(R.string.error_internet_unavailable));
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return isSuccess;
        }

        private void saveData(Item response) {
            //UserSessionUtils.saveUserLoginData(LoginActivity.this, response);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mDonateItemTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                MsgUtils.displayToast(DonateItemActivity.this, R.string.error_generic_2);
            }
        }

        @Override
        protected void onCancelled() {
            mDonateItemTask = null;
            showProgress(false);
        }
    }

}
