package com.example.listdemotest.viewModel;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.listdemotest.api.ApiService;
import com.example.listdemotest.model.UserDetailModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserDetailViewModel extends ViewModel {

    private Disposable mDisposable;
    public ObservableField<UserDetailModel> userDetail = new ObservableField<>();
    private ObservableBoolean isSuccessDetail = new ObservableBoolean();


    public void callApiUser(int id) {
        ApiService.apiService.callApiUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mDisposable = disposable;
                })
                .subscribe(
                        // onNext
                        userDetailModel -> {
                            userDetail.set(userDetailModel);
                            isSuccessDetail.set(true);
                        },
                        // onError
                        throwable -> {
                            isSuccessDetail.set(false);
                            // Handle error
                        }
                );
    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public ObservableBoolean getIsSuccessDetail() {
        return isSuccessDetail;
    }

    public void setIsSuccessDetail(ObservableBoolean isSuccessDetail) {
        this.isSuccessDetail = isSuccessDetail;
    }

    public ObservableField<UserDetailModel> getUserDetail() {
        return userDetail;
    }
}
