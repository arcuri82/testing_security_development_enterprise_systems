package org.tsdes.jee.jsf.jacoco.backend;

import java.math.BigInteger;
import java.security.SecureRandom;

public class StringUtil {

    public static String getRandomString(int n){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5

        return new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
    }
}
