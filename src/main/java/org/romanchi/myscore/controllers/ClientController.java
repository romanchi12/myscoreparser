package org.romanchi.myscore.controllers;

import org.romanchi.myscore.client.Client;
import org.romanchi.myscore.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "/loadMoreMatches")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity loadMoreMatches(){
        clientService.loadMoreMatches();
        return ResponseEntity.ok("OK");
    }
}
