package com.android.store.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.store.MainActivity;
import com.android.store.R;
import com.android.store.adapter.OrderInfoAdapter;
import com.android.store.model.Order;

import java.text.DecimalFormat;


public class OrderInfoFragment extends Fragment {

    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    public static final String TAG = OrderInfoFragment.class.getName();
    private Order order;
    private MainActivity home;

    private View mView;
    private Button btnOrderInfoBack;
    private TextView tvOrderInfoNo,tvOrderInfoDate,tvOrderInfoCustName,tvOrderInfoCustAddress
            ,tvOrderInfoCustPhone,tvOrderInfoNum,tvOrderInfoTotal,tvOrderInfoStatus;
    private RecyclerView rcvOrderInfo;

    private OrderInfoAdapter orderInfoAdapter;

    public OrderInfoFragment(Order orderInfo) {
        order = orderInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_order_info, container, false);

        initItem();

        setContentOrder();

        setDataOrderInfoAdapter();

        return mView;
    }

    private void initItem(){
        orderInfoAdapter = new OrderInfoAdapter();
        home = (MainActivity) getActivity();
        tvOrderInfoNo = mView.findViewById(R.id.tv_order_info_no);
        tvOrderInfoDate = mView.findViewById(R.id.tv_order_info_date);
        tvOrderInfoCustName = mView.findViewById(R.id.tv_order_info_cust_name);
        tvOrderInfoCustAddress = mView.findViewById(R.id.tv_order_info_cust_address);
        tvOrderInfoCustPhone = mView.findViewById(R.id.tv_order_info_cust_phone);
        tvOrderInfoNum = mView.findViewById(R.id.tv_order_info_num);
        tvOrderInfoTotal = mView.findViewById(R.id.tv_order_info_total);
        tvOrderInfoStatus = mView.findViewById(R.id.tv_order_info_status);
        rcvOrderInfo = mView.findViewById(R.id.rcv_order_info);
        btnOrderInfoBack = mView.findViewById(R.id.btn_order_info_back);
        btnOrderInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void setContentOrder(){
        tvOrderInfoNo.setText(order.getOrderNo().toUpperCase());
        tvOrderInfoDate.setText(order.getDateOrder());
        tvOrderInfoCustName.setText(order.getCustName());
        tvOrderInfoCustAddress.setText(order.getCustAddress());
        tvOrderInfoCustPhone.setText(order.getCustPhone());
        tvOrderInfoNum.setText(String.valueOf(order.getNumProduct()));
        tvOrderInfoTotal.setText(formatPrice.format(order.getTotalPrice()) + "VNĐ");
        tvOrderInfoStatus.setText(order.getStatus());
    }

    private void setDataOrderInfoAdapter(){
        orderInfoAdapter.setData(order.getListDetailOrder());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(home,RecyclerView.VERTICAL,false);
        rcvOrderInfo.setLayoutManager(linearLayoutManager);
        rcvOrderInfo.setAdapter(orderInfoAdapter);
    }

}