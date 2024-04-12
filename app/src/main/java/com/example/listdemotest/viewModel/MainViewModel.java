package com.example.listdemotest.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.listdemotest.api.ApiService;
import com.example.listdemotest.database.UserDao;
import com.example.listdemotest.database.UserDatabase;
import com.example.listdemotest.entity.UserEntity;
import com.example.listdemotest.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private Context context;
    private Disposable mDisposable;
    private MutableLiveData<List<UserModel>> userListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    public MainViewModel() {
    }

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
                        userListLiveData.setValue(userModels); // Cập nhật LiveData
                        List<UserEntity> userEntities = new ArrayList<>();

                        for (UserModel userModel : userModels) {
                            UserEntity userEntity = new UserEntity();
                            userEntity.setId(userModel.getId());
                            userEntity.setAvatar_url(userModel.getAvatar_url());
                            userEntity.setHtml_url(userModel.getHtml_url());
                            userEntity.setLogin(userModel.getLogin());
                            userEntities.add(userEntity);
                        }
                        saveUsersToDatabase(userEntities);

                        isSuccess.setValue(true); // Đánh dấu thành công
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isSuccess.setValue(false); // Đánh dấu thất bại
                    }

                    @Override
                    public void onComplete() {
                        // Không cần thiết
                    }
                });
    }

    private void saveUsersToDatabase(List<UserEntity> users) {
        UserDatabase userDatabase = UserDatabase.getInstance(context);
        UserDao userDao = userDatabase.userDao();

        // Sử dụng Executor để thực hiện lưu trữ dữ liệu vào database trên một luồng riêng
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            userDao.insertUser(users);
        });
    }
    public void getUsersFromDatabase() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            UserDatabase userDatabase = UserDatabase.getInstance(context);
            List<UserEntity> userEntities = userDatabase.userDao().getUserEntity();
            List<UserModel> userModels = new ArrayList<>();

            for (UserEntity userEntity : userEntities) {
                UserModel userModel = new UserModel();
                userModel.setId(userEntity.getId());
                userModel.setLogin(userEntity.getLogin());
                userModel.setAvatar_url(userEntity.getAvatar_url());
                userModel.setHtml_url(userEntity.getHtml_url());
                // Điền các trường dữ liệu khác của người dùng
                userModels.add(userModel);
            }

            userListLiveData.postValue(userModels);
        });
    }

    public MutableLiveData<List<UserModel>> getUserListLiveData() {
        return userListLiveData;
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
