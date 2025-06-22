package com.unibuc.userservice.service;

import com.unibuc.userservice.model.Client;
import com.unibuc.userservice.repository.ClientRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final Counter clientCreatedCounter;
    private final Counter clientRetrievedCounter;

    @Autowired
    public ClientService(ClientRepository clientRepository, MeterRegistry meterRegistry) {
        this.clientRepository = clientRepository;
        this.clientCreatedCounter = Counter.builder("client_created_total")
                .description("Total number of clients created")
                .register(meterRegistry);
        this.clientRetrievedCounter = Counter.builder("client_retrieved_total")
                .description("Total number of client retrieval operations")
                .register(meterRegistry);
    }

    public List<Client> getAllClients() {
        clientRetrievedCounter.increment();
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        clientRetrievedCounter.increment();
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Optional<Client> getClientByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    public Client createClient(Client client) {
        Client savedClient = clientRepository.save(client);
        clientCreatedCounter.increment();
        return savedClient;
    }

    public Client updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(clientDetails.getName());
                    client.setEmail(clientDetails.getEmail());
                    client.setPhoneNumber(clientDetails.getPhoneNumber());
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }
}
