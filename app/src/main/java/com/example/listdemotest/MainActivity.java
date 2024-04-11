package com.example.listdemotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listdemotest.adapter.MyAdapter;
import com.example.listdemotest.databinding.ActivityMainBinding;
import com.example.listdemotest.model.UserModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding mainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        MainViewModel mainViewModel=new MainViewModel();
        mainBinding.setMainViewModel(mainViewModel);


        // Loop through the user list and create TextViews and ImageViews dynamically

        mainViewModel.callApiListUser();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mainBinding.rcvListUser.setLayoutManager(linearLayoutManager);

    }
    public void goToUserDetailActivity(int id) {

        Intent intent = new Intent(MainActivity.this , UserDetailActivity.class);
        intent.putExtra("id_user", id);
        startActivity(intent);



    }
    @BindingAdapter({"list_data","is_success"})
    public static void loadListUser(RecyclerView view, ObservableArrayList<UserModel> list, ObservableBoolean isSuccess){
        if(isSuccess.get()){
            Toast.makeText(view.getContext(), "Call api success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "Call api error", Toast.LENGTH_SHORT).show();}
        MyAdapter myAdapter= new MyAdapter(list, new MyAdapter.IClickItemListener() {
            @Override
            public void onClickDetailUser(int id) {
                ((MainActivity) view.getContext()).goToUserDetailActivity(id);
            }
        });
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        view.setAdapter(myAdapter);


    }
}