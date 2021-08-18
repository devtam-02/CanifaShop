package com.example.canifa_shop.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canifa_shop.Login.Object.Accounts;
import com.example.canifa_shop.R;
import com.example.canifa_shop.SQLHelper.SQLHelper;
import com.example.canifa_shop.databinding.ActivityAcountManagerBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class AcountManagerActivity extends AppCompatActivity {
    ActivityAcountManagerBinding binding;
    private ImageView btnBack, btnAdd;
    private TextView tvTitile, tvDelete;
    SQLHelper sqlHelper;
    List<Accounts> accountsList;
    SharedPreferences sharedPreferences;
    private int ID;
    private Accounts accountsChoose;
    private String control = "";
    int cDay = 0, cMonth = 0, cYear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_acount_manager);
        findByViewID();
        initialization();
        getInten();
        setData();
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control.equals("create")) {
                    createAccount();
                } else {
                    updateAccout(accountsChoose);
                }
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processBirthday();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelper.deleteAccount(ID);
                finish();
            }
        });
    }

    public void processBirthday() {
        Calendar c = Calendar.getInstance();
        this.cDay = c.get(Calendar.DAY_OF_MONTH);
        this.cMonth = c.get(Calendar.MONTH);
        this.cYear = c.get(Calendar.YEAR);
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                binding.etDateOfBird.setText(arg3 + "/" + (arg2 + 1) + "/" + arg1);
            }
        };
        DatePickerDialog dateDialog = new DatePickerDialog(this, callBack, cYear, cMonth, cDay);
        dateDialog.setTitle("Choose the Birthday");
        dateDialog.show();
    }

    public void findByViewID() {
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        tvTitile = findViewById(R.id.tvTitle);
        tvDelete = findViewById(R.id.tvDelete);
        btnAdd.setVisibility(View.INVISIBLE);
        tvDelete.setVisibility(View.INVISIBLE);
        tvTitile.setText("Tài khoản");

    }

    public void initialization() {
        sqlHelper = new SQLHelper(getApplicationContext());
        accountsList = new ArrayList<>();
        accountsList = sqlHelper.getAllAccounts();
    }

    public void getInten() {
        Intent intent = getIntent();
        control += intent.getStringExtra("control");
        if (control != null && !control.equals("")) {
            if (control.equals("create")) {
                binding.btnUpdate.setText("Thêm mới");
                binding.etUserName.setEnabled(true);
            } else if (control.equals("update")) {
                ID = intent.getIntExtra("ID", 0);
                tvDelete.setVisibility(View.VISIBLE);
            } else {
                sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                ID = sharedPreferences.getInt("ID", 0);
            }
        }

    }

    public void setData() {
        for (Accounts accounts : accountsList) {
            if (accounts.getAccountID() == ID) {
                accountsChoose = accounts;
                binding.etDateOfBird.setText(accounts.getDateOfBirth());
                binding.etEmail.setText(accounts.getEmail());
                binding.etFullName.setText(accounts.getFullName());
                binding.etPassword.setText(accounts.getPassword());
                binding.etPhoneNumber.setText(accounts.getPhone());
                binding.etAddress.setText(accounts.getHomeTown());
                binding.etUserName.setText(accounts.getUserName());
            }
        }
    }

    public void updateAccout(Accounts accounts) {
        try {
            if (binding.etPhoneNumber.length() == 9) {
                if (checkEmail()==true) {
                    accounts.setDateOfBirth(binding.etDateOfBird.getText().toString());
                    accounts.setEmail(binding.etEmail.getText().toString());
                    accounts.setFullName(binding.etFullName.getText().toString());
                    accounts.setHomeTown(binding.etAddress.getText().toString());
                    accounts.setPassword(binding.etPassword.getText().toString());
                    accounts.setPhone(binding.etPhoneNumber.getText().toString());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Số điện thoại phải đủ 10 chữ số", Toast.LENGTH_SHORT).show();
            }
            sqlHelper.updateAccount(accounts);
        }catch (Exception e)
        {
            Toast.makeText(getBaseContext(), "Có lỗi nhập liệu", Toast.LENGTH_SHORT).show();
        }
    }

    public void createAccount() {
        try {
            String userName = binding.etUserName.getText().toString();
            String password = binding.etPassword.getText().toString();
            String fullName = binding.etFullName.getText().toString();
            String dateOfBirth = binding.etDateOfBird.getText().toString();
            String phone = binding.etPhoneNumber.getText().toString();
            String email = binding.etEmail.getText().toString();
            String homeTow = binding.etAddress.getText().toString();
            String avatar = "";
            String permission = "employee";
            if (userName.equals("") || password.equals("") || fullName.equals("") || dateOfBirth.equals("") || phone.equals("") || email.equals("") || homeTow.equals("")) {
                Toast.makeText(getBaseContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                if (checkAccount()==true) {
                    if (checkPassword()==true) {
                        if (phone.length() == 9) {
                            if (checkEmail()==true) {
                                Accounts accounts = new Accounts(0, userName, password, fullName, dateOfBirth, phone, email, homeTow, avatar, permission);
                                sqlHelper.insertAccount(accounts);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Số điện thoại phải đủ 10 chữ số", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Có lỗi nhập liệu", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkPassword() {
        String passPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";
        if (binding.etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
        }
        if (Pattern.matches(passPattern, binding.etPassword.getText().toString())) {
            return true;
        } else {
            Toast.makeText(getBaseContext(), "Mật khẩu có từ 8 ký tự bao gồm chữ hoa, chữ thường và số", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean checkEmail() {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (Pattern.matches(emailPattern, binding.etEmail.getText().toString())) {
            return true;
        } else {
            Toast.makeText(getBaseContext(), "Email không đúng định dạng '@gmail.com'", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean checkAccount() {
        List<Accounts> accountsArrayList = sqlHelper.getAllAccounts();
        for (Accounts acc : accountsArrayList) {
            if (acc.getUserName().equalsIgnoreCase(binding.etUserName.getText().toString())) {
                Toast.makeText(getBaseContext(), "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}