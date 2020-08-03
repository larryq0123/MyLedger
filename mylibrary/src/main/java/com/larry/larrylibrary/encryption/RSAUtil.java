package com.larry.larrylibrary.encryption;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public class RSAUtil {

    private static RSAUtil instance;
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String DEFAULT_ALIAS = "KEYSTORE_DEFAULT";

    private KeyStore mKeyStore;



    public static RSAUtil getInstance(){
        if(instance == null){
            synchronized (RSAUtil.class){
                if(instance == null){
                    instance = new RSAUtil();
                }
            }
        }

        return instance;
    }

    private RSAUtil(){
        initKeyStore();
    }

    private void initKeyStore(){
        try {
            mKeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            mKeyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasAlias(String alias) {
        try {
            return mKeyStore != null && mKeyStore.containsAlias(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void generateKeyPair(Context context) throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, NoSuchProviderException{

        generateKeyPair(context, DEFAULT_ALIAS);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void generateKeyPair(Context context, String alias) throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, NoSuchProviderException{

        if(hasAlias(alias))
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            generateRSAKeyAboveApi23(alias);
        } else {
            generateRSAKeyBelowApi23(context, alias);
        }
    }

    /**
     * 非对称加密算法密匙对的生成
     * @param alias key pair的名稱
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateRSAKeyAboveApi23(String alias) throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, NoSuchProviderException{

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA,
                KEYSTORE_PROVIDER);

        //RSA算法用于签名与校验，具体参考KeyGenParameterSpec类注释
        KeyGenParameterSpec spec = new KeyGenParameterSpec.
                Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build();

        keyPairGenerator.initialize(spec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void generateRSAKeyBelowApi23(Context context, String alias) throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, NoSuchProviderException{

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);

        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();

//        KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", KEYSTORE_PROVIDER);
        generator.initialize(spec);
        generator.generateKeyPair();
    }

    private KeyPair getKeyPair(String alias) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);
        PublicKey publicKey = entry.getCertificate().getPublicKey();
        PrivateKey privateKey = entry.getPrivateKey();
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 使用RSA加密
     * @param plainText
     * @return 成功回傳加密後文字, 失敗的話回傳null
     */
    @Nullable
    public String encrypt(@NonNull String plainText){
        return encrypt(DEFAULT_ALIAS, plainText);
    }

    /**
     * 使用RSA加密
     * @param alias KeyPair 名字
     * @param plainText
     * @return 成功回傳加密後文字, 失敗的話回傳null
     */
    @Nullable
    public String encrypt(String alias, @NonNull String plainText){
        try{
            PublicKey publicKey = mKeyStore.getCertificate(alias).getPublicKey();
            Cipher cipher = Cipher.getInstance(RSA_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        }catch (KeyStoreException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        } catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用RSA解密
     * @param encryptedText
     * @return 成功回傳解密後文字, 失敗的話回傳null
     */
    @Nullable
    public String decrypt(@NonNull String encryptedText) {
        return decrypt(DEFAULT_ALIAS, encryptedText);
    }

    /**
     * 使用RSA解密
     * @param alias KeyPair 名字
     * @param encryptedText
     * @return 成功回傳解密後文字, 失敗的話回傳null
     */
    @Nullable
    public String decrypt(String alias, @NonNull String encryptedText) {
        try{
            PrivateKey privateKey = (PrivateKey) mKeyStore.getKey(alias, null);
            Cipher cipher = Cipher.getInstance(RSA_MODE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        }catch (UnrecoverableKeyException e){
            e.printStackTrace();
        }catch (KeyStoreException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        } catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }

        return null;
    }
}
