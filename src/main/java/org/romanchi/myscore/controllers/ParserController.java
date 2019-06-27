package org.romanchi.myscore.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/parser")
public class ParserController {
    @GetMapping(value = "/about")
    public Map<String, String> about(){
        Map<String, String> map = new HashMap<>();
        map.put("version", "1.0.0");
        return map;
    }
}
