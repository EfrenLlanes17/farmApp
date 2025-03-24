package com.example.dinehero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

public class ProductPage extends AppCompatActivity {

    public TextView PPProductName;
    public TextView PPDiscription;

    public ImageView PPImage;
    public TextView PPPrice;
    private TextView PPPercentOff;
    private Button PPButton;
    private Button shareButton;
    private Button saveButton;
    private Button followingButton;
    private static User goingToUser;

    private TextView phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);


        Intent intent = getIntent();
        String text1 = intent.getStringExtra(ProductAdapter.EXTRA_TEXT);

        String text3 = intent.getStringExtra(ProductAdapter.EXTRA_TEXT3);
        //String text7 = intent.getStringExtra(ProductAdapter.EXTRA_TEXT7);
        Product temp = MainActivity2.findProduct(text1);
        boolean temp2 = ProfileActivity.getCurrentUserUsername().equals("");


        String text2 = temp.getProductDiscription();
        String text4 = temp.getProductPrice();
        int text5 = temp.getProductPercentOff();
        String text6 = temp.getProductLink();
        goingToUser =MainActivity2.findProduct(text1).getProductSeller();


        PPProductName = findViewById(R.id.txtPPName);
        PPDiscription = findViewById(R.id.txtPPDiscription);
        PPImage = findViewById(R.id.txtPPImage);
        PPPrice = findViewById(R.id.txtPPPrice);
        PPPercentOff = findViewById(R.id.txtPPPercentOff);
        PPButton = findViewById(R.id.sitePPButton);
        saveButton = findViewById(R.id.savePPButton);
        followingButton = findViewById(R.id.followPPButton);
        phone = findViewById(R.id.txtPhone);


        if(MainActivity2.findProduct(text1).getUserMadeEvent()){
            PPProductName.setText("You are Hosting");
        }
        else {
            PPProductName.setText(text1);
        }

        phone.setText(MainActivity2.findProduct(text1).getPhone());

        PPDiscription.setText(text2);
        PPPrice.setText(MainActivity2.findProduct(text1).getLocation());

        if(MainActivity2.findProduct(text1).getUserMadeEvent()){
            saveButton.setVisibility(View.INVISIBLE);
            followingButton.setVisibility(View.INVISIBLE);
            PPButton.setVisibility(View.INVISIBLE);
        }

        if(MainActivity2.findProduct(text1).getCost() == 0){

            PPPercentOff.setVisibility(View.INVISIBLE);
        }
        else{

            PPPercentOff.setVisibility(View.VISIBLE);
            PPPercentOff.setText("$" +MainActivity2.findProduct(text1).getCost() + ".00" );
        }

        if(MainActivity2.findProduct(text1).getUserMadeEvent()){
            PPImage.setImageURI(MainActivity2.findProduct(text1).getUriImage());

        }
        else {
            PPImage.setImageDrawable(ContextCompat.getDrawable(ProductPage.this,MainActivity2.findProduct(text1).getProductImage()));

        }








        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // attend

                Context context = view.getContext();
                Intent intent = new Intent(Intent.ACTION_SEND);
                String email = MainActivity2.findProduct(text1).getLocation() + "Main@" + MainActivity2.findProduct(text1).getLocation() + ".com";
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email.toString().replace(" ","")});
                intent.putExtra(Intent.EXTRA_SUBJECT, MainActivity2.findProduct(text1).getProductName() );
//                intent.putExtra(Intent.EXTRA_TEXT, "" + "Test");

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(intent, "Choose an email client"));
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + MainActivity2.findProduct(text1).getLoc());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps"); // Open in Google Maps app if available
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=Googleplex,Mountain+View,CA"));
                    startActivity(browserIntent);
                }
            }
        });

        PPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www." + MainActivity2.findProduct(text1).getWeb()));
                startActivity(intent);
            }
        });







    }

public void openUserPage(){
    Intent intent = new Intent(this, UserPage.class);
    this.startActivity(intent);
}
public static User goToUserProfile(){
        return  goingToUser;
}




}