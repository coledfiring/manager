package com.whaty.products.service.businessunit.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.commons.codec.binary.Hex;

/**
 * 北交大第三方支付:易智付证书操作类(第三方提供代码)
 *
 * @author shanshuai
 */
public class MerchantX509Cert {
    /**
     * Java密钥库(Java Key Store，JKS)KEY_STORE
     */
    public static final String KEY_STORE = "PKCS12";

    public static final String X509 = "X.509";

    /**
     * 签名
     * @param signSrc
     * @param keyStorePath
     * @param alias
     * @param keyStorePassword
     * @param aliasPassword
     * @return String
     * @throws Exception
     */
    public static String sign(String signSrc, String keyStorePath, String alias,
                              String keyStorePassword, String aliasPassword) throws Exception {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate)getCertificate(keyStorePath, keyStorePassword, alias);
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, keyStorePassword, alias, aliasPassword);
        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(signSrc.getBytes("UTF-8"));
        return new Hex().encodeHexString(signature.sign());
    }

    /**
     * 验证签名
     *
     * @param signSrc
     * @param sign
     * @param certificatePath
     * @return boolean
     * @throws Exception
     */
    public static boolean verifySign(String signSrc, String sign, String certificatePath) throws Exception {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate)getCertificate(certificatePath);
        // 获得公钥
        PublicKey publicKey = x509Certificate.getPublicKey();
        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(signSrc.getBytes("UTF-8"));
        byte[] signByte = new Hex().decode(sign.getBytes());

        boolean verifyStatus = false;
        verifyStatus = signature.verify(signByte);
        return verifyStatus;

    }

    /**
     * 由 KeyStore获得私钥
     * @param keyStorePath
     * @param keyStorePassword
     * @param alias
     * @param aliasPassword
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String keyStorePath, String keyStorePassword,
                                            String alias, String aliasPassword) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
        PrivateKey key = (PrivateKey)ks.getKey(alias, aliasPassword.toCharArray());
        return key;
    }

    /**
     * 由 Certificate获得公钥
     *
     * @param certificatePath
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String certificatePath) throws Exception {
        Certificate certificate = getCertificate(certificatePath);
        PublicKey key = certificate.getPublicKey();
        return key;
    }

    /**
     * 获得Certificate
     *
     * @param certificatePath
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
        FileInputStream in = new FileInputStream(certificatePath);
        Certificate certificate = certificateFactory.generateCertificate(in);
        in.close();
        return certificate;
    }

    /**
     * 获得Certificate
     *
     * @param keyStorePath
     * @param keyStorePassword
     * @param alias
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String keyStorePath, String keyStorePassword, String alias) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
        Certificate certificate = ks.getCertificate(alias);
        return certificate;
    }

    /**
     * 获得KeyStore
     * @param keyStorePath
     * @param password
     * @return
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance(KEY_STORE);
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

}
