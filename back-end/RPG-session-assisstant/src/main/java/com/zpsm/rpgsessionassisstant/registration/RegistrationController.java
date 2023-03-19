package com.zpsm.rpgsessionassisstant.registration;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequest request) {
        log.info("New player to register");
        registrationService.register(request);
        return ResponseEntity.ok("New player registered");
    }

}
