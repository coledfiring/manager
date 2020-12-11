package com.whaty.products.service.suda.domain;

import com.whaty.common.encrypt.EncryptUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 苏大培训平台对接签名模型
 *
 * @author suoqiangqiang
 */
public class TrainDSA implements Serializable {

    private static final long serialVersionUID = 773899518439793010L;
    /**
     * 公钥
     */
    private String publicKeyStr;
    /**
     * 私钥
     */
    private String privateKeyStr;
    /**
     * 参数
     */
    private Map paramMap;
    /**
     * 密钥对
     */
    private KeyPair keyPair;

    public TrainDSA(String pubKey, String priKey, Map params) throws Exception {
        paramMap = new HashMap();
        publicKeyStr = pubKey;
        privateKeyStr = priKey;
        paramMap = params;
        keyPair = loadKeyPair();
    }

    /**
     * 加载密钥对
     *
     * @return
     * @throws Exception
     */
    public KeyPair loadKeyPair() throws Exception {
        PublicKey pubKey = KeyFactory.getInstance("DSA").generatePublic(
                new X509EncodedKeySpec(decodeBase64Str(publicKeyStr)));
        PrivateKey privateKey = KeyFactory.getInstance("DSA").generatePrivate(
                new PKCS8EncodedKeySpec(decodeBase64Str(privateKeyStr)));
        return new KeyPair(pubKey, privateKey);
    }

    /**
     * 解码字符串
     *
     * @param src
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] decodeBase64Str(String src) throws UnsupportedEncodingException {
        return Base64.decodeBase64(src.getBytes("utf-8"));
    }

    /**
     * 加密签名
     *
     * @return
     * @throws Exception
     */
    public String signWithBase64() throws Exception {
        return new String(Base64.encodeBase64(sign()), "utf-8");
    }

    /**
     * 获取签名
     *
     * @return
     * @throws Exception
     */
    public byte[] sign() throws Exception {
        return sign(EncryptUtils.getSortedParamValue(paramMap, "auth").getBytes("UTF-8"));
    }

    /**
     * 获取签名
     *
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public byte[] sign(byte content[]) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("DSA");
        sig.initSign(this.keyPair.getPrivate());
        sig.update(content);
        return sig.sign();
    }
}
