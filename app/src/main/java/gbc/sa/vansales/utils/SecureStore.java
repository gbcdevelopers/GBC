package gbc.sa.vansales.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
/**
 * Created by Rakshit on 16-Dec-16.
 */
public class SecureStore {

    private static String ALGORITHM = "AES";
    private static String MECHANISM = "AES/CBC/PKCS5Padding";

    public static String encryptData(byte[] key, byte[] iv, String value){
        SecretKeySpec keySpec = new SecretKeySpec(key,ALGORITHM);
        byte[] encryptedData = new byte[value.getBytes().length];
        String cipherText = null;
        try{
            Cipher cipher = Cipher.getInstance(MECHANISM);
            cipher.init(Cipher.ENCRYPT_MODE,keySpec,new IvParameterSpec(iv));
            encryptedData = cipher.doFinal(value.getBytes());
            cipherText = Base64.encode(encryptedData).toString();
        }
        catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch(BadPaddingException e){
            e.printStackTrace();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(NoSuchPaddingException e){
            e.printStackTrace();
        }catch(InvalidKeyException e){
            e.printStackTrace();
        }catch(InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }
        return cipherText;

    }

    public static String decryptData(byte[] key, byte[] iv, String encrypted){
        SecretKeySpec keySpec = new SecretKeySpec(key,ALGORITHM);
        byte[] original = new byte[encrypted.getBytes().length];
        try{
            Cipher cipher = Cipher.getInstance(MECHANISM);
            cipher.init(Cipher.DECRYPT_MODE,keySpec,new IvParameterSpec(iv));
            original = cipher.doFinal(Base64.decode(encrypted));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new String(original);
    }

    public static int generateRandom(int min, int max){

        Random r = new Random();
        return r.nextInt((max - min) + 5) + min;

    }

    public static byte[] generateKey(int bytes){
        byte[] keys = new byte[bytes];
        for(int i=0;i<bytes;i++){
            int min = generateRandom(2, 40);
            int max = generateRandom(80,128);

            Random r = new Random();
            if(r.nextInt((max-min)+generateRandom(10,20))<=0){
                if((r.nextInt(50)+generateRandom(10, 100))<=0){
                    keys[i]= (byte)(100);
                }
                else{
                    keys[i] = (byte)r.nextInt((max-min)+generateRandom(10,20));
                }
            }
            else{
                if((r.nextInt(50)+generateRandom(10, 100))<=0){
                    keys[i]= (byte)(100);
                }
                else{
                    keys[i] = (byte)r.nextInt((max-min)+generateRandom(10,20));
                }
            }

        }
        return keys;
    }

    public static byte[] validateKey(byte[]key){
        byte[] tempKey = key;
        for(int i=0;i<key.length;i++){
            if(tempKey[i]<=0||tempKey[i]>128){
                tempKey[i] = (byte)120;
            }
        }
        return tempKey;
    }
}
