package org.romanchi.myscore.services;

import org.romanchi.myscore.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class ClientService {
    private final Client client;

    @Autowired
    public ClientService(Client client) {
        this.client = client;
    }

    public void loadMoreMatches() throws ParseException {
        client.loadMoreMatches();
    }

    public void parse() throws ParseException {
        client.parse();
    }
}
