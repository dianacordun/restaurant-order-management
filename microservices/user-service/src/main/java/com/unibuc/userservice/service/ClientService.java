package com.unibuc.userservice.service;

import com.unibuc.userservice.dto.ClientDTO;
import com.unibuc.userservice.model.Client;
import com.unibuc.userservice.repository.ClientRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ClientDTO> getAllClients() {
        clientRetrievedCounter.increment();
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        clientRetrievedCounter.increment();
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToDTO(client);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Optional<Client> getClientByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    public ClientDTO addClient(ClientDTO clientDTO) {
        // Check if email already exists
        if (clientDTO.getEmail() != null && clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new RuntimeException("Client with email '" + clientDTO.getEmail() + "' already exists");
        }

        // Check if phone number already exists
        if (clientRepository.existsByPhoneNumber(clientDTO.getPhoneNumber())) {
            throw new RuntimeException("Client with phone number '" + clientDTO.getPhoneNumber() + "' already exists");
        }

        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setEmail(clientDTO.getEmail());

        Client savedClient = clientRepository.save(client);
        clientCreatedCounter.increment();
        return convertToDTO(savedClient);
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (clientDTO.getEmail() != null && !clientDTO.getEmail().equals(client.getEmail())) {
            if (clientRepository.existsByEmail(clientDTO.getEmail())) {
                throw new RuntimeException("Client with email '" + clientDTO.getEmail() + "' already exists");
            }
        }

        // Check if phone number is being changed and if new phone number already exists
        if (!clientDTO.getPhoneNumber().equals(client.getPhoneNumber())) {
            if (clientRepository.existsByPhoneNumber(clientDTO.getPhoneNumber())) {
                throw new RuntimeException("Client with phone number '" + clientDTO.getPhoneNumber() + "' already exists");
            }
        }

        client.setName(clientDTO.getName());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setEmail(clientDTO.getEmail());

        Client updatedClient = clientRepository.save(client);
        return convertToDTO(updatedClient);
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    public ClientDTO findByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + email));
        return convertToDTO(client);
    }

    public ClientDTO findByPhoneNumber(String phoneNumber) {
        Client client = clientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Client not found with phone number: " + phoneNumber));
        return convertToDTO(client);
    }

    private ClientDTO convertToDTO(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getPhoneNumber(), client.getEmail());
    }
}
