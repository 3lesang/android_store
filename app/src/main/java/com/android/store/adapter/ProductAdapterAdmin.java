package com.android.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.store.Admin;
import com.android.store.R;
import com.android.store.model.Product;
import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(@NonNull ProductViewHolderAdmin holder, int position) {
        Product product = mListProduct.get(position);

        if (product == null) {
            return;
        }
        else {
            Glide.with(holder.imgPhotoProduct.getContext()).load(product.getUrlImg()).into(holder.imgPhotoProduct);
            holder.tvProductName.setText(product.getProductName());
            holder.tvProductPrice.setText(formatPrice.format(product.getProductPrice()) + " VNĐ");

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

        public ProductViewHolderAdmin(@NonNull View itemView) {
            super(itemView);
            tvProductName= itemView.findViewById(R.id.tv_product_name_admin);
            tvProductPrice= itemView.findViewById(R.id.tv_product_price_admin);
            imgPhotoProduct = itemView.findViewById(R.id.img_photo_product_admin);
        }

    }
}
