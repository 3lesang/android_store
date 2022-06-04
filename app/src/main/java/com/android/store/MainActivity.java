package com.android.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.store.fragment.CartFragment;
import com.android.store.fragment.DetailProductFragment;
import com.android.store.fragment.HistoryFragment;
import com.android.store.fragment.OrderInfoFragment;
import com.android.store.fragment.ProductFragment;
import com.android.store.model.Order;
import com.android.store.model.Product;
import com.android.store.ultil.CheckInternetConnection;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<Product> listCartProduct;
    private int countProduct;
    private AHBottomNavigation ahBotNavHome;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initItem();

        if(CheckInternetConnection.haveNetworkConnection(getApplicationContext()))
        {
            setDataBotNavHome();
        }
        else
        {
            CheckInternetConnection.ShowToast_Short(getApplicationContext(),"Check Internet Connection");
//            finish();
        }


    }

    private void initItem() {
        ahBotNavHome = findViewById(R.id.ahbotnav_home);
        if(listCartProduct == null){
            listCartProduct = new ArrayList<>();
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame, new ProductFragment());

        fragmentTransaction.commit();
    }


    private void setDataBotNavHome() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_product, R.drawable.ic_baseline_home_24, R.color.teal_200);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_cart, R.drawable.ic_baseline_add_shopping_cart_24, R.color.gray);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_history, R.drawable.ic_baseline_history_24, R.color.yellow);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("login", R.drawable.ic_baseline_account_box_24, R.color.teal_200);

        ahBotNavHome.addItem(item1);
        ahBotNavHome.addItem(item2);
        ahBotNavHome.addItem(item3);
        ahBotNavHome.addItem(item4);

        ahBotNavHome.setColored(false);

        ahBotNavHome.setDefaultBackgroundColor(getResources().getColor(R.color.white));

        ahBotNavHome.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame, new ProductFragment());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame, new CartFragment(listCartProduct));
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame, new HistoryFragment());
                        fragmentTransaction.commit();
                        break;
                    case 3:
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });
    }

    public void setCountProductInCart(int count){
        countProduct = count;
        AHNotification notification = new AHNotification.Builder()
                .setText(String.valueOf(count))
                .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                .build();
        ahBotNavHome.setNotification(notification, 1);
    }

    public void toDetailProductFragment(Product product){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame, new DetailProductFragment(product,listCartProduct));
        fragmentTransaction.commit();
    }

    public void toOrderInfoFragment(Order orderInfo){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame, new OrderInfoFragment(orderInfo));
        fragmentTransaction.addToBackStack(OrderInfoFragment.TAG);
        fragmentTransaction.commit();
    }

    public void addToListCartProdct(Product product){
        listCartProduct.add(product);
    }

    public List<Product> getListCartProduct() {
        return listCartProduct;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountForProduct(int possion, int countProduct){
        listCartProduct.get(possion).setNumProduct(countProduct);
    }

}