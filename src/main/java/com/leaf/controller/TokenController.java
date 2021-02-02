package com.leaf.controller;

import com.leaf.domain.Token;
import com.leaf.domain.TokenHistory;
import com.leaf.service.TokenService;
import com.leaf.vo.LeafResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @RequestMapping(value="/produce/{count}/{amount}", method = {RequestMethod.GET})
    public ResponseEntity<LeafResponse> produce(@RequestHeader(value="X-USER-ID") String userId
                                    , @RequestHeader(value="X-ROOM-ID") Long roomId
                                    , @PathVariable int count
                                    , @PathVariable Long amount) {

        Token token = tokenService.produce(roomId, userId, count, amount);
        LeafResponse<Token> result = LeafResponse.<Token>builder().result(token).build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/consume/{token}", method = {RequestMethod.GET})
    public ResponseEntity<LeafResponse> consume(@RequestHeader(value="X-USER-ID") String userId
                                    , @RequestHeader(value="X-ROOM-ID") Long roomId
                                    , @PathVariable String token) {

        TokenHistory tokenHistory = tokenService.consume(roomId, userId, token);
        LeafResponse<TokenHistory> result = LeafResponse.<TokenHistory>builder().result(tokenHistory).build();

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping("/status/{token}")
    public ResponseEntity<LeafResponse> status(@RequestHeader(value="X-USER-ID") String userId
                                    , @RequestHeader(value="X-ROOM-ID", required = false) Long roomId
                                    , @PathVariable String token) {

        Token tokenInfo = tokenService.status(token, userId);
        LeafResponse<Token> result = LeafResponse.<Token>builder().result(tokenInfo).build();

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
