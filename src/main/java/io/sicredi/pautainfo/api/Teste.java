package io.sicredi.pautainfo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Teste {

    @GetMapping("/")
    public String get() {
        return "ae";
    }
}
