package com.github.backend.config.security.util;

import com.github.backend.service.exception.CommonException;
import com.github.backend.web.entity.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AESUtil {
    private byte[] key;
    private SecretKeySpec secretKeySpec;

    static String IV = "";

    public AESUtil(@Value("${spring.aes.secret}") String rawKey) {
        try {
          key = rawKey.getBytes(StandardCharsets.UTF_8);
          IV = rawKey.substring(0, 16);
          secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
          throw new CommonException(e.getMessage(), ErrorCode.INTERNAL_FAIL_RESPONSE);
        }
    }

    public String encrypt(String str){
        try{
          //알고리즘/블럭 암호화 방식/Padding방식(메세지 길이가 짧은 경우 어떻게 처리 할 것인가?)
          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
          cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
          //cipher를 통한 암호화 결과 타입은 byte array라서 이를 쉽게 다루기위해 base64 string 으로 encoding 사용.
          return encodeBase64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception e) {
          throw new CommonException(e.getMessage(), ErrorCode.INTERNAL_FAIL_RESPONSE);
        }
    }

    public String decrypt(String str) {
        try {
          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
          cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes("UTF-8")));

          return new String(cipher.doFinal(decodeBase64(str)), "UTF-8");
        } catch (Exception e) {
          throw new CommonException(e.getMessage(), ErrorCode.INTERNAL_FAIL_RESPONSE);
        }
    }

    private String encodeBase64(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    private byte[] decodeBase64(String encodedString) {
      return Base64.getDecoder().decode(encodedString);
    }
}
