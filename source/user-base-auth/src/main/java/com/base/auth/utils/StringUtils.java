package com.base.auth.utils;

import com.base.auth.repository.CartRepository;
import com.base.auth.repository.OrderRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.SecureRandom;

public class StringUtils {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }

    public static String generateUniqueCode(int length, JpaRepository<?, ?> repository) {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(length).toLowerCase();
        } while (repository instanceof CartRepository && ((CartRepository) repository).existsByCode(code) ||
                repository instanceof OrderRepository && ((OrderRepository) repository).existsByCode(code));
        return code;
    }

}
