package com.nish.ms.service;

import com.nish.ms.exception.ElementNotFoundException;
import com.nish.ms.model.Customer;
import com.nish.ms.repo.CustomerRepoIface;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CustomerService {

    @Autowired
    CustomerRepoIface repo;


    public List<Customer> findAll(){

        log.debug("Find all method in Service called");

        return this.repo.findAll();
    }

    public Customer add(Customer entity) {

        log.debug("Add Restaurant Method in service called");

        return this.repo.save(entity);
    }

    public Customer findById(int id) {

        log.debug("Find By Id in Service  Called");


        return this.repo.findById(id)
                .orElseThrow( () -> new ElementNotFoundException("Element with Given Id Not found"));

    }

    public Optional<Integer> remove(int id) {

        Optional<Integer> optional = Optional.empty();

        if(this.repo.existsById(id)) {

            this.repo.deleteById(id);

            optional = Optional.of(id);
        }
        return optional;
    }

    public Customer saveOrUpdate(Customer entity) {

        // save method works like a upsert if there is an record with the id is present it updates it
        // else it inserts a new record

        return this.repo.save(entity);
    }



}

