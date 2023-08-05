package org.fsp.springredditclone.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.fsp.springredditclone.dto.AuthenticationResponse;
import org.fsp.springredditclone.dto.LoginRequest;
import org.fsp.springredditclone.dto.RefreshTokenRequest;
import org.fsp.springredditclone.dto.RegisterRequest;
import org.fsp.springredditclone.exceptions.SpringRedditException;
import org.fsp.springredditclone.service.AuthService;
import org.fsp.springredditclone.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws SpringRedditException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws SpringRedditException {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) throws SpringRedditException {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) { return authService.refreshToken(refreshTokenRequest); }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
