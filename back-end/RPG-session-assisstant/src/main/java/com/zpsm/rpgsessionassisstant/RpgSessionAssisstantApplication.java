package com.zpsm.rpgsessionassisstant;

import com.zpsm.rpgsessionassisstant.model.Player;
import com.zpsm.rpgsessionassisstant.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RpgSessionAssisstantApplication implements ApplicationRunner {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(RpgSessionAssisstantApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Player player = new Player();
        player.setLogin("Testowy");
        player.setPassword(passwordEncoder.encode("password1"));
        playerRepository.save(player);
    }

}
