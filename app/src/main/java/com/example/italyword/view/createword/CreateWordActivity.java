package com.example.italyword.view.createword;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.italyword.MainActivity;
import com.example.italyword.R;
import com.example.italyword.model.WordModel;
import com.example.italyword.viewmodel.WordViewModel;
import com.google.gson.Gson;

import java.io.IOException;

public class CreateWordActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    private ImageView selectImageView;
    private EditText englishEDt;
    private EditText italyWordET;
    private WordViewModel wordViewModel ;

    private Uri imageUri;
    private String imageFilePath ="" ;
    private Button deletBtn,saveBtn,updateBtn;


    private String tag =this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private  WordModel wordModelInent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);
        selectImageView =(ImageView) findViewById(R.id.selectImageView);
        englishEDt =(EditText) findViewById(R.id.englishEDt);
        italyWordET=(EditText) findViewById(R.id.italyWordET);
        deletBtn =(Button) findViewById(R.id.deletBtn);
        saveBtn =(Button) findViewById(R.id.saveBtn);
        updateBtn =(Button) findViewById(R.id.updateBtn);
        wordModelInent = new WordModel();
        progressDialog = new ProgressDialog(this);
        wordViewModel =new ViewModelProvider(this).get(WordViewModel.class);
        selectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        if(getIntent().getExtras() !=null){
            if(getIntent().getStringExtra("mo") !=null){
                Gson gson = new Gson();
                WordModel ob = gson.fromJson(getIntent().getStringExtra("mo"), WordModel.class);
                Log.d(tag,"word is -> "+ob.englishWord);
                wordModelInent =ob;

                Glide.with(this).load(ob.imageUrl).into(selectImageView) ;
                        englishEDt.setText(ob.englishWord);

                italyWordET.setText(ob.italyWord);
                updateBtn.setVisibility(View.VISIBLE);
                deletBtn.setVisibility(View.VISIBLE);

            }else{
                Log.d(tag,"word is -> getIntent().getStringExtra(\"mo\")=null ");
            }
        }else{
            saveBtn.setVisibility(View.VISIBLE);
            Log.d(tag,"word is -> getIntent().getExtra()=null ");
        }


    }


    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
       // startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        launchSomeActivity.launch(i);
    }


    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectImageView.setImageBitmap(
                                selectedImageBitmap);
                    }
                }
            });


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant

            Uri selectedImageUri = data.getData();
            imageFilePath = selectedImageUri.toString();
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data



                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    selectImageView.setImageURI(selectedImageUri);
                }
            }
        }
    }


    private  void updateWord(){
        progressDialog.setTitle("Updating....");
        progressDialog.show();


        wordModelInent.englishWord=englishEDt.getText().toString();
        wordModelInent.italyWord  =italyWordET.getText().toString();

        if(imageFilePath.length() >1){

            wordModelInent.imageUrl =imageFilePath;
        }
        wordViewModel.setImageUrl(wordModelInent);
        wordViewModel.getImageUrl().observe(this,blog->{
            if(blog.length() >1){

                Log.d(tag,"url is "+blog);

                if(blog.equals("success")){


                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                /*    Intent intent = new Intent(CreateWordActivity.this, MainActivity.class);
                    startActivity(intent);*/
                    onBackPressed();
                    finish();
                    italyWordET.setText("");
                    englishEDt.setText("");
                    selectImageView.setImageBitmap(null);
                    imageFilePath =null;

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(this, "fails", Toast.LENGTH_SHORT).show();
                }


            }else{  progressDialog.dismiss();

                Toast.makeText(this, "not  have url", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void deleteWord(WordModel model){
        wordViewModel.deleteWord(model).observe(this,blog ->{
            if(blog.equals("deleted success")){
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateWordActivity.this,MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(CreateWordActivity.this,MainActivity.class));
                finish();
                Toast.makeText(this, "deleting fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SaveDat(View view) {

        progressDialog.setTitle("Saving....");
        progressDialog.show();
        if(checkInput()){
            if(imageFilePath.length() >1){


                WordModel model = new WordModel();
                model.imageUrl=imageFilePath;
                model.italyWord =italyWordET.getText().toString();
                model.englishWord=englishEDt.getText().toString();

                wordViewModel.setImageUrl(model);
                wordViewModel.getImageUrl().observe(this,blog->{
                    if(blog.length() >1){

                        Log.d(tag,"url is "+blog);

                        if(blog.equals("success")){
                            progressDialog.dismiss();
                            italyWordET.setText("");
                                    englishEDt.setText("");
                                    selectImageView.setImageBitmap(null);
                            imageFilePath =null;
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();


                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(this, "fails", Toast.LENGTH_SHORT).show();
                        }

               /*

                        imageFilePath="";
                        model.imageUrl =blog;
                        wordRmViewModel.setSaveDataInSql(model);
                        wordRmViewModel.getSaveDataInSql().observe(this,blogs->{
                            if(blog.length() >1){
                                if(blog.equals("succes")){
                                    Toast.makeText(this, "Succes room", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(this, " not Succes room", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(this, "2 wrong in room", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }else{
                        Toast.makeText(this, "not  have url", Toast.LENGTH_SHORT).show();
                    }

                });

            }else{
                Toast.makeText(this, "image  null", Toast.LENGTH_SHORT).show();
            }
        }

        else{
            Toast.makeText(this, "fill all value", Toast.LENGTH_SHORT).show();
        }

     //   wordViewModel.getSaveDataInSql().removeObservers(this);
    }


   private boolean checkInput(){
       if(italyWordET.getText().toString().toString().length() <1){
           italyWordET.setError("!");
       }

       if(englishEDt.getText().toString().toString().length() <1){
           englishEDt.setError("!");
       }

       if(imageFilePath !=null && imageFilePath.length() <1){
          // Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
           imageFilePath ="https://firebasestorage.googleapis.com/v0/b/italyword.appspot.com/o/empty%2Fdownload.png?alt=media&token=67bd13c1-0976-4d09-9dbe-460f7d245be7";
       }
       if(imageFilePath ==null){
           imageFilePath ="https://firebasestorage.googleapis.com/v0/b/italyword.appspot.com/o/empty%2Fdownload.png?alt=media&token=67bd13c1-0976-4d09-9dbe-460f7d245be7";
       }

       if(italyWordET.getText().toString().toString().length() <1 ||
               englishEDt.getText().toString().toString().length() <1 ||
               (imageFilePath.length() <1 || wordModelInent ==null )
       ){
           return false;
       }else{
           return true;
       }

   }

    public void UpdateDate(View view) {
        if(checkInput()){
            updateWord();
        }

        else{
            Toast.makeText(this, "fill all value l", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeletData(View view) {
        if(wordModelInent !=null){
            deleteWord(wordModelInent);
        }else{
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateWordActivity.this,MainActivity.class));
            finish();
        }

    }
}