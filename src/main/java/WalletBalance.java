import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.net.ServerSocket;

public class WalletBalance {
    private static final Logger log = LoggerFactory.getLogger(WalletBalance.class);

    public static Web3j web3 = null;

    public static void main(String args[]) {
        try {
            web3 = Web3j.build(new HttpService("https://ropsten.infura.io"));
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("clientVersion : " + clientVersion);

            EthGetBalance ethGetBalance = web3
                    .ethGetBalance("0x5955a4af833de085e670426f9ddf5b17e19f3c63", DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            BigInteger wei = ethGetBalance.getBalance();
            System.out.println("balance is :" + wei);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
