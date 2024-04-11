package com.example.listdemotest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.listdemotest.R;
import com.example.listdemotest.databinding.UserDetailBinding;
import com.example.listdemotest.model.UserDetailModel;

public class UserDetailAdapter {

    private Context context;
    private UserDetailModel user;

    public UserDetailAdapter(Context context, UserDetailModel user) {
        this.context = context;
        this.user = user;
    }

    public  View getView() {
        LayoutInflater inflater = LayoutInflater.from(context);
         UserDetailBinding userDetailBinding = DataBindingUtil.inflate(inflater, R.layout.user_detail,null, false);
        userDetailBinding.setUserDetailViewModel(userDetailBinding.getUserDetailViewModel());

        Glide.with(context)
                .load(user.getAvatar_url())
                .circleCrop()
                .into(userDetailBinding.imgAvatar1);

        return userDetailBinding.getRoot();
    }
}
