package com.android.store.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.store.Admin;
import com.android.store.AdminProduct;
import com.android.store.Login;
import com.android.store.MainActivity;
import com.android.store.R;
import com.android.store.adapter.ProductAdapter;
import com.android.store.adapter.ProductAdapterAdmin;
import com.android.store.adapter.ProductSearchAdapter;
import com.android.store.adapter.SlidePhotoAdapter;
import com.android.store.db.ConnectFB;
import com.android.store.model.Product;
import com.android.store.model.SlidePhoto;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ProductFragmentAdmin extends Fragment {
    private Admin home;
    private Timer mTimer;
    private List<Product> listAllProduct;
    private View mView;
    private RecyclerView rcvProduct;
    private ProductAdapterAdmin productAdapterAdmin;
    private NestedScrollView nestedScrollViewProduct;

    private MaterialButton btnDeleteProductAdmin;
    private MaterialButton btnEditProductAdmin;
    private int limitProduct = 8;
    private int startProduct = 0;
    private int totalProduct;
    private DatabaseReference ref = new ConnectFB().getDBRef();
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_admin_product, container, false);
        initItem();
        listChange();
        setDataProductAdapter();
        return mView;
    }
    private void initItem(){

        rcvProduct = mView.findViewById(R.id.rcv_product_admin);
        nestedScrollViewProduct = mView.findViewById(R.id.scrollViewProductAdmin);
        btnDeleteProductAdmin = mView.findViewById(R.id.btn_delete_product_admin);
        btnDeleteProductAdmin = mView.findViewById(R.id.btn_edit_product_admin);
        listAllProduct = new ArrayList<Product>();
//        getProducts(listAllProduct);



        nestedScrollViewProduct.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
//                    limitProduct++;
                    // on below line we are making our progress bar visible.
//                    loadingPB.setVisibility(View.VISIBLE);
//                    if (startProduct < totalProduct/limitProduct) {
//                        getProducts(listAllProduct);
//                    }

                }
            }
        });
        home = (Admin) getActivity();
    }

    // Set Adapter cho rcvProduct
    private void setDataProductAdapter(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(home, 1);
        rcvProduct.setLayoutManager(gridLayoutManager);

        productAdapterAdmin = new ProductAdapterAdmin();
        productAdapterAdmin.setData(listAllProduct,home);

        rcvProduct.setAdapter(productAdapterAdmin);
    }


    private void getProducts(List<Product> products) {

        Query productListQuery = ref.limitToFirst(limitProduct).orderByChild("productName")
                .startAt(startProduct);

        productListQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                productAdapterAdmin.notifyDataSetChanged();

                for (DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    System.out.println(product.getProductName());
                    product.setId(data.getKey());
                    products.add(product);

                }
                startProduct += (limitProduct + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Không tải được dữ liệu từ firebase"
                        +databaseError.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ databaseError.toString());
            }

        });

    }

    public void listChange() {
//        List<Product> products = new ArrayList<>();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Product product = dataSnapshot.getValue(Product.class);
//                Log.d(TAG, "onChildAdded:" + product.getProductName());
                listAllProduct.add(product);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                Product product = dataSnapshot.getValue(Product.class);
                System.out.println(product.getProductName());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                Product product = dataSnapshot.getValue(Product.class);
                System.out.println(product.getProductName());
                Toast.makeText(getActivity(), "Xoa thanh cong" + product.getProductName(), Toast.LENGTH_SHORT).show();
                listAllProduct.remove(product);
                productAdapterAdmin.notifyDataSetChanged();
                productAdapterAdmin.setData(listAllProduct,home);

                rcvProduct.setAdapter(productAdapterAdmin);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        totalProduct = listAllProduct.size();
        System.out.println(totalProduct);
        ref.addChildEventListener(childEventListener);
    }





}
