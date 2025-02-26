package com.base.auth.utils;

import com.base.auth.repository.CartRepository;
import org.apache.commons.lang.RandomStringUtils;

import java.security.SecureRandom;

public class StringUtils {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }

    public static String generateUniqueCode(int length, CartRepository cartRepository) {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(length).toLowerCase();
        } while (cartRepository.existsByCode(code));
        return code;
    }

}
