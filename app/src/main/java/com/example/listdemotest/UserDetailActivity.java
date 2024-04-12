package com.example.listdemotest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.listdemotest.databinding.UserDetailBinding;
import com.example.listdemotest.model.UserDetailModel;
import com.example.listdemotest.viewModel.UserDetailViewModel;


public class UserDetailActivity extends AppCompatActivity{
    private UserDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);
        UserDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.user_detail);
        viewModel = new ViewModelProvider(this).get(UserDetailViewModel.class);

        int id = getIntent().getIntExtra("id_user", -1);

        if (id != -1) {
            // Gọi phương thức để lấy thông tin người dùng dựa trên ID
            viewModel.callApiUser(id);
        } else {
            Log.e("UserDetailActivity", "Invalid id received");
        }

        viewModel.userDetail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                UserDetailModel userDetailModel = viewModel.userDetail.get();
                if (userDetailModel != null) {
                    // Load ảnh với Glide khi userDetail có dữ liệu
                    Glide.with(UserDetailActivity.this)
                            .load(userDetailModel.getAvatar_url())
                            .circleCrop()
                            .into(binding.imgAvatar1);

                    // Set userDetail vào binding để hiển thị dữ liệu trên layout
                    binding.setUserDetailViewModel(viewModel);

                    // Execute pending bindings
                    binding.executePendingBindings();
                }
            }
        });

        viewModel.getIsSuccessDetail().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean isSuccess = viewModel.getIsSuccessDetail().get();
                if (!isSuccess) {
                    // Xử lý khi có lỗi
                    Toast.makeText(UserDetailActivity.this, "Đã xảy ra lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    }








