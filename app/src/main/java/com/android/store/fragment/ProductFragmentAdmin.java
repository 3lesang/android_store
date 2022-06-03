package com.android.store.fragment;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.store.Admin;
import com.android.store.MainActivity;
import com.android.store.R;
import com.android.store.adapter.ProductAdapter;
import com.android.store.adapter.ProductAdapterAdmin;
import com.android.store.adapter.ProductSearchAdapter;
import com.android.store.adapter.SlidePhotoAdapter;
import com.android.store.model.Product;
import com.android.store.model.SlidePhoto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ProductFragmentAdmin extends Fragment {
    private Admin home;
    private Timer mTimer;
    private List<SlidePhoto> listSlidePhoto;
    private List<Product> listAllProduct;

    private View mView;
    private RecyclerView rcvProduct;
    private ViewPager viewPagerSlidePhoto;
    private CircleIndicator circleIndicator;
    private AutoCompleteTextView atcProductSearch;

    private ProductAdapterAdmin productAdapterAdmin;
    private SlidePhotoAdapter slidePhotoAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_admin_product, container, false);
        // Khởi tạo các item
        initItem();

        // Set Adapter cho viewPagerSlidePhoto
        //setDataSlidePhotoAdapter();

        // Set Adapter cho rcvProduct
        setDataProductAdapter();
        return mView;
    }
    private void initItem(){
        rcvProduct = mView.findViewById(R.id.rcv_product_admin);
        viewPagerSlidePhoto = mView.findViewById(R.id.vp_slide_photo);
        circleIndicator = mView.findViewById(R.id.circle_indicator);
        atcProductSearch = mView.findViewById(R.id.atc_product_search);

        //listSlidePhoto = getListSlidePhoto();
        listAllProduct = getDataProduct();

        //home = (Admin) getActivity();
    }
    private void setDataSlidePhotoAdapter(){
        slidePhotoAdapter = new SlidePhotoAdapter(listSlidePhoto, this);
        viewPagerSlidePhoto.setAdapter(slidePhotoAdapter);
        circleIndicator.setViewPager(viewPagerSlidePhoto);
        slidePhotoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        // Auto chuyển các slide photo
        autoSildeImage();
    }

    private void autoSildeImage(){
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

    // Set Adapter cho rcvProduct
    private void setDataProductAdapter(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(home, 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        productAdapterAdmin = new ProductAdapterAdmin();
        productAdapterAdmin.setData(listAllProduct,home);

        rcvProduct.setAdapter(productAdapterAdmin);
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
    private List<Product> getDataProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBProduct");

        com.android.store.model.Product product = new com.android.store.model.Product("https://3g.co.uk/userfiles/products/n_1474-1.jpg", "iphone 12 pro", "day la iphone", "apple", 2000);
        myRef.child("1").setValue(product);
        List<com.android.store.model.Product> mListProduct = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productAdapterAdmin.notifyDataSetChanged();

                for (DataSnapshot data : snapshot.getChildren()){
                    com.android.store.model.Product product = data.getValue(com.android.store.model.Product.class);
                    product.setId(data.getKey());
                    mListProduct.add(product);
                }
                //setProductSearchAdapter(mListProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Không tải được dữ liệu từ firebase"
                        +error.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ error.toString());
            }
        });
        return mListProduct;
    }

}
