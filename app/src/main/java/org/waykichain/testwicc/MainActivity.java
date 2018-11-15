package org.waykichain.testwicc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import core.CoinApi;
import core.Constants;
import core.JBtSeed;
import core.JBtWallet;
import core.JKeyPath;
import core.JNetParams;
import core.JSmcContractTxParams;

public class MainActivity extends AppCompatActivity {
    //程序启动时加载jni库
    static {
        System.loadLibrary("coinapi");
    }

    private CoinApi coinApi;
    private boolean isinit;
    private JNetParams jNetParams;
    private  JBtWallet jBtWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coinApi = new CoinApi();
        isinit = coinApi.init();
        setJNetParams();
    }

    //关于创建钱包和签名的，都属于耗时操作，请都放在异步线程处理
    public void setJNetParams() {
        jNetParams = new JNetParams();//
        jNetParams.setCoinType(Constants.COIN_TYPE_WICC);
        jNetParams.setSymbol(Constants.COIN_SYMBOL_WICC);
        jNetParams.setVersion(1);
        //测试网络
        //  jNetParams.setNettype(Constants.NETWORK_TYPE_TEST);
        jNetParams.setNettype(Constants.NETWORK_TYPE_MAIN);//正式网络
        JKeyPath jKeyPath = new JKeyPath();
        jKeyPath.setPath1(44);
        jKeyPath.setHd1(true);
        jKeyPath.setPath2(99999);
        jKeyPath.setHd2(true);
        jKeyPath.setPath3(0);
        jKeyPath.setHd3(true);
        jKeyPath.setPath4(0);
        jKeyPath.setHd4(false);
        jKeyPath.setPath5(0);
        jKeyPath.setHd5(false);
        jKeyPath.setSymbol(Constants.COIN_SYMBOL_WICC);

//测试网络
//            jNetParams.setHDprivate(0x7d5c5a26);
//            jNetParams.setHDpublic(0x7d573a2c);
//            jNetParams.setP2KH(135);
//            jNetParams.setP2SH(88);
//            jNetParams.setKeyprefixes(210);

        jNetParams.setHDprivate(0x4c233f4b);
        jNetParams.setHDpublic(0x4c1d3d5f);
        jNetParams.setP2KH(73);
        jNetParams.setP2SH(51);
        jNetParams.setKeyprefixes(153);//正式网络

        jNetParams.setN(32768);
        jNetParams.setR(8);
        jNetParams.setP(1);
        jNetParams.setKeyPath(jKeyPath);
    }

    //创建/导入钱包
    private void createWallet(){
        String password="a12345678";
        List<String > words =  coinApi.createAllCoinMnemonicCode() ;//生成助记词
        boolean validate = coinApi.checkMnemonicCode(words);//检查助记词是否有效
        StringBuilder sb = new StringBuilder();
        int size = words.size();
        int j = 0;
        for (String word : words) {
            sb.append(word);
            if (j != size - 1) {
                sb.append(" ");
            }
            j++;
        }
        final String wordsListString = sb.toString();
        //创建钱包对象 异步线程处理
         jBtWallet = coinApi.createWallet(wordsListString, password, jNetParams);
        //获取私钥
        String strPri = coinApi.getPriKeyFromBtSeed(jBtWallet.getBtSeed(),password, jNetParams);
        //获取钱包地址
        String walletAddress = jBtWallet.getAddress();
    }

    //激活
    private void activation(){
        int height=0;//传入当前区块实时高度
        String password="a12345678";
        JBtSeed jbs = jBtWallet.getBtSeed();
        JSmcContractTxParams jSmcContractTxParams2 = new JSmcContractTxParams();
        jSmcContractTxParams2.setPassword(password);//钱包密码
        jSmcContractTxParams2.setBtSeed(jbs);
        jSmcContractTxParams2.setValidHeight(height);
        jSmcContractTxParams2.setFees(new BigInteger("10000"));//激活收取0.0001WICC，可自由定义              jSmcContractTxParams2.setTxType(Constants.TX_WICC_REGISTERACCOUNT);
        jSmcContractTxParams2.setValue(new BigInteger("0"));//金额设置为0
        Map resultMap = coinApi.createSignTransaction(jSmcContractTxParams2, jNetParams);
        String signHex = resultMap.get("hex").toString();//获得签名后的hash提交到链上
    }

    //转账
    private void transfer(){
        String password="a12345678";
        JBtSeed jbs = jBtWallet.getBtSeed();
        JSmcContractTxParams jSmcContractTxParams2 = new JSmcContractTxParams();
        jSmcContractTxParams2.setPassword(password);
        jSmcContractTxParams2.setBtSeed(jbs);
        //fee输入小费，可自由定义
        String fee="0.01";
        int height=0;//传入当前区块实时高度
        BigDecimal fee_decimal= new BigDecimal(Double.parseDouble(fee)*100000000);
        jSmcContractTxParams2.setFees(fee_decimal.toBigInteger());//设置小费
        jSmcContractTxParams2.setValidHeight(height);//传入当前区块高度
        jSmcContractTxParams2.setSrcRegId("613730-1");//钱包注册ID
        jSmcContractTxParams2.setDestAddr("Wcc2urYYmcDJvPJdLSWzyPJLDGH7pVpANp");//收款人地址
        jSmcContractTxParams2.setTxType(Constants.TX_WICC_COMMON); //WICC转账
        BigDecimal value_decimal= new BigDecimal(Double.parseDouble("100") * 100000000);
        jSmcContractTxParams2.setValue(value_decimal.toBigInteger());//设置转账金额
        Map resultMap = coinApi.createSignTransaction(jSmcContractTxParams2, jNetParams);
        String signHex = resultMap.get("hex").toString();//获得交易签名hash
    }

    //调用智能合约 以锁仓为例
    private void lockPosition(){
        String password="a12345678";
        int height=0;//传入当前区块实时高度
        String fee="0.01";//fee输入小费，可自由定义
        JBtSeed jbs = jBtWallet.getBtSeed();
        JSmcContractTxParams jSmcContractTxParams2 = new JSmcContractTxParams();
        jSmcContractTxParams2.setPassword(password);
        jSmcContractTxParams2.setBtSeed(jbs);
        jSmcContractTxParams2.setValidHeight(height);//实时区块高度
        BigDecimal fee_decimal= new BigDecimal(Double.parseDouble(fee)*100000000);

        jSmcContractTxParams2.setFees(fee_decimal.toBigInteger());
        jSmcContractTxParams2.setSrcRegId("613730-1");
        jSmcContractTxParams2.setDestAddr("450687-1");//设置智能合约的id号，在区块浏览器应用栏下可以找到
        jSmcContractTxParams2.setTxType(Constants.TX_WICC_BET);//调用合约
        BigDecimal value_decimal= new BigDecimal(Double.parseDouble("1") * 100000000);
        jSmcContractTxParams2.setValue(value_decimal.toBigInteger());//设置锁仓金额
        String amount = Utils.toHexString(value_decimal.toBigInteger().toString());//金额转16进制
        String net_amount= "f20200e1f50500000000";//金额16进制需要转网络序
        // 这里表示锁仓1WICC, f202是合约头 ，金额转网络序后不足补零
        byte[] bin2 = Utils.hexString2binaryString(net_amount);//16进制转二进制
        jSmcContractTxParams2.setContract(bin2);//设置合约
        Map resultMap = coinApi.createSignTransaction(jSmcContractTxParams2, jNetParams);
        String signHex = resultMap.get("hex").toString();//获得合约签名
    }

}


