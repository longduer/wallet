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
            web3 = Web3j.build(new HttpService("http://127.0.0.1:7545"));
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("clientVersion : " + clientVersion);

            //String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
            Credentials credentials = WalletUtils.loadCredentials("aaaaaaaa", "/Users/yves/ethereum/privateChain/keystore/UTC--2018-05-22T11-48-03.459005936Z--0df14334e094acc0197d52a415d799c2b8a3b04b" );
            System.out.println(credentials.getAddress());
            System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));
            System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));

            log.info("Sending 1 Wei ("
                    + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
            TransactionReceipt transferReceipt = org.web3j.tx.Transfer.sendFunds(
                    web3, credentials,
                    "0x18f54aade5dde6ce3772b78b293d76c25d874f92",  // you can put any address here
                    BigDecimal.ONE, Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
                    .send();
            System.out.println(transferReceipt.getTransactionHash());
            log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transferReceipt.getTransactionHash());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
