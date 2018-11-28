package com.wayki.wallet.callback;

public interface Wallet_Callback<T> {
    void Success(T str);
    void Failure(T err);
}
