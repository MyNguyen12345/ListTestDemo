package com.example.listdemotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.listdemotest.MainActivity;
import com.example.listdemotest.R;
import com.example.listdemotest.UserDetailActivity;
import com.example.listdemotest.databinding.ItemUserBinding;
import com.example.listdemotest.model.UserModel;

import java.util.List;

public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private  List<UserModel> listUser;
//    private Context context;
    private IClickItemListener mIClickItemListener;


    public void setData(List<UserModel> list){
        this.listUser=list;
        notifyDataSetChanged();
    }

    public interface IClickItemListener{
        void onClickDetailUser(int id);
    }


    public MyAdapter(List<UserModel> listUser, IClickItemListener listener)
    {
        this.listUser = listUser;
        this.mIClickItemListener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding itemUserBinding= DataBindingUtil.inflate(LayoutInflater.from
                (parent.getContext()), R.layout.item_user,parent,false);

        return new MyViewHolder(itemUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel userModel=listUser.get(position);
        holder.itemUserBinding.setUserViewModel( userModel);
        Glide.with(holder.itemView.getContext())
                .load(userModel.getAvatar_url())
                .circleCrop()
                .into(holder.itemUserBinding.imgAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItemListener.onClickDetailUser(userModel.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listUser!=null){
            return  listUser.size();
        }
        return 0;
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        private final ItemUserBinding itemUserBinding;

        public MyViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
            this.itemUserBinding=itemUserBinding;
        }
        public void bind(UserModel user) {
            itemUserBinding.setUserViewModel(user);
            itemUserBinding.executePendingBindings();
        }
    }

}
