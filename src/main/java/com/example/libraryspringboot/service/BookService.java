package com.example.libraryspringboot.service;

import com.example.libraryspringboot.models.Book;
import com.example.libraryspringboot.models.Person;
import com.example.libraryspringboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void change(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    public void delete(int id) {
        bookRepository.findById(id).ifPresent(value -> value.setPerson(null));
        bookRepository.deleteById(id);
    }

    public Person findByBookOwner(int id) {
        return bookRepository.findById(id).map(Book::getPerson).orElse(null);
    }

    @Transactional
    public void assign(int id, Person person) {
        bookRepository.findById(id).ifPresent(book -> book.setPerson(person));
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(book -> book.setPerson(null));
    }
}
