import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class WalletTransfer {

    private static final Logger log = LoggerFactory.getLogger(WalletTransfer.class);

    public static Web3j web3 = null;

    public static void main(String args[]) {

        try {
            web3 = Web3j.build(new HttpService("https://ropsten.infura.io/JOEnl84Gm76oX0RMUrJB"));
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("clientVersion : " + clientVersion);

            //String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
            Credentials credentials = WalletUtils.loadCredentials("ionc", "/Users/yves/Library/Ethereum/UTC--2018-08-10T00-57-14.520000000Z--5955a4af833de085e670426f9ddf5b17e19f3c63.json" );
            System.out.println(credentials.getAddress());
            System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));
            System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));

            log.info("Sending 1 Wei ("
                    + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
            TransactionReceipt transferReceipt = org.web3j.tx.Transfer.sendFunds(
                    web3, credentials,
                    "0x18f54aade5dde6ce3772b78b293d76c25d874f92",  // you can put any address here
                    BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                    .send();
            System.out.println(transferReceipt.getTransactionHash());
            log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transferReceipt.getTransactionHash());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
