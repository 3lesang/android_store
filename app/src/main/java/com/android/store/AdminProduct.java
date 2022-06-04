package com.android.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.store.adapter.ProductAdapter;
import com.android.store.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminProduct extends AppCompatActivity {
    private RecyclerView rcvProduct;
    private ProductAdapter mProductAdapter;
    private List<Product> mListProduct;
    TextInputEditText NameProduct,PriceProduct,DescriptionProduct,BrandProduct,imageProduct;

    FloatingActionButton btnInsertdata;
    DatabaseReference ProductDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addproduct);
        NameProduct = findViewById(R.id.tv_detail_product_name_admin);
        PriceProduct = findViewById(R.id.tv_detail_product_price_admin);
        DescriptionProduct= findViewById(R.id.tv_detail_product_description_admin);
        imageProduct = findViewById(R.id.img_detail_product_photo_admin);
        BrandProduct=findViewById(R.id.tv_detail_product_brand_admin);


        btnInsertdata= findViewById(R.id.btn_add_product_admin);

        btnInsertdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertProductData();


                //Toast.makeText(AdminProduct.this, DescriptionProduct.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        ProductDbRef = FirebaseDatabase.getInstance().getReference().child("DBProduct");


    }
    public void NoticeProduct(){
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("key","tên sản phẩm:  "+NameProduct.getText().toString().trim()+"           giá: "+PriceProduct.getText().toString().trim()+  "  VNĐ");

        startService(intent);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }
    public void insertProductData(){
        if (BrandProduct.length() == 0 || NameProduct.length() == 0 || PriceProduct.length() == 0 || DescriptionProduct.length() == 0 || imageProduct.length() == 0) {
            Toast.makeText(AdminProduct.this, "điền thiếu thông tin", Toast.LENGTH_SHORT).show();

        }
        else {

            String brand = BrandProduct.getText().toString();
            String ProductName = NameProduct.getText().toString();
            int price = Integer.parseInt(PriceProduct.getText().toString());
            String description= DescriptionProduct.getText().toString();
            String numProduct= "1";
            String urlImg= imageProduct.getText().toString();

            Product product = new Product(urlImg, ProductName, description, brand, price);
            ProductDbRef.push().setValue(product);
            Toast.makeText(AdminProduct.this, "data inserted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminProduct.this, Admin.class);
            startActivity(intent);

            NoticeProduct();
        }
    }

}
