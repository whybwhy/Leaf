package com.leaf.service;

import com.leaf.domain.Token;
import com.leaf.entity.TokenRepository;
import com.leaf.exception.GlobalException;
import com.leaf.exception.GlobalExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenGenerator {

    @Value("${service.token.length}")
    private int tokenSize;

    private final TokenRepository repository;

    private String generate() {
       return RandomStringUtils.randomAlphanumeric(tokenSize);
    }

    public String generate(int retry) {

        if (retry == 1)
            throw new GlobalException(GlobalExceptionMessage.INVALID_SERVER_TOKEN);

        String token = this.generate();

        Optional<Token> tokenOptional = repository.findById(token);
        if (tokenOptional.isPresent()) {
            token = generate(retry - 1);
        }

        return token;
    }
}
