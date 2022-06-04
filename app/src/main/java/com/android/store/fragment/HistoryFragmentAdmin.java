package com.android.store.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.store.Admin;
import com.android.store.MainActivity;
import com.android.store.R;
import com.android.store.adapter.HistoryProductAdapter;
import com.android.store.adapter.HistoryProductAdapterAdmin;
import com.android.store.model.DetailOrder;
import com.android.store.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragmentAdmin extends Fragment {

    private Admin home;
    private List<Order> listOrder;
    private List<DetailOrder> listDetailOrder;

    private View mView;
    private EditText edtHistoryPhone;
    private Button btnHistorySearch;
    private RecyclerView rcvHitorySearch;

    private HistoryProductAdapterAdmin historyProductAdapterAdmin;

    public HistoryFragmentAdmin() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // Khi quay lại từ fragment OrderInfo sẽ thực hiện tìm kiếm lại
        if (!edtHistoryPhone.getText().toString().trim().isEmpty()){
            findOrder();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_history, container, false);

        initItem();

        return mView;
    }

    private void initItem(){
        listOrder = new ArrayList<>();
        listDetailOrder = new ArrayList<>();

        home = (Admin) getActivity();

        historyProductAdapterAdmin = new HistoryProductAdapterAdmin();

        edtHistoryPhone = mView.findViewById(R.id.edt_history_phone);

        rcvHitorySearch = mView.findViewById(R.id.rcv_hitory_search);
        btnHistorySearch = mView.findViewById(R.id.btn_history_search);
        btnHistorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOrder();
            }
        });
    }

    private void setDataHistoryProductAdapter(){
        historyProductAdapterAdmin.setData(listDetailOrder,listOrder,home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(home,RecyclerView.VERTICAL,false);
        rcvHitorySearch.setLayoutManager(linearLayoutManager);
        rcvHitorySearch.setAdapter(historyProductAdapterAdmin);
    }

    private void findOrder(){

        listOrder.clear();
        listDetailOrder.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBOrder");

        myRef.orderByChild("custPhone").equalTo(edtHistoryPhone.getText().toString())
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              historyProductAdapterAdmin.notifyDataSetChanged();
                for (DataSnapshot dataOrder : snapshot.getChildren()){
                    Order order = dataOrder.getValue(Order.class);
                    order.setOrderNo(dataOrder.getKey());
                    listOrder.add(order);
                }

                if (listOrder.size() > 0){
                    // Lấy thông tin detail order
                    findDetailOrder(myRef);
                }
                else {
                    Toast.makeText(getContext(),"Không tìm thấy lịch sử đặt hàng",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Không lấy được thông tin đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findDetailOrder( DatabaseReference myRef){
        if (listOrder.size() > 0){
            for (int i = 0; i<listOrder.size(); i++){
                Order order = listOrder.get(i);
                myRef.child(order.getOrderNo()).child("detail").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataDetail : snapshot.getChildren()){
                            historyProductAdapterAdmin.notifyDataSetChanged();
                            DetailOrder detailOrder = dataDetail.getValue(DetailOrder.class);
                            listDetailOrder.add(detailOrder);
                        }

                        // set data HistoryProductAdapter
                        if (listDetailOrder.size() > 0){
                            setDataHistoryProductAdapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),"Không lấy được chi tiết đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}