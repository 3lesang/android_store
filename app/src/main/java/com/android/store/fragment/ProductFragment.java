package com.android.store.fragment;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.store.MainActivity;
import com.android.store.R;
import com.android.store.adapter.ProductAdapter;
import com.android.store.adapter.ProductSearchAdapter;
import com.android.store.adapter.SlidePhotoAdapter;
import com.android.store.db.ConnectFB;
import com.android.store.model.Product;
import com.android.store.model.SlidePhoto;
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

public class ProductFragment extends Fragment {

    private MainActivity home;
    private Timer mTimer;
    private List<SlidePhoto> listSlidePhoto;
    private List<Product> listAllProduct;

    private View mView;
    private RecyclerView rcvProduct;
    private ViewPager viewPagerSlidePhoto;
    private CircleIndicator circleIndicator;
    private AutoCompleteTextView atcProductSearch;
    private NestedScrollView nestedScrollViewProduct;
    private ProductAdapter productAdapter;
    private SlidePhotoAdapter slidePhotoAdapter;
    private DatabaseReference ref = new ConnectFB().getDBRef();
    private int limitProduct = 6;
    private int startProduct = 0;
    private int totalProduct;
    public ProductFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product, container, false);


        initItem();
        listenFirebase();
        getProducts();
        setDataSlidePhotoAdapter();
        setDataProductAdapter();
        return mView;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initItem() {

        rcvProduct = mView.findViewById(R.id.rcv_product);
        viewPagerSlidePhoto = mView.findViewById(R.id.vp_slide_photo);
        circleIndicator = mView.findViewById(R.id.circle_indicator);
        atcProductSearch = mView.findViewById(R.id.atc_product_search);
        atcProductSearch.clearFocus();
        nestedScrollViewProduct = mView.findViewById(R.id.scrollViewProduct);
        listSlidePhoto = getListSlidePhoto();
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



//                    if (startProduct < totalProduct) {
//                        System.out.println(startProduct);
//                        getProducts(listAllProduct);
//                    }
//
//
//                    productAdapter.setData(listAllProduct,home);
//                    rcvProduct.setAdapter(productAdapter);
//                    setProductSearchAdapter(listAllProduct);
                }
            }
        });

        home = (MainActivity) getActivity();
    }


    private void setDataSlidePhotoAdapter(){
        slidePhotoAdapter = new SlidePhotoAdapter(listSlidePhoto, this);
        viewPagerSlidePhoto.setAdapter(slidePhotoAdapter);
        circleIndicator.setViewPager(viewPagerSlidePhoto);
        slidePhotoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoSlideImage();
    }

    private void autoSlideImage(){
        if(listSlidePhoto == null || listSlidePhoto.isEmpty() || viewPagerSlidePhoto == null){
            return;
        }
        if (mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPagerSlidePhoto.getCurrentItem();
                        int totalItem = listSlidePhoto.size() - 1;

                        // Nếu item hiện tại chưa phải cuối cùng
                        if(currentItem < totalItem){
                            currentItem++;
                            viewPagerSlidePhoto.setCurrentItem(currentItem);
                        }else {
                            viewPagerSlidePhoto.setCurrentItem(0);
                        }
                    }
                });
            }

            // xử lý thêm để set time
        },500,3000 );
    }

    private void setDataProductAdapter(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(home, 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter();
        productAdapter.setData(listAllProduct,home);

        rcvProduct.setAdapter(productAdapter);
    }

    private void setProductSearchAdapter(List<Product> listProduct ){
        ProductSearchAdapter productSearchAdapter = new ProductSearchAdapter(home,R.layout.item_search, listProduct);
        atcProductSearch.setAdapter(productSearchAdapter);

        // Sau khi chọn item search sẽ chuyển sang fragment detail
        atcProductSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                home.toDetailProductFragment(listProduct.get(position));
            }
        });
    }

    private List<SlidePhoto> getListSlidePhoto(){
        List<SlidePhoto> listSlidePhoto = new ArrayList<>();
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide1));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide2));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide3));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide4));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide5));
        return listSlidePhoto;
    }

    private void getProducts() {


        Query productListQuery = ref.limitToFirst(limitProduct).orderByChild("productName")
                .startAt(startProduct);

        productListQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

//                productAdapter.notifyDataSetChanged();
//
//                for (DataSnapshot data : snapshot.getChildren()){
//                    Product product = data.getValue(Product.class);
//                    product.setId(data.getKey());
//                    System.out.println(product.getNumProduct());
//                    products.add(product);
//
//                }
                setProductSearchAdapter(listAllProduct);
//                startProduct += (limitProduct + 1);
                startProduct += limitProduct;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Không tải được dữ liệu từ firebase"
                        +databaseError.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ databaseError.toString());
            }
        });

    }

    public void listenFirebase() {
//        List<Product> products = new ArrayList<>();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Product product = dataSnapshot.getValue(Product.class);
//                Log.d(TAG, "onChildAdded:" + product.getNumProduct());
                totalProduct++;
                listAllProduct.add(product);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

//                Product product = dataSnapshot.getValue(Product.class);
//                System.out.println(product.getProductName());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        ref.addChildEventListener(childEventListener);
    }




}