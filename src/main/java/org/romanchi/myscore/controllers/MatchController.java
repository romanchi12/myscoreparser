package org.romanchi.myscore.controllers;

import org.romanchi.myscore.model.dtos.MatchDTO;
import org.romanchi.myscore.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/matches")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping(value = "/all")
    public List<MatchDTO> all(@RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "DESC") String sortDirection,
                              @RequestParam(defaultValue = "id") String sortFields){
        Sort.Direction direction = "ASC".equals(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        String [] sortFieldsArray = sortFields.split(",");
        return matchService.findAll(PageRequest.of(page, pageSize, Sort.by(direction, sortFieldsArray)))
                .stream()
                .map(MatchDTO::new)
                .collect(Collectors.toList());
    }
}
