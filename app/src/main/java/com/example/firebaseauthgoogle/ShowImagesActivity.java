package com.example.firebaseauthgoogle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


//import android.support.v7.app.AppCompatActivity;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;*/

public class ShowImagesActivity extends AppCompatActivity {

    public String TAG ="ShowImagesActivity";

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private NewAdapter adapter;

    private NewAdapter adapter1;

    private NewAdapter adapter2;

    Upload upload1;
    //database reference
    private DatabaseReference mDatabase;

    //database reference
    StorageReference mStorageReference;

    FirebaseStorage mFirebaseStorage;

    //progress dialog
   // private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;
    private List<String> s_upload,t_upload;
    private List<Upload> m_uploads;
    private List<String> listFinal;

    public String title_upload,name_upload,url_upload,desc_upload,category_upload,video_url;

    Upload shw_data;



    FirebaseFirestore db;
    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(base);
            MultiDex.install(this);
        }
        catch(Exception e)
        {
            System.out.println("Show Exception message" +e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        setupUI();
        setupAdd();
        /*getdbdoc();*/
           /* recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(ShowImagesActivity.this));*/


       // progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        /*s_upload = new ArrayList<>();
            t_upload= new ArrayList<>();
            m_uploads= new ArrayList<>();
            listFinal= new ArrayList<>();*/

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(ShowImagesActivity.this));

        //displaying progress dialog while fetching images
        /*progressDialog.setMessage("Please wait...");
        progressDialog.show();*/

            get_db_document();
            get_storage_document();
       //     get_database_doc();




       // mFirebaseStorage = FirebaseStorage.getInstance();
      //  mStorageReference = mFirebaseStorage.getReference().child(Constants.STORAGE_PATH_UPLOADS);


            /*uploads.add(shw_data);
            int s = uploads.size();
            // int s = listFinal.size();
            if(s == 0) {
                System.out.println("size" + s);
                Toast.makeText(ShowImagesActivity.this,"NO PRODUCTS TO DISPLAY", Toast.LENGTH_SHORT).show();
                //dismissing the progress dialog
                progressDialog.dismiss();
            }
            else {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                // adapter1 = new NewAdapter(ShowImagesActivity.this,listFinal,"hello");
                recyclerView.setAdapter(adapter);
                //dismissing the progress dialog
                progressDialog.dismiss();
            }*/
       // getdbdoc();
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }

    }

    public void get_db_document()
    {
        try
        {
            db = FirebaseFirestore.getInstance();
            // Code here executes on main thread after user presses button
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                    /*for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData().values());
                                        System.out.println("show data "+document.getData().get("Title"));
*/
                                //List<String> uploads = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("Title") != null) {

                                        //   Upload upload = new Upload(editTextName.getText().toString(), downloadUrl.toString());

                                        // Upload upload1 = new Upload(doc.getString("Title").toString());
                                        upload1 = new Upload(doc.getString("Title").toString());
                                        //Upload listData1=doc.toObject(Upload.class);
                                       /* Upload listData1=doc.toObject(Upload.class);
                                        //  String listData2= listData1.getTitle();
                                        s_upload.add(doc.getString("Title").toString());

                                        //using this gets the title value in list_data
                                        m_uploads.add(upload1);*/

                                        title_upload =doc.getString("Title").toString();
                                        desc_upload=doc.getString("Description").toString();
                                        category_upload=doc.getString("Category").toString();
                                        shw_data = new Upload(name_upload,url_upload,title_upload,desc_upload,category_upload,video_url);
                                        uploads.add(shw_data);
                                        //upload1.getTitle();
                                        //this code is used to merge title,name and url values in single list
                                        // uploads.add(upload1);
                                        // uploads.add(shw_data);
                                        //  uploads.set(0,upload1);

                                    }
                                            /*recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                                            adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                                            // adapter1 = new NewAdapter(ShowImagesActivity.this,listFinal,"hello");
                                            recyclerView.setAdapter(adapter);
                                            //dismissing the progress dialog
                                            progressDialog.dismiss();*/


                                }
                                //uploads.add(shw_data);
                                int s = uploads.size();
                                // int s = listFinal.size();
                                if(s == 0) {
                                    System.out.println("size" + s);
                                    Toast.makeText(ShowImagesActivity.this,"NO PRODUCTS TO DISPLAY", Toast.LENGTH_SHORT).show();
                                    //dismissing the progress dialog
                                  //  progressDialog.dismiss();
                                }
                                else {
                                    /*recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ShowImagesActivity.this));*/
                                     recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                                    adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                                    // adapter1 = new NewAdapter(ShowImagesActivity.this,listFinal,"hello");
                                    recyclerView.setAdapter(adapter);
                                    //dismissing the progress dialog
                                   // progressDialog.dismiss();
                                }
                                //adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                                // adapter2 = new NewAdapter(ShowImagesActivity.this,m_uploads);
                                /*adapter1 = new NewAdapter(ShowImagesActivity.this,s_upload,"hello");*/
                                // adapter1 = new NewAdapter(ShowImagesActivity.this,uploads,"hello","world");


                                /*TextView txt = findViewById(R.id.tvtitleval);
                                txt.setText((CharSequence) document.getData().get("Title"));*/
                                // }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }

 /*   public void get_database_doc()
    {
        try
        {
        db = FirebaseFirestore.getInstance();
        // Code here executes on main thread after user presses button
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().values());
                                System.out.println("show data "+document.getData().get("Title"));

                                //List<String> uploads = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("Title") != null) {

                                     //   Upload upload = new Upload(editTextName.getText().toString(), downloadUrl.toString());

                                       // Upload upload1 = new Upload(doc.getString("Title").toString());
                                         upload1 = new Upload(doc.getString("Title").toString());
                                        //Upload listData1=doc.toObject(Upload.class);
                                        Upload listData1=doc.toObject(Upload.class);
                                     //  String listData2= listData1.getTitle();
                                        s_upload.add(doc.getString("Title").toString());

                     //using this gets the title value in list_data
                                        m_uploads.add(upload1);

                                        title_upload =doc.getString("Title").toString();
                                       // Upload shw_data = new Upload(name_upload,url_upload,title_upload);
                                        //upload1.getTitle();
 //this code is used to merge title,name and url values in single list
                                        // uploads.add(upload1);
                                       // uploads.add(shw_data);
                                      //  uploads.set(0,upload1);

                                    }


                                }
                                //adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                               // adapter2 = new NewAdapter(ShowImagesActivity.this,m_uploads);
                                */
 /*adapter1 = new NewAdapter(ShowImagesActivity.this,s_upload,"hello");*/
    /*
                               // adapter1 = new NewAdapter(ShowImagesActivity.this,uploads,"hello","world");


                                *//*TextView txt = findViewById(R.id.tvtitleval);
                                txt.setText((CharSequence) document.getData().get("Title"));*//*
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    *//*db.collection("users").document()
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Upload u = documentSnapshot.toObject(Upload.class);
                muploads.add(u);
            }
        });*//*
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e.getMessage());
        }
    }*/

    public void get_storage_document()
    {
        try
        {
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload listData = postSnapshot.getValue(Upload.class);
                    /*t_upload.add(listData.getName().toString());
                    t_upload.add(listData.getUrl().toString());*/
                    name_upload = listData.getName().toString();
                    url_upload = listData.getUrl().toString();
                    video_url = listData.getvideo_url().toString();
                    //  shw_data = new Upload(listData.getName().toString(),listData.getUrl().toString(),title_upload);
                    shw_data = new Upload(name_upload, url_upload, title_upload, desc_upload, category_upload, video_url);
                    // uploads.add(listData);
                    //  uploads.add(shw_data);
                    /*listFinal.addAll(t_upload);
                    listFinal.addAll(s_upload);
                    System.out.println("listFinal : " + listFinal);*/
                    // uploads.addAll(m_uploads);
                }


                /*uploads.add(shw_data);
                int s = uploads.size();
               // int s = listFinal.size();
                if(s == 0) {
                    System.out.println("size" + s);
                    Toast.makeText(ShowImagesActivity.this,"NO PRODUCTS TO DISPLAY", Toast.LENGTH_SHORT).show();
                    //dismissing the progress dialog
                    progressDialog.dismiss();
                }
                else {
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                   // adapter1 = new NewAdapter(ShowImagesActivity.this,listFinal,"hello");
                    recyclerView.setAdapter(adapter);
                    //dismissing the progress dialog
                    progressDialog.dismiss();
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//dismissing the progress dialog
                //  progressDialog.dismiss();
                Toast.makeText(ShowImagesActivity.this, "NO PRODUCTS TO DISPLAY", Toast.LENGTH_SHORT).show();
            }
        });
    }
    catch(Exception e)

    {
        System.out.println("show Exception"+e.getMessage());
    }
    }

    public void setupAdd() {
        final Button buttondadd= findViewById(R.id.add_button);
        buttondadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent I = new Intent(ShowImagesActivity.this, UploadImageActivity.class);
                startActivity(I);
            }
        });
    }


    public void setupUI() {
        final Button buttondadd= findViewById(R.id.sign_out_button);
        buttondadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //signOut();
                showLogoutAlert();
            }
        });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent I = new Intent(ShowImagesActivity.this, SignInActivity.class);
        startActivity(I);
    }


    @Override
    public void onBackPressed() {
        // do something on back.
        try
        {
            showLogoutAlert();

        }
        catch(Exception e)
        {
            System.out.println("Exception ShowImagesActivity onBackPressed " + e.getMessage());
//			onClickGoToHomePage();
        }
        catch(UnsatisfiedLinkError err)
        {
            err.getStackTrace();
            System.out.println("Error ShowImagesActivity UnsatisfiedLinkError ");
//    		onClickGoToHomePage();
        }
    }
    public void showLogoutAlert()
    {
        try
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("Alert");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to logout ?");

            // On pressing Settings button
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,int which) {
                    //            	onClickLogOut();
                    signOut();
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        catch(Exception e)
        {
            e.getStackTrace();
            System.out.println("Exception ShowImagesActivity showLogoutAlert " + e.getMessage());
            //onClickLogOut();
        }
        catch(UnsatisfiedLinkError err)
        {
            err.getStackTrace();
            System.out.println("Error ShowImagesActivity UnsatisfiedLinkError showLogoutAlert ");
            //onClickLogOut();
        }
    }


}

