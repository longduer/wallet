import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;

import java.io.File;
import java.util.Random;

public class WalletCreate {

    public static Web3j web3 = null;

    public static void main(String args[]){

        try {

            //cherry type collect echo derive shy balcony dog concert picture kid february :
            //rich dad able super sail slow depend early case video double decade :glory
//            //step 1: 创建钱包并输出keystore文件 keyStore mac 默认路径：/Users/user_name/Library/Ethereum
//            String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
//            System.out.println(keyStoreDir);
//            Bip39Wallet wallet = WalletUtils.generateBip39Wallet("yves", new File(keyStoreDir));
//            System.out.println(wallet.getFilename());
//            System.out.println(wallet.getMnemonic());


            //step 2: 通过password和助记词拿到钱包私钥Bip39Wallet{filename='UTC--2018-05-22T02-11-24.10000000Z--6cf4b1ce62ddf6e4859baaf875d90f8469410e56.json', mnemonic='rich dad able super sail slow depend early case video double decade'}
            Credentials credentials = WalletUtils.loadBip39Credentials("yves",
                     "welcome resemble clump empty steak attitude quarter turn cherry search behind priority");
            System.out.println(credentials.getAddress());
            System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));
            System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));

//            //step 3：通过password和keystore拿到钱包私钥信息
//            credentials = WalletUtils.loadCredentials("", keyStoreDir + "/UTC--2018-05-22T02-46-57.932000000Z--ae45f5aec6e6e7c0780a2a09dc830a9c3cb5b16b.json" );
//            System.out.println(credentials.getAddress());
//            System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));
//            System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
