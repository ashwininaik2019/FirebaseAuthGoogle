package com.example.firebaseauthgoogle;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    private static final int PICK_VIDEO_REQUEST = 3;

    //view objects
    private Button buttonChooseImage,buttonChooseVideo;
    private Button buttonUpload;
    private EditText editTextName,editTextTitle,editTextDesc;
   // private TextView textViewShow;
    private ImageView imageView;
    private VideoView vv;
    Spinner spinner;
    public String TAG ="MainActivity";

    private String videoname,Image_url_upload,Video_url_upload,item;

    File fileimage = null;

    //uri to store file
    private Uri filePath,videouri;

    private MediaController mc;

    FirebaseFirestore db;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload_image);

            buttonChooseImage = (Button) findViewById(R.id.buttonChooseImage);
            buttonChooseVideo = (Button) findViewById(R.id.buttonChooseVideo);
            buttonUpload = (Button) findViewById(R.id.buttonUpload);
            imageView = (ImageView) findViewById(R.id.imageView);
            vv=(VideoView)findViewById(R.id.VideoView);
            editTextName = (EditText) findViewById(R.id.editText);
            editTextTitle = (EditText) findViewById(R.id.editTexttitle);
            editTextDesc = (EditText) findViewById(R.id.editTextDesc);
            // textViewShow = (TextView) findViewById(R.id.textViewShow);

            spinner = (Spinner) findViewById(R.id.categry_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.category, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            //spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
            spinner.setOnItemSelectedListener(this);

           /* vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {*/
                            mc = new MediaController(UploadImageActivity.this);
                            vv.setMediaController(mc);
                            mc.setAnchorView(vv);
                            mc.setMinimumHeight(20);
                            vv.start();
                        /*}
                    });
                }
            });*/



            storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS).child("Videos");
            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

            buttonChooseImage.setOnClickListener(this);
            buttonChooseVideo.setOnClickListener(this);
            buttonUpload.setOnClickListener(this);
            // textViewShow.setOnClickListener(this);
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try
        {
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private void showFileImageChooser() {
        try {
           // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
         //   intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
           // final Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	      /*File image = getFile(this);
	       intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(image) );*/
           // if (intent.resolveActivity(getPackageManager()) != null) {

               // startActivityForResult(intent, PICK_IMAGE_REQUEST);

           // }
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }
    //public void showFileVideoChooser(View view)
    public void showFileVideoChooser()
    {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
           intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Check which request we're responding to
            if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                videouri = data.getData();
                vv.setVideoURI(videouri);
                videoname = getFileExtension(videouri);
            }

        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonChooseImage) {
            showFileImageChooser();
        } else if (view == buttonUpload) {

           // if(editTextTitle.getText().toString().trim()!=null && editTextDesc.getText().toString().trim()!=null && item=="Select") {
           //if(editTextTitle.getText().toString().trim()!= null && !editTextTitle.getText().toString().trim().equals("null"))
            if(TextUtils.isEmpty(editTextTitle.getText().toString()))
            {
                Toast.makeText(getApplicationContext(), "Please Enter title", Toast.LENGTH_SHORT).show();
            }
           else if(TextUtils.isEmpty(editTextDesc.getText().toString()))
            {
                Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
            }
            else if(item.equals("Select"))
            {
                Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
            }
            else if (filePath == null)
            {
                Toast.makeText(getApplicationContext(), "No Image selected,Please select a Image", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(editTextName.getText().toString()))
            {
                Toast.makeText(getApplicationContext(), "Please Enter Image name", Toast.LENGTH_SHORT).show();
            }
            else if(videouri == null)
            {
                Toast.makeText(getApplicationContext(), "No video selected,Please select a video", Toast.LENGTH_SHORT).show();

            }
        else
        {
            uploadImageFile();
            uploadDoc();
            UploadVideoFile();
         //   Toast.makeText(getApplicationContext(), " field entered", Toast.LENGTH_SHORT).show();
        }

        }
        else if (view == buttonChooseVideo) {
            showFileVideoChooser();
        }
    }

    private void uploadDoc()
    {
        try {
            db = FirebaseFirestore.getInstance();


                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Title", editTextTitle.getText().toString().trim());
                user.put("Description", editTextDesc.getText().toString().trim());
                user.put("Category", String.valueOf(spinner.getSelectedItem()));
                /*  user.put("born", 1912);*/

// Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }

    public String getFileExtension(Uri uri) {

            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();

            return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void UploadVideoFile()
    {
        if (videouri != null) {
        //getting the storage reference
        storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS).child("Videos");
       // StorageReference sRef = storageReference.child(videoname);
            StorageReference   sRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(videouri));
       // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
      //  StorageReference riversRef = storageRef.child("images/rivers.jpg");

        sRef.putFile(videouri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog
                       // progressDialog.dismiss();

                        //displaying success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_SHORT).show();
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrlvideo = urlTask.getResult();

                        Log.d(TAG, "onSuccess: firebase download url: " + downloadUrlvideo.toString()); //use if testing...don't need this line.
                        Video_url_upload =downloadUrlvideo.toString();
                        Upload upload = new Upload(editTextName.getText().toString(),Image_url_upload,Video_url_upload);

                        String uploadId = mDatabase.push().getKey();
                        mDatabase.child(uploadId).setValue(upload);


                        //Commented on 4dec2019
                           /* //creating the upload object to store uploaded image details
                            Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);*/
                        //Commented on 4dec2019
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                      //  progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                      //  progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    } else {
        //display an error if no file is selected
        Toast.makeText(getApplicationContext(), "No video selected,Please select a video ", Toast.LENGTH_SHORT).show();

    }
    }

    private void uploadImageFile() {
        try {
            // if(editTextName.getText().toString() !=null) {
            //checking if file is available
            if (filePath != null) {
                //displaying progress dialog while image is uploading
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                //getting the storage reference
                storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS).child("Images");
                StorageReference sRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filePath));

                //adding the file to reference
                sRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //dismissing the progress dialog
                                progressDialog.dismiss();

                                //displaying success toast
                                Toast.makeText(getApplicationContext(), "Files Uploaded ", Toast.LENGTH_SHORT).show();
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();

                                Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.

                                Image_url_upload =downloadUrl.toString();

                                Upload upload = new Upload(editTextName.getText().toString(),Image_url_upload,Video_url_upload);

                               // String uploadId = mDatabase.push().getKey();
                               // mDatabase.child(uploadId).setValue(upload);


                                //Commented on 4dec2019
                           /* //creating the upload object to store uploaded image details
                            Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);*/
                                //Commented on 4dec2019
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //displaying the upload progress
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });
            } else {
                //display an error if no file is selected
                Toast.makeText(getApplicationContext(), "No Image selected,Please select a Image ", Toast.LENGTH_SHORT).show();

            }
        /*}
         else
            {
                Toast.makeText(getApplicationContext(), "Please Enter a file name", Toast.LENGTH_LONG).show();
            }*/
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                videouri = data.getData();
                vv.setVideoURI(videouri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }*/

    @Override
    public void onBackPressed() {
        // do something on back.
        try
        {
            Intent goToNextActivity = new Intent(getApplicationContext(), ShowImagesActivity.class);
        	 goToNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToNextActivity);


        }
        catch(Exception e)
        {
            System.out.println("Exception UploadImageActivity onBackPressed " + e.getMessage());
//			onClickGoToHomePage();
        }
        catch(UnsatisfiedLinkError err)
        {
            err.getStackTrace();
            System.out.println("Error UploadImageActivity UnsatisfiedLinkError ");
//    		onClickGoToHomePage();
        }
    }
}
