package com.example.canifa_shop.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.example.canifa_shop.Bill.Adapter.BillListAdapter;
import com.example.canifa_shop.Bill.Object.Bill;
import com.example.canifa_shop.Manager.Adapter.ReceiptAdapter;
import com.example.canifa_shop.Manager.Object.Receipt;
import com.example.canifa_shop.Product.Object.Product;
import com.example.canifa_shop.R;
import com.example.canifa_shop.SQLHelper.SQLHelper;
import com.example.canifa_shop.databinding.ActivityWarehouseReportBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarehouseReportActivity extends AppCompatActivity {
    ActivityWarehouseReportBinding binding;
    List<Receipt> receiptList;
    List<Receipt> receiptListSearch;
    SQLHelper sqlHelper;
    ReceiptAdapter receiptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_warehouse_report);
        initialization();
        setAdapter(receiptListSearch);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                receiptListSearch.clear();
                binding.btnDelete.setVisibility(View.VISIBLE);
                for (Receipt receipt : receiptList) {
                    if (receipt.getIDReceipt() == Integer.valueOf(binding.edtSearch.getText().toString().trim())) {
                        receiptListSearch.add(receipt);
                    }
                }
                if (binding.edtSearch.getText().toString().equals("")) {
                    binding.btnDelete.setVisibility(View.INVISIBLE);
                    receiptListSearch = receiptList;
                }
                setAdapter(receiptListSearch);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.rvReceipt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WarehouseReportActivity.this, ImportProductActivity.class);
                intent.putExtra("ID", receiptListSearch.get(position).getIDReceipt());
                intent.putExtra("control","show");
                startActivity(intent);
            }
        });
    }

    public void initialization() {
        receiptList = new ArrayList<>();
        receiptListSearch = new ArrayList<>();
        sqlHelper = new SQLHelper(getApplicationContext());
        receiptList = sqlHelper.getAllReceipt();
        receiptListSearch = receiptList;
    }

    public void setAdapter(List<Receipt> receiptList) {
        Collections.reverse(receiptList);
        receiptAdapter = new ReceiptAdapter(receiptList);
        binding.rvReceipt.setAdapter(receiptAdapter);
    }
}