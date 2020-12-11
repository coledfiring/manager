package com.whaty.framework.im;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.Deflater;

/**
 * 生成userSig
 *
 * @author weipengsen
 */
public class GenerateUserSig {

    private static final long EXPIRE_TIME = 604800;

    private static String hmacSha256(long appId, String key, String identifier, long currTime, long expire)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String contentToBeSigned = "TLS.identifier:" + identifier + "\n"
                + "TLS.sdkappid:" + appId + "\n"
                + "TLS.time:" + currTime + "\n"
                + "TLS.expire:" + expire + "\n";
        byte[] byteKey = key.getBytes("UTF-8");
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
        hmac.init(keySpec);
        byte[] byteSig = hmac.doFinal(contentToBeSigned.getBytes("UTF-8"));
        return (new BASE64Encoder().encode(byteSig)).replaceAll("\\s*", "");
    }

    public static String genSig(long appId, String key, String identifier)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        long currTime = System.currentTimeMillis() / 1000;

        JSONObject sigDoc = new JSONObject();
        sigDoc.put("TLS.ver", "2.0");
        sigDoc.put("TLS.identifier", identifier);
        sigDoc.put("TLS.sdkappid", appId);
        sigDoc.put("TLS.expire", EXPIRE_TIME);
        sigDoc.put("TLS.time", currTime);
        sigDoc.put("TLS.sig", hmacSha256(appId, key, identifier, currTime, EXPIRE_TIME));
        Deflater compressor = new Deflater();
        compressor.setInput(sigDoc.toString().getBytes(Charset.forName("UTF-8")));
        compressor.finish();
        byte[] compressedBytes = new byte[2048];
        int compressedBytesLength = compressor.deflate(compressedBytes);
        compressor.end();
        return (new String(Base64.getEncoder().encode(Arrays.copyOfRange(compressedBytes,
                0, compressedBytesLength)))).replaceAll("\\s*", "");
    }

}
