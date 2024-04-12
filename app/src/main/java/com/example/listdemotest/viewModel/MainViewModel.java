package com.example.listdemotest.viewModel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.listdemotest.api.ApiService;
import com.example.listdemotest.model.UserDetailModel;
import com.example.listdemotest.model.UserModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private Disposable mDisposable;
    private ObservableArrayList<UserModel> list= new ObservableArrayList<>();
    private ObservableBoolean isSuccess= new ObservableBoolean();


    public void callApiListUser(){
        ApiService.apiService.callApiListUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable=d;
                    }

                    @Override
                    public void onNext(@NonNull List<UserModel> userModels) {
                        list.addAll( userModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isSuccess.set(false);

                    }

                    @Override
                    public void onComplete() {
                        isSuccess.set(true);
                    }
                });

    }


    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public ObservableArrayList<UserModel> getList() {
        return list;
    }

    public void setList(ObservableArrayList<UserModel> list) {
        this.list = list;
    }

    public ObservableBoolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(ObservableBoolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
