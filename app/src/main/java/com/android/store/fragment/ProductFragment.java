package com.android.store.fragment;

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

    private int limitProduct = 8;
    private int startProduct = 1;
    public ProductFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        initItem();

        setDataSlidePhotoAdapter();

        setDataProductAdapter();

        ArrayList<Product> listProduct = new ArrayList<>();
        listProduct.add(new Product("https://img.fpt.shop/w_214/h_214/cmpr_10/m_letterbox_ffffff_100/https://fptshop.com.vn/Uploads/Originals/2020/8/5/637322682439532348_ss-note-20-ultra-5g-gold-dd.png", "Samsung Galaxy Note 20 Ultra", "Samsung Galaxy Note 20 Ultra được chế tác từ những vật liệu cao cấp hàng đầu hiện nay, với sự tỉ mỉ và chất lượng gia công thượng thừa, tạo nên chiếc điện thoại đẹp hơn những gì bạn có thể tưởng tượng. Không chỉ có kiểu dáng thanh lịch, màn hình không viền Infinity-O quyến rũ, Galaxy Note20 Ultra còn thể hiện sự cao cấp ở từng chi tiết nhỏ như các phần viền cạnh sáng bóng, họa tiết phay xước độc đáo trên khung máy, mang đến niềm cảm hứng cho người dùng ở mọi góc cạnh.", "samsung", 26990000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/206176/samsung-galaxy-note-10-plus-silver-new-600x600.jpg", "Samsung Galaxy Note 10+", "Trông ngoại hình khá giống nhau, tuy nhiên Samsung Galaxy Note 10+ sở hữu khá nhiều điểm khác biệt so với Galaxy Note 10 và đây được xem là một trong những chiếc máy đáng mua nhất trong năm 2019, đặc biệt dành cho những người thích một chiếc máy màn hình lớn, camera chất lượng hàng đầu.", "samsung", 16490000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/228744/iphone-12-pro-max-vang-new-600x600-600x600.jpg", "iPhone 12 Pro Max","iPhone 12 Pro Max 512GB - đẳng cấp từ tên gọi đến từng chi tiết. Ngay từ khi chỉ là tin đồn thì chiếc smartphone này đã làm đứng ngồi không yên bao “fan cứng” nhà Apple, với những nâng cấp vô cùng nổi bật hứa hẹn sẽ mang đến những trải nghiệm tốt nhất về mọi mặt mà chưa một chiếc iPhone tiền nhiệm nào có được.", "iphone", 41990000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/190325/iphone-xr-hopmoi-den-600x600-2-600x600.jpg", "iPhone XR 64GB","Là chiếc điện thoại iPhone có mức giá dễ chịu, phù hợp với nhiều khách hàng hơn, iPhone Xr vẫn được ưu ái trang bị chip Apple A12 mạnh mẽ, màn hình tai thỏ cùng khả năng chống nước chống bụi.", "iphone", 12190000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/232668/samsung-galaxy-z-fold-2-vang-600x600-600x600.jpg", "Samsung Galaxy Z Fold2 5G","Thuộc dòng smartphone cao cấp, Samsung Galaxy Z Fold2 5G được Samsung trau chuốt không chỉ vẻ ngoài sang trọng, tinh tế mà lẫn cả “nội thất” bên trong đầy mạnh mẽ khiến chiếc smartphone này hoàn toàn xứng đáng để được sở hữu.", "samsung", 50000000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/210644/iphone-11-128gb-green-600x600.jpg", "iPhone 11 128GB","Được xem là phiên bản iPhone \\\"giá rẻ\\\" trong bộ 3 iPhone mới ra mắt nhưng iPhone 11 128GB vẫn sở hữu cho mình rất nhiều ưu điểm mà hiếm có một chiếc smartphone nào khác sở hữu. Nâng cấp mạnh mẽ về cụm camera Năm nay với iPhone 11 thì Apple đã nâng cấp khá nhiều về camera nếu so sánh với chiếc iPhone Xr 128GB năm ngoái.", "iphone", 19490000));
        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/210644/iphone-11-128gb-green-600x600.jpg", "","iPhone SE 256GB 2020 cuối cùng đã được Apple ra mắt, với ngoại hình nhỏ gọn được sao chép từ iPhone 8 nhưng mang trong mình một hiệu năng mạnh mẽ với vi xử lý A13 Bionic, mức giá hấp dẫn hứa hẹn sẽ là yếu tố thu hút", "iphone", 19490000));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBProduct");
        for(Product product : listProduct) {
            myRef.push().setValue(product);
        }
        return mView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initItem(){
        rcvProduct = mView.findViewById(R.id.rcv_product);
        viewPagerSlidePhoto = mView.findViewById(R.id.vp_slide_photo);
        circleIndicator = mView.findViewById(R.id.circle_indicator);
        atcProductSearch = mView.findViewById(R.id.atc_product_search);
        nestedScrollViewProduct = mView.findViewById(R.id.scrollViewProduct);
        listSlidePhoto = getListSlidePhoto();
        listAllProduct = new ArrayList<Product>();
        getProducts(listAllProduct);

        nestedScrollViewProduct.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    limitProduct++;
                    // on below line we are making our progress bar visible.
//                    loadingPB.setVisibility(View.VISIBLE);
//                    if (count < 20) {
//                        // on below line we are again calling
//                        // a method to load data in our array list.
//
//                    }
                    getProducts(listAllProduct);
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

    private void getProducts(List<Product> products) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBProduct");
        Query productListQuery = myRef.limitToFirst(limitProduct).orderByChild("brand")
                .startAt(startProduct);
        List<Product> mListProduct = new ArrayList<>();
        productListQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                productAdapter.notifyDataSetChanged();

                for (DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    product.setId(data.getKey());
                    System.out.println(data.getKey());
                    products.add(product);

                }
                setProductSearchAdapter(mListProduct);
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


}