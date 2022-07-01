package com.example.libraryspringboot.service;

import com.example.libraryspringboot.models.Book;
import com.example.libraryspringboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        bookRepository.deleteById(id);
    }
}
