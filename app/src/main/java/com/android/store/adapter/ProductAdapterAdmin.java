package com.android.store.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.store.Admin;
import com.android.store.R;
import com.android.store.model.Product;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ProductViewHolderAdmin> {

    private List<Product> mListProduct;
    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private Admin home;


    /*public ProductAdapterAdmin(List<Product> mListProduct) {
        this.mListProduct = mListProduct;
    }*/

    public ProductAdapterAdmin() {

    }


    @NonNull
    @Override
    public ProductViewHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin,parent,false);
        return new ProductAdapterAdmin.ProductViewHolderAdmin(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderAdmin holder, @SuppressLint("RecyclerView") int position) {
        Product product = mListProduct.get(position);
        final String id = product.getId();

        if (product == null) {
            return;
        }
        else {
            Glide.with(holder.imgPhotoProduct.getContext()).load(product.getUrlImg()).into(holder.imgPhotoProduct);
            holder.tvProductName.setText(product.getProductName());
            holder.tvProductPrice.setText(formatPrice.format(product.getProductPrice()) + " VNĐ");

            holder.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence options[]=new CharSequence[]{
                            // select any from the value
                            "Delete",
                            "Cancel",
                    };
                    AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Delete Product");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if delete option is choosed
                            // then call delete function
                            if(which==0) {
                                deleteProduct(position,id);
                                mListProduct.remove(product);
                                notifyDataSetChanged();
                            }

                        }
                    });
                    builder.show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if(mListProduct != null)
            return mListProduct.size();
        return 0;
    }
    public void setData(List<Product> mList, Admin home) {
        this.mListProduct = mList;
        this.home = home;
        notifyDataSetChanged();
    }

    public  class ProductViewHolderAdmin extends RecyclerView.ViewHolder{
        ImageView imgPhotoProduct;
        TextView tvProductName,tvProductPrice;
        MaterialButton btnDeleteProduct, btnEditProduct;
        public ProductViewHolderAdmin(@NonNull View itemView) {
            super(itemView);
            tvProductName= itemView.findViewById(R.id.tv_product_name_admin);
            tvProductPrice= itemView.findViewById(R.id.tv_product_price_admin);
            imgPhotoProduct = itemView.findViewById(R.id.img_photo_product_admin);
            btnDeleteProduct = itemView.findViewById(R.id.btn_delete_product_admin);
            btnEditProduct = itemView.findViewById(R.id.btn_edit_product_admin);
        }

    }

    private void deleteProduct(int position, String id) {

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("DBProduct");

        Query query = dbref.child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
