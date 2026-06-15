package pk.km.pasir_konieczny_mikolaj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.HashMap;
import java.util.Map;




@RestController
public class InfoController {

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("appName", "Aplikacja Budżetowa");
        info.put("version", "1.0");
        info.put("message", "Witaj w aplikacji budżetowej stworzonej ze Spring Boot!");

        return ResponseEntity.ok(info);
    }
}