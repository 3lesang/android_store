package com.android.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.store.fragment.CartFragment;
import com.android.store.fragment.DetailProductFragment;
import com.android.store.fragment.HistoryFragment;
import com.android.store.fragment.HistoryFragmentAdmin;
import com.android.store.fragment.OrderInfoFragment;
import com.android.store.fragment.ProductFragment;
import com.android.store.fragment.ProductFragmentAdmin;
import com.android.store.model.Order;
import com.android.store.model.Product;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class Admin extends AppCompatActivity {
    private List<Product> listCartProduct;
    private int countProduct;
    private AHBottomNavigation ahBotNavAdmin;
    private FragmentTransaction fragmentTransaction;
    EditText NameProduct,PriceProduct,DescriptionProduct,BrandProduct;
    ImageView imageProduct;
    Button btnInsertdata;
    DatabaseReference ProductDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initItem();
        setDataBotNavHome();
        NameProduct = findViewById(R.id.tv_detail_product_name_admin);
        PriceProduct = findViewById(R.id.tv_detail_product_price_admin);
        DescriptionProduct= findViewById(R.id.tv_detail_product_description_admin);
        //imageProduct = findViewById(R.id.img_detail_product_photo_admin);
        BrandProduct=findViewById(R.id.tv_detail_product_brand_admin);


        btnInsertdata= (Button)findViewById(R.id.btn_add_product_admin);

        ProductDbRef = FirebaseDatabase.getInstance().getReference().child("DBProduct");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_fragment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initItem() {
        ahBotNavAdmin = findViewById(R.id.ahbotnav_admin);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame_admin, new ProductFragmentAdmin());

        fragmentTransaction.commit();

    }
    private void setDataBotNavHome() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_product, R.drawable.ic_baseline_home_24, R.color.teal_200);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Đơn hàng", R.drawable.ic_baseline_history_24, R.color.yellow);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Thoát", R.drawable.ic_baseline_login_24, R.color.yellow);
        ahBotNavAdmin.addItem(item1);
        ahBotNavAdmin.addItem(item2);
        ahBotNavAdmin.addItem(item3);



        ahBotNavAdmin.setColored(false);

        ahBotNavAdmin.setDefaultBackgroundColor(getResources().getColor(R.color.white));

        ahBotNavAdmin.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position){
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame_admin, new ProductFragmentAdmin());
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame_admin, new HistoryFragmentAdmin());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        Intent intent = new Intent(Admin.this, MainActivity.class);
                        startActivity(intent);
                        break;


                }

                return true;
            }
        });
    }


    public void toDetailProductFragment(Product product){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame_admin, new DetailProductFragment(product,listCartProduct));
        fragmentTransaction.commit();
    }

    public void toOrderInfoFragment(Order orderInfo){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame_admin, new OrderInfoFragment(orderInfo));
        fragmentTransaction.addToBackStack(OrderInfoFragment.TAG);
        fragmentTransaction.commit();
    }
    public void onClick_AddProduct(View view)
    {
        Intent intent = new Intent(Admin.this, AdminProduct.class);
        startActivity(intent);
    }

}