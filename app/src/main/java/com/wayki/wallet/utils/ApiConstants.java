package com.wayki.wallet.utils;

import com.wayki.wallet.BuildConfig;

public class ApiConstants {
    public  static final String baseurl= BuildConfig.API_HOST;
    public static final String upgrade= baseurl+"api/app/upgrade";
    public static final String bindwallet=baseurl +"api/customer/bind/wallet";
    public static final String bindinfo=baseurl +"api/customer/ref/wallet/info";
    public static final String getaccountinfo=baseurl +"api/customer/account/getinfo";
    public  static final String getrate=baseurl+"api/sys/exchange/rate";
    public  static final String transaction_wicc=baseurl+"api/exchange/trans/wicc";
    public static final String  blocknumber=baseurl+"api/chain/height";
    public static final String  isactive=baseurl+"api/address/isActive";
    public static final String  wicc2token=baseurl+"api/exchange/trans/wicc/token";
    public static final String  token2wicc2=baseurl+"api/exchange/trans/token/wicc";
    public static final String  exchanglist=baseurl+"api/exchange/exchangeOrders";
    public static final String  tradelist=baseurl+"api/customer/account/log/logs";
    public static final String  getcontract=baseurl+"api/exchange/contract";
    public static final String  getwicc=baseurl+"api/sys/link/100";

    public static final String  tradedetail=baseurl+"api/customer/account/log/detail/";

    public static final String  bottom_item=baseurl+"api/sys/tab/menu/list";

    public static final String  reward=baseurl+"api/walletaddress/sys/reward/list";

    public  static final String wtestrate=baseurl+"api/sys/exchange/WTEST/rate";


    public  static final String web_login= baseurl+"login";

}
