package com.example.laundry254.Customer;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundry254.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    //Storage

    StorageReference storageReference;
    //path where images of users profile and cover will be stored
    String storagePath = "Users_Profile_Cover_Imgs";


    //View from xml
    ImageView avatar, coverIv;
    TextView nameTv, emailTv,phoneTv;
    FloatingActionButton fab;

    //Progress Dialog
    ProgressDialog pd;

    // Permissions constants
    private  static final int CAMERA_REQUEST_CODE = 100;
    private  static final int STORAGE_REQUEST_CODE = 200;
    private  static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 400;
    private  static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    //Arrays of permissions to be requested
    String cameraPermission[];
    String storagePermission[];

    //Uri of pick image

    Uri image_Uri;

    //Check profile of cover photo
    String profileOrCoverPhoto;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();

        //Init  array of permission
        cameraPermission = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //init views
        avatar = view.findViewById(R.id.avatar);
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.emailTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        coverIv = view.findViewById(R.id.coverIv);
        fab = view.findViewById(R.id.fab);

        pd = new ProgressDialog(getActivity());

        Query query = reference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Check untill required data get

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

                    try{
                        Picasso.get().load(image).into(avatar);


                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.ic_default_img).into(avatar);

                    }
                    try{
                        Picasso.get().load(cover).into(coverIv);


                    }
                    catch (Exception e){

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fab button click

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfleDialog();
            }
        });

        return view;
    }
    private boolean checkStoragePermission(){
        boolean result  = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        requestPermissions( storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result  = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1  = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
       requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
    }

    private void showEditProfleDialog() {

        String options[] = {"Edit Profile Picture", "Edit Cover Photo" ,"Edit Name" ,"Edit Phone"};
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Choose action");
        //set Items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items clicks
                if ( which == 0){
                    //edit profile clicked
                    pd.setMessage("Update Profile Picture");
                    profileOrCoverPhoto = "image";
                    showImagePicDialog();

                }
                else if (which == 1){
                    //Edit Cover clicked
                    pd.setMessage("Update Cover photo");
                    profileOrCoverPhoto = "cover";
                    showImagePicDialog();


                }
                else if (which == 2){
                    //Edit Name clicked
                    pd.setMessage("Update Name");
                    showNamePhoneUpdateDialog("name");


                }
                else if (which == 3){
                    //Edit phone clicked
                    pd.setMessage("Update Phone");
                    showNamePhoneUpdateDialog("phone");


                }

            }
        });
        //Create and show dialog
        builder.show();
    }

    private void showNamePhoneUpdateDialog(final String key) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"  + key);
        //set layout of dialog

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter" + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        // Update in dialog button
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String value = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String ,Object > result = new HashMap<>();
                    result.put(key, value);

                    reference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    pd.dismiss();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), "Please enter" +key, Toast.LENGTH_SHORT).show();

                }


            }
        });

        //add button to cancel
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        //create and show dialog
        builder.create().show();


    }

    private void showImagePicDialog()  {

        String options[] = {"Camera", "Gallery" };
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Pick Image From");
        //set Items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items clicks
                if ( which == 0){
                    //Camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickFromCamera();
                    }

                }
                else if (which == 1){
                    //Gallery clicked
                    if (!checkStoragePermission()){

                        requestStoragePermission();
                    }else{
                        pickFromGallary();
                    }



                }

            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        //permission allowed
                        pickFromCamera();
                    }else{
                        //pemission denied
                        Toast.makeText(getActivity(), "Please enable camera & storage permission", Toast.LENGTH_SHORT).show();

                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if ( writeStorageAccepted){
                        //permission allowed
                        pickFromGallary();
                    }else{
                        //pemission denied
                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                image_Uri = data.getData();
                uploadProfileCoverPhoto(image_Uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){
                uploadProfileCoverPhoto(image_Uri); 


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri image_uri) {
        pd.show();

        //path and name of image to be stored

        String filepathAndName = storagePath + "" + profileOrCoverPhoto + "_" +user.getUid();

        StorageReference storageReference2nd = storageReference.child(filepathAndName);
        storageReference2nd.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()){
                            Uri downloadUri = uriTask.getResult();

                            // check if image is uploased or not
                            if(uriTask.isSuccessful()){
                                //image uploaded

                                //add update user in users database
                                HashMap<String, Object> results = new HashMap<>();
                                results.put(profileOrCoverPhoto,    downloadUri.toString());
                                reference.child(user.getUid()).updateChildren(results)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                pd.dismiss();

                                                Toast.makeText(getActivity(), "Image uploaded...", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                pd.dismiss();
                                                Toast.makeText(getActivity(), "Error Uploadin...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else{
                                pd.dismiss();
                                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        image_Uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE );
    }

    private void pickFromGallary() {

        ///Pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);

    }
}
