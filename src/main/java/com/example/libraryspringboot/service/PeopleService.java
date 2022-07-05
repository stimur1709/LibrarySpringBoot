package com.example.libraryspringboot.service;

import com.example.libraryspringboot.models.Book;
import com.example.libraryspringboot.models.Person;
import com.example.libraryspringboot.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    public Page<Person> findAll(int offset, int limit, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable nextPage = PageRequest.of(offset, limit, sort);
        return peopleRepository.findAll(nextPage);
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public void save(Person newPerson) {
        peopleRepository.save(newPerson);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public Person show(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public void change(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    public void delete(int id) {
        peopleRepository.getById(id).getBookList().forEach(book -> book.setPerson(null));
        peopleRepository.deleteById(id);
    }

    @Transactional
    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            person.get().getBookList().forEach(book -> {
                long difference = Math.abs(book.getTimeAssign().getTime() - new Date().getTime());
                if(difference > 60000)
                    book.setOverdue(true);
            });
            return person.get().getBookList();
        }
        else
            return Collections.emptyList();
    }
}
