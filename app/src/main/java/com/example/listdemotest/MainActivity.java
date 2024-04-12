package com.example.listdemotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.listdemotest.adapter.MyAdapter;
import com.example.listdemotest.databinding.ActivityMainBinding;
import com.example.listdemotest.entity.UserEntity;
import com.example.listdemotest.model.UserModel;
import com.example.listdemotest.netword.NetworkUtils;
import com.example.listdemotest.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ActivityMainBinding mainBinding;
    private RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setContext(this);


        mainBinding.setMainViewModel(mainViewModel);
        mainViewModel.callApiListUser();
        mainBinding.setLifecycleOwner(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mainBinding.rcvListUser.setLayoutManager(linearLayoutManager);

        // Observe dữ liệu từ ViewModel
//        mainViewModel.getUserListLiveData().observe(this, userEntities -> {
//            if (userEntities != null) {
//                List<UserModel> userList = new ArrayList<>();
//                for (UserModel userModel : userEntities) {
//                    userList.add(userModel);
//                }
//
//                // Đưa danh sách người dùng vào RecyclerView
//                MyAdapter myAdapter = new MyAdapter(userList, id -> goToUserDetailActivity(id));
//                mainBinding.rcvListUser.setAdapter(myAdapter);
//            }
//        });
        mainViewModel.getUserListLiveData().observe(this, userModels -> {
            if (userModels != null) {
                updateRecyclerView(userModels);
            }
        });
        // Observe trạng thái của việc gọi API
        mainViewModel.getIsSuccess().observe(this, isSuccess -> {
            if (!isSuccess) {
                Toast.makeText(this, "Call api error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Call api success", Toast.LENGTH_SHORT).show();
            }
        });
        // Kiểm tra kết nối internet và gọi API
        if (NetworkUtils.isNetworkAvailable(this)) {
            mainViewModel.callApiListUser();
        } else {
            // Nếu không có kết nối, lấy dữ liệu từ Room Database
            mainViewModel.getUsersFromDatabase();
        }

        // Gọi API để lấy danh sách người dùng
        mainViewModel.callApiListUser();
    }

    private void updateRecyclerView(List<UserModel> userList) {
        MyAdapter myAdapter= new MyAdapter(userList, new MyAdapter.IClickItemListener() {
            @Override
            public void onClickDetailUser(int id) {
                ((MainActivity) view.getContext()).goToUserDetailActivity(id);
            }
        });
        mainBinding.rcvListUser.setAdapter(myAdapter);
        mainBinding.rcvListUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void goToUserDetailActivity(int id) {
        Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
        intent.putExtra("id_user", id);
        startActivity(intent);
    }
    @BindingAdapter({"list_data"})
    public static void loadListUser(RecyclerView view, List<UserModel> userList) {
        if (userList != null) {
            MyAdapter myAdapter = new MyAdapter(userList, id -> {
                ((MainActivity) view.getContext()).goToUserDetailActivity(id);
            });
            view.setAdapter(myAdapter);
            view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        }
    }


}
