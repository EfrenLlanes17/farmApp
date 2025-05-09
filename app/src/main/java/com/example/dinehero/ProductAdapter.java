package com.example.dinehero;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private ArrayList<Product> products = new ArrayList<>();
    public static final String EXTRA_TEXT = "com.example.dinehero.example.EXTRA_TEXT";

    public static final String EXTRA_TEXT3 = "com.example.dinehero.example.EXTRA_TEXT3";

    public static final String EXTRA_TEXT7 = "com.example.dinehero.example.EXTRA_TEXT7";

    private Context context;


    public ProductAdapter(Context context) {
     this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);



        return holder;



    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
       holder.txtName.setText(products.get(position).getProductName());

       holder.image.setImageDrawable(ContextCompat.getDrawable(context,products.get(position).getProductImage()));
        holder.txtPrice.setText(String.format("$%.2f", products.get(position).getPrice()));
       holder.txtPercentOff.setText(products.get(position).getDate());
       holder.txtSeller.setText(products.get(position).getLocation());

       if (products.get(position).getHasbeenSaved()){
           holder.pickUp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_location_on_24));
       }
       else{
           holder.pickUp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_local_shipping_24));

       }





       holder.btnDecline.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                Product p = products.get(position);
                products.remove(position);
                notifyDataSetChanged();

               Snackbar snackbar = Snackbar.make(view, "Item deleted", Snackbar.LENGTH_LONG)
                       .setAction("UNDO", new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               products.add(position,p);
                               notifyDataSetChanged();

                           }
                       });

               snackbar.setActionTextColor(Color.parseColor("#E0B05F"));

               snackbar.show();
           }
       });


       holder.btnAccept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
               SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
               Date date = null;
               try {
                   date = oldFormat.parse(products.get(position).getDate());
               } catch (ParseException e) {
                   throw new RuntimeException(e);
               }
               String formattedDate = newFormat.format(date);


               CalenderActivity.eventList.add(new Event(formattedDate,products.get(position).getProductName() + " to " + products.get(position).getLocation() + " at " + products.get(position).getLoc() + " for $" + products.get(position).getPrice(),"Order","None"));
               CalenderActivity.adapter = new EventAdapter(holder.txtSeller.getContext(), CalenderActivity.eventList);
               CalenderActivity.adapter.notifyDataSetChanged();
               Product p = products.get(position);
               products.remove(position);
               notifyDataSetChanged();
               Toast.makeText(holder.btnAccept.getContext(), "Order Added To Calender" , Toast.LENGTH_SHORT).show();

               Snackbar snackbar = Snackbar.make(view, "Order Confirmed", Snackbar.LENGTH_LONG)
                       .setAction("Cancel", new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               CalenderActivity.eventList.remove(0);
                               CalenderActivity.adapter.notifyDataSetChanged();
                               products.add(position,p);
                               notifyDataSetChanged();


                           }
                       });

               snackbar.setActionTextColor(Color.parseColor("#E0B05F"));

               snackbar.show();

           }
       });

       holder.image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String name = products.get(position).getProductName();
               int image = products.get(position).getProductImage();
              // String sell = products.get(position).getProductSeller().getUsername().toString();




               if(User.findUser() != null) {
                   if(User.findUser().isNotInViewedProducts(products.get(position))) {
                       User.findUser().addToViewedProducts(products.get(position));
                   }
                   for(int x = position-1; x >= 0; x--) {

                       if(User.findUser().isNotInViewedProducts(products.get(x))) {
                           User.findUser().addToViewedProducts(products.get(x));
                           notifyDataSetChanged();
                       }


                   }
               }

               openProductPage(name,image);


           }
       });

       holder.parent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


           }
       });


//       Glide.with(context)
//               .asBitmap()
//               .load(products.get(position).getProductImageURL())
//               .into(holder.image);
    }

    public void openProductPage(String text1,int text3){

        Intent intent = new Intent(context, ProductPage.class);
        intent.putExtra(EXTRA_TEXT,text1);

        intent.putExtra(EXTRA_TEXT3,text3);

        //intent.putExtra(EXTRA_TEXT7,text7);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if(products == null){
            return 1;
        }
        return products.size();
    }



    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
    public void addHashtags(ArrayList<String> stringArrayList){
        boolean temp = ProfileActivity.getCurrentUserUsername().equals("");
       for(int x = 0; x < stringArrayList.size();x++){

           if (!(temp)) {
              User.addToHashtagList(stringArrayList.get(x));
           }
       }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName, txtPrice, txtPercentOff, txtSeller;
        private CardView parent;
        private ImageView image;
        private TextView PPProductName;

        private Button btnContact;

        private Button btnAccept;

        private Button btnDecline;
        private ImageView pickUp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtPercentOff = itemView.findViewById(R.id.txtPercentOff);
            txtSeller = itemView.findViewById(R.id.txtSeller);
            parent = itemView.findViewById(R.id.parent);
            image = itemView.findViewById(R.id.txtImage);
            PPProductName = itemView.findViewById(R.id.txtPPName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            pickUp = itemView.findViewById(R.id.pickUpImage);

        }
    }
}
