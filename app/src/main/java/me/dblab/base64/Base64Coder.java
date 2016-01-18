package me.dblab.base64;

import android.util.Base64;

public class Base64Coder {
    public static byte[] fromBase64(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    public static String toBase64(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT));
    }
}
