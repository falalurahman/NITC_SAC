package com.falalurahman.sacapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.falalurahman.sacapp.JavaBean.StoreItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 200;

    private static final String STOREFEED_REF = "StoreFeed";
    private static final String LOGO_FILE_PATH = "asset:///child1.jpg";
    private static final String IMAGE_FRAME_FILE_PATH = "asset:///add_image.png";
    private static final String UPLOAD_REQUEST_TAG = "UploadImageRequest";
    private static final String UPLOAD_REQUEST_USERNAME = "username";
    private static final String UPLOAD_REQUEST_IMAGE = "image";

    private static final String SHARED_PREF_PATH = "SACNITC";
    private static final String SHARED_PREF_USERNAME = "Username";
    private static final String SHARED_PREF_ROLLNO = "RollNo";

    private SimpleDraweeView Photo1;
    private SimpleDraweeView Photo2;
    private SimpleDraweeView Photo3;
    private SimpleDraweeView Photo4;
    private Uri fileUri;
    private String filePath;
    private ArrayList<String> imageFilePaths;

    private EditText StatusEditText;
    private EditText PhoneNumberEditText;
    private EditText ContactAddressEditText;
    private View ImagePreview;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private int pendingRequests;
    private ArrayList<String> imageUrls;

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SAC NITC");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("QBError", "Cannot Make Directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDraweeView ProfilePic = findViewById(R.id.profile_pic);
        ProfilePic.setImageURI(Uri.parse(LOGO_FILE_PATH));

        imageFilePaths = new ArrayList<>(4);
        Photo1 = findViewById(R.id.photo1);
        Photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPictureDialog(AddItemActivity.this, 0);
            }
        });
        Photo2 = findViewById(R.id.photo2);
        Photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPictureDialog(AddItemActivity.this, 1);
            }
        });
        Photo3 = findViewById(R.id.photo3);
        Photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPictureDialog(AddItemActivity.this, 2);
            }
        });
        Photo4 = findViewById(R.id.photo4);
        Photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPictureDialog(AddItemActivity.this, 3);
            }
        });
        updateImageView();

        View SubmitButton = findViewById(R.id.postButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkForError()) {
                    uploadItem();
                }
            }
        });

        StatusEditText = findViewById(R.id.status_editText);
        PhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        ContactAddressEditText = findViewById(R.id.contactAddressEditText);
        ImagePreview = findViewById(R.id.imagePreview);

        imageUrls = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

    }

    private void openPictureDialog(Context context, final int position) {
        final Dialog photoDialog = new Dialog(context);
        photoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photoDialog.setContentView(R.layout.menu_select_photo);

        View cameraPhoto = photoDialog.findViewById(R.id.camera_photo);
        cameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
                photoDialog.dismiss();
            }
        });

        View galleryPhoto = photoDialog.findViewById(R.id.gallery_photo);
        galleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                photoDialog.dismiss();
            }
        });

        View removePhoto = photoDialog.findViewById(R.id.remove_photo);
        if (position >= imageFilePaths.size()) {
            removePhoto.setVisibility(View.GONE);
        }
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(position);
                photoDialog.dismiss();
            }
        });

        photoDialog.show();
    }

    private void openCamera() {
        if (isDeviceSupportCamera()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri();
            filePath = fileUri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! Your Device doesn't support camera", Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
    }

    private boolean isDeviceSupportCamera() {
        return (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();
                    imageFilePaths.add(filePath);
                    updateImageView();
                }
            } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
                filePath = fileUri.getPath();
                imageFilePaths.add(filePath);
                updateImageView();
            }
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Someting went wrong!! Try Again", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteImage(int position) {
        imageFilePaths.remove(position);
        updateImageView();
    }

    private void updateImageView() {
        for (int i = 0; i < imageFilePaths.size(); i++) {
            String currFilePath = imageFilePaths.get(i);
            if (i == 0) {
                Photo1.setImageURI("file:///" + currFilePath);
            } else if (i == 1) {
                Photo2.setVisibility(View.VISIBLE);
                Photo2.setImageURI("file:///" + currFilePath);
            } else if (i == 2) {
                Photo3.setVisibility(View.VISIBLE);
                Photo3.setImageURI("file:///" + currFilePath);
            } else if (i == 3) {
                Photo4.setVisibility(View.VISIBLE);
                Photo4.setImageURI("file:///" + currFilePath);
            }
        }
        if (imageFilePaths.size() == 0) {
            Photo1.setVisibility(View.VISIBLE);
            Photo1.setImageURI(Uri.parse(IMAGE_FRAME_FILE_PATH));
        } else if (imageFilePaths.size() == 1) {
            Photo2.setVisibility(View.VISIBLE);
            Photo2.setImageURI(Uri.parse(IMAGE_FRAME_FILE_PATH));
        } else if (imageFilePaths.size() == 2) {
            Photo3.setVisibility(View.VISIBLE);
            Photo3.setImageURI(Uri.parse(IMAGE_FRAME_FILE_PATH));
        } else if (imageFilePaths.size() == 3) {
            Photo4.setVisibility(View.VISIBLE);
            Photo4.setImageURI(Uri.parse(IMAGE_FRAME_FILE_PATH));
        }
        for (int i = imageFilePaths.size() + 1; i < 4; i++) {
            if (i == 1) {
                Photo2.setVisibility(View.INVISIBLE);
            } else if (i == 2) {
                Photo3.setVisibility(View.INVISIBLE);
            } else if (i == 3) {
                Photo4.setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean checkForError() {
        if (StatusEditText.getText().toString().isEmpty()) {
            StatusEditText.setError("Item Description must be set");
            return true;
        } else if (PhoneNumberEditText.getText().toString().length() != 10) {
            PhoneNumberEditText.setError("Incorrect Phone Number");
            return true;
        } else if (ContactAddressEditText.getText().toString().isEmpty()) {
            ContactAddressEditText.setError("Contact Address must be set");
            return true;
        } else if (imageFilePaths.size() == 0) {
            Snackbar.make(ImagePreview, "Atleast One Image must be present", Snackbar.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void uploadItem() {
        showProgressDialog(AddItemActivity.this, "Uploading Images");
        pendingRequests = 0;
        for (int i = 0; i < imageFilePaths.size(); i++) {
            String currFilePath = imageFilePaths.get(i);
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file:///" + currFilePath));
                uploadImage(getStringImage(bitmapImage));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void uploadImage(final String imageString) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_PATH, MODE_PRIVATE);
        final String rollNumber = sharedPreferences.getString(SHARED_PREF_ROLLNO, null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.uploadImageURL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            pendingRequests--;
                            if (pendingRequests == 0) {
                                hideProgressDialog();
                                imageUrls.add(response);
                                uploadToFirebase();
                            } else {
                                imageUrls.add(response);
                            }
                        } else {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null) {
                            hideProgressDialog();
                        }
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error In Connection!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put(UPLOAD_REQUEST_USERNAME, rollNumber);
                params.put(UPLOAD_REQUEST_IMAGE, imageString);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(25 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(UPLOAD_REQUEST_TAG);
        requestQueue.add(stringRequest);
        pendingRequests++;
    }

    private String getStringImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
                requestQueue.cancelAll(UPLOAD_REQUEST_TAG);
                progressDialog = null;
            }
        });
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
        progressDialog = null;
    }

    private void uploadToFirebase() {
        StoreItem uploadItem = new StoreItem();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_PATH, MODE_PRIVATE);
        uploadItem.setUsername(sharedPreferences.getString(SHARED_PREF_USERNAME, null));
        uploadItem.setRollNo(sharedPreferences.getString(SHARED_PREF_ROLLNO, null));
        uploadItem.setTimeStamp(new Date().getTime());
        uploadItem.setMessage(StatusEditText.getText().toString());
        uploadItem.setPhoneNumber(PhoneNumberEditText.getText().toString());
        uploadItem.setContactAddress(ContactAddressEditText.getText().toString());
        uploadItem.setImageUrls(imageUrls);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(STOREFEED_REF);
        DatabaseReference itemRef = databaseReference.push();
        itemRef.setValue(uploadItem);

        NavUtils.navigateUpFromSameTask(this);
    }
}
