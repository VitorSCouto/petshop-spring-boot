/*package dev.vitor.petshop.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        // Obtém a autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Obtém o nome do usuário a partir da autenticação
        String username = authentication.getName();
        
        // Retorna a mensagem com o nome do usuário
        return ResponseEntity.ok("Bem vindo(a), " + username + " ao PetShop :D");
    }
}*/