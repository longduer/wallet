import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 基于ERC20的代币
 */
public class TokenClient {

    private static Web3j web3j;

    private static Admin admin;

    private static String fromAddress = "0x5955A4af833De085E670426f9ddf5B17E19F3c63";

    private static String toAddress = "0x79E3FaA828793b8d96E947f59DFA4b767e12B5Ed";

    private static String contractAddress = "0x92e831bbbb22424e0f22eebb8beb126366fa07ce";

    private static String emptyAddress = "0x0000000000000000000000000000000000000000";

    public static void main(String[] args) {
        web3j = Web3j.build(new HttpService("https://ropsten.infura.io/JOEnl84Gm76oX0RMUrJB"));
        admin = Admin.build(new HttpService("https://ropsten.infura.io/JOEnl84Gm76oX0RMUrJB"));
        //查询账户余额
        BigInteger balance = getTokenBalance(web3j, fromAddress, contractAddress);
        System.out.println("查询账户余额: "+Convert.fromWei(new BigDecimal(balance), Convert.Unit.ETHER) + " IONC");

        //转账交易
//        sendTokenTransaction(
//                fromAddress,
//                "ionc",
//                toAddress,
//                contractAddress,
//                BigInteger.valueOf(1*Math.round(Math.pow(10,18)))
//        );

        //Token名称
        System.out.println("Token名称: "+getTokenName(web3j, contractAddress));
        //代币精度
        System.out.println("代币精度：" + getTokenDecimals(web3j, contractAddress));
        //Token符号
        System.out.println("Token符号: " + getTokenSymbol(web3j, contractAddress));
        //总供应量
        System.out.println("总供应量: "+getTokenTotalSupply(web3j, contractAddress));
        //
        System.out.println("转账交易hash:" +ioncTokenTransfer(
                "ionc",
                "/Users/yves/Library/Ethereum/UTC--2018-08-10T00-57-14.520000000Z--5955a4af833de085e670426f9ddf5b17e19f3c63.json",
                toAddress,
                BigInteger.valueOf(1*Math.round(Math.pow(10,18)))
                )
        );
//        //计算合约地址
//        System.out.println(calculateContractAddress("0x6c0f49aF552F2326DD851b68832730CB7b6C0DaF".toLowerCase(), 294));
    }

    /**
     * 查询代币余额
     */
    public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    /**
     * 查询代币名称
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenName(Web3j web3j, String contractAddress) {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询代币符号
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenSymbol(Web3j web3j, String contractAddress) {
        String methodName = "symbol";
        String symbol = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return symbol;
    }

    /**
     * 查询代币精度
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static int getTokenDecimals(Web3j web3j, String contractAddress) {
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return decimal;
    }

    /**
     * 查询代币发行总量
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
        String methodName = "totalSupply";
        String fromAddr = emptyAddress;
        BigInteger totalSupply = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return totalSupply;
    }

    public static String ioncTokenTransfer(String password,String keyStorePath,String toAddress,BigInteger amount){

        //授权凭证
        Credentials finalCredentials = null;

        try {
            //通过本地password与keyStore文件进行授权
            finalCredentials = WalletUtils.loadCredentials(
                    password,
                    keyStorePath
            );
            //调用IONC transfer方法
            Function function = new Function(
                    "transfer",
                    Arrays.<Type>asList(new Address(toAddress),
                            new Uint256(amount)),
                    Collections.<TypeReference<?>>emptyList());

            String encodedFunction = FunctionEncoder.encode(function);
            //交易gasPrice，目前固定，后期调整成动态获取
           // BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(2), Convert.Unit.GWEI).toBigInteger();

            // 创建 tx 管理器，并通过 txManager发起合约转账
            RawTransactionManager txManager = new RawTransactionManager(web3j, finalCredentials);
            EthSendTransaction transactionResponse = txManager.sendTransaction(
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                    contractAddress, encodedFunction, BigInteger.ZERO);

            // 获取TxHash
            String transactionHash = transactionResponse.getTransactionHash();
            return transactionHash;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 代币转账
     */
    public static String sendTokenTransaction(String fromAddress, String password, String toAddress, String contractAddress, BigInteger amount) {
        String txHash = null;

        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(
                    fromAddress, password, BigInteger.valueOf(10)).send();
            if (personalUnlockAccount.accountUnlocked()) {
                String methodName = "transfer";
                List<Type> inputParameters = new ArrayList<>();
                List<TypeReference<?>> outputParameters = new ArrayList<>();

                Address tAddress = new Address(toAddress);

                Uint256 value = new Uint256(amount);
                inputParameters.add(tAddress);
                inputParameters.add(value);

                TypeReference<Bool> typeReference = new TypeReference<Bool>() {
                };
                outputParameters.add(typeReference);

                Function function = new Function(methodName, inputParameters, outputParameters);

                String data = FunctionEncoder.encode(function);

                EthGetTransactionCount ethGetTransactionCount = web3j
                        .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();

                Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice,
                        BigInteger.valueOf(60000), contractAddress, data);

                EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
                txHash = ethSendTransaction.getTransactionHash();
                System.out.println("txHash : " + txHash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return txHash;
    }

    /**
     * 计算合约地址
     *
     * @param address
     * @param nonce
     * @return
     */
    private static String calculateContractAddress(String address, long nonce) {
        //样例 https://ropsten.etherscan.io/tx/0x728a95b02beec3de9fb09ede00ca8ca6939bad2ad26c702a8392074dc04844c7
        byte[] addressAsBytes = Numeric.hexStringToByteArray(address);

        byte[] calculatedAddressAsBytes =
                Hash.sha3(RlpEncoder.encode(
                        new RlpList(
                                RlpString.create(addressAsBytes),
                                RlpString.create((nonce)))));

        calculatedAddressAsBytes = Arrays.copyOfRange(calculatedAddressAsBytes,
                12, calculatedAddressAsBytes.length);
        String calculatedAddressAsHex = Numeric.toHexString(calculatedAddressAsBytes);
        return calculatedAddressAsHex;
    }



}