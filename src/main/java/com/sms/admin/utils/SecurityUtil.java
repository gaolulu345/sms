package com.sms.admin.utils;

import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityUtil {

    /**
     * 消息摘要
     * @author
     */
    public static class MessageDigestUtil {

        public static byte[] digest(String content, boolean isMd5) throws Exception {
            MessageDigest messageDigest = null;
            String algorithm = isMd5 ? "MD5" : "SHA";
            messageDigest = MessageDigest.getInstance(algorithm);
            return messageDigest.digest(content.getBytes());
        }

        public static byte[] digest1(String content, boolean isMd5) throws Exception {
            MessageDigest messageDigest = null;
            String algorithm = isMd5 ? "MD5" : "SHA";
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes());
            return messageDigest.digest();
        }
    }

    /**
     * 对称加密算法
     *
     * @author zhouzhian
     */
    public static class AesUtil {
        private static final String ALGORITHM = "AES";
        private static final String DEFAULT_CHARSET = "UTF-8";

        /**
         * 生成秘钥
         *
         * @return
         * @throws NoSuchAlgorithmException
         */
        public static String generaterKey() throws NoSuchAlgorithmException {
            KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
            keygen.init(128, new SecureRandom()); // 16 字节 == 128 bit
            //            keygen.init(128, new SecureRandom(seedStr.getBytes())); // 随机因子一样，生成出来的秘钥会一样
            SecretKey secretKey = keygen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }

        /**
         */
        public static SecretKeySpec getSecretKeySpec(String secretKeyStr) {
            byte[] secretKey = Base64.getDecoder().decode(secretKeyStr);
            System.out.println(secretKey.length);
            return new SecretKeySpec(secretKey, ALGORITHM);
        }

        /**
         * 加密
         */
        public static String encrypt(String content, String secretKey) throws Exception {
            Key key = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器  
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
            return Base64.getEncoder().encodeToString(result);
        }

        /**
         * 解密
         */
        public static String decrypt(String content, String secretKey) throws Exception {
            Key key = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result);
        }

        /*
         * 加解密
         * @param data 待处理数据
         * @param password  密钥
         * @param mode 加解密mode
         * @return
         */
        public static String doAes(String data, String key, int mode) {
            try {
                if (StringUtils.isEmpty(data) || StringUtils.isEmpty(key)) {
                    return null;
                }
                //判断是加密还是解密
                boolean encrypt = mode == Cipher.ENCRYPT_MODE;
                byte[] content;
                //true 加密内容 false 解密内容
                if (encrypt) {
                    content = data.getBytes(DEFAULT_CHARSET);
                } else {
                    BASE64Decoder decoder = new BASE64Decoder();
                    content = decoder.decodeBuffer(data);
//                    content = parseHexStr2Byte(data);
                }
                //1.构造密钥生成器，指定为AES算法,不区分大小写
                KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
                //2.根据ecnodeRules规则初始化密钥生成器
                //生成一个128位的随机源,根据传入的字节数组
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes(DEFAULT_CHARSET)); // UTF-8
                kgen.init(128, random);
                //3.产生原始对称密钥
                SecretKey secretKey = kgen.generateKey();
                //4.获得原始对称密钥的字节数组
                byte[] enCodeFormat = secretKey.getEncoded();
                //5.根据字节数组生成AES密钥
                SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(DEFAULT_CHARSET),ALGORITHM);
                //6.根据指定算法AES自成密码器
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
                //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
                cipher.init(mode, keySpec);// 初始化
                byte[] result = cipher.doFinal(content);
                if (encrypt) {
                    // 二进制 转换 base64
                    BASE64Encoder encoder = new BASE64Encoder();
                    return encoder.encode(result);
                    //将二进制转换成16进制
//                    return parseByte2HexStr(result);
                } else {
                    return new String(result, DEFAULT_CHARSET);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 将16进制转换为二进制
         *
         * @param hexStr
         * @return
         */
        public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1) {
                return null;
            }
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }

        /**
         * 将二进制转换成16进制
         *
         * @param buf
         * @return
         */
        public static String parseByte2HexStr(byte buf[]) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < buf.length; i++) {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }


    }

    /**
     * 非对称加密算法
     *
     * @author zhouzhian
     */
    public static class RsaUtil {

        public static class RsaKeyPair {
            private String publicKey = "";
            private String privateKey = "";

            public RsaKeyPair(String publicKey, String privateKey) {
                super();
                this.publicKey = publicKey;
                this.privateKey = privateKey;
            }

            public String getPublicKey() {
                return publicKey;
            }

            public String getPrivateKey() {
                return privateKey;
            }
        }

        private static final String ALGORITHM = "RSA";
        private static final String ALGORITHMS_SHA1WithRSA = "SHA1WithRSA";
        private static final String ALGORITHMS_SHA256WithRSA = "SHA256WithRSA";
        private static final String DEFAULT_CHARSET = "UTF-8";

        private static String getAlgorithms(boolean isRsa2) {
            return isRsa2 ? ALGORITHMS_SHA256WithRSA : ALGORITHMS_SHA1WithRSA;
        }

        /**
         * 生成秘钥对
         *
         * @return
         * @throws NoSuchAlgorithmException
         */
        public static RsaKeyPair generaterKeyPair() throws NoSuchAlgorithmException {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
            SecureRandom random = new SecureRandom();
            //            SecureRandom random = new SecureRandom(seedStr.getBytes()); // 随机因子一样，生成出来的秘钥会一样
            // 512位已被破解，用1024位,最好用2048位
            keygen.initialize(2048, random);
            // 生成密钥对
            KeyPair keyPair = keygen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            return new RsaKeyPair(publicKeyStr, privateKeyStr);
        }


        /**
         * 获取公钥
         *
         * @param publicKey
         * @return
         * @throws Exception
         */
        public static RSAPublicKey getPublicKey(String publicKey) throws Exception {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPublicKey) keyFactory.generatePublic(spec);
        }

        /**
         * 获取私钥
         *
         * @param privateKey
         * @return
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         * @throws Exception
         */
        public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {
//            byte[] keyBytes = Base64.getDecoder().decode(privateKey);  
            byte[] keyBytes = decodeBase64(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(spec);
        }

        public static String encodeBase64(byte[] input) throws Exception {
            Class clazz = Class
                    .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method mainMethod = clazz.getMethod("encode", byte[].class);
            mainMethod.setAccessible(true);
            Object retObj = mainMethod.invoke(null, new Object[]{input});
            return (String) retObj;
        }

        /***
         * decode by Base64
         */
        public static byte[] decodeBase64(String input) throws Exception {
            Class clazz = Class
                    .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method mainMethod = clazz.getMethod("decode", String.class);
            mainMethod.setAccessible(true);
            Object retObj = mainMethod.invoke(null, input);
            return (byte[]) retObj;
        }

        /**
         * 要私钥签名
         *
         * @throws InvalidKeySpecException
         * @throws Exception
         */
        public static String sign(String content, String privateKey, boolean isRsa2) throws Exception {
            PrivateKey priKey = getPrivateKey(privateKey);
//            PrivateKey priKey = getPrivateKeyFromPKCS8("SHA256WithRSA", privateKey.getBytes());
            Signature signature = Signature.getInstance(getAlgorithms(isRsa2));
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return encodeBase64(signed);
//            return Base64.getEncoder().encodeToString(signed);
        }


        public static String signUrlEncode(String content, String privateKey, boolean isRsa2) throws Exception {
            PrivateKey priKey = getPrivateKey(privateKey);
//            PrivateKey priKey = getPrivateKeyFromPKCS8("SHA256WithRSA", privateKey.getBytes());
            Signature signature = Signature.getInstance(getAlgorithms(isRsa2));
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return URLEncoder.encode(encodeBase64(signed), DEFAULT_CHARSET);
//            return URLEncoder.encode(Base64.getEncoder().encodeToString(signed),DEFAULT_CHARSET);
        }

        public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, byte[] privateKeyBytes) throws Exception {
            if (privateKeyBytes == null || StringUtils.isEmpty(algorithm)) {
                return null;
            }

            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

            byte[] encodedKey = privateKeyBytes;

            encodedKey = Base64.getDecoder().decode(encodedKey); //Base64.decodeBase64(encodedKey);

            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        }

        /**
         * 要公钥签名
         */
        public static boolean verify(String content, String sign, String publicKey, boolean isRsa2) throws Exception {
            PublicKey pubKey = getPublicKey(publicKey);
            Signature signature = Signature.getInstance(getAlgorithms(isRsa2));
            signature.initVerify(pubKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            return signature.verify(Base64.getDecoder().decode(sign));
        }

        /**
         * 加密
         *
         * @param content
         * @param pubOrPrikey
         * @return
         */
        public static String encrypt(String content, Key pubOrPrikey) throws Exception {
            Cipher cipher = null;
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubOrPrikey);
            byte[] result = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
            return Base64.getEncoder().encodeToString(result);
        }

        /**
         * 解密
         *
         * @param content
         * @param pubOrPrikey
         * @return
         */
        public static String decrypt(String content, Key pubOrPrikey) throws Exception {
            Cipher cipher = null;
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubOrPrikey);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result);
        }
    }

}
