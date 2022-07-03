package com.example.libraryspringboot.controllers;

import com.example.libraryspringboot.models.Book;
import com.example.libraryspringboot.models.Person;
import com.example.libraryspringboot.service.BookService;
import com.example.libraryspringboot.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getBooksPage(Model model,
                               @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                               @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        Page<Book> books = bookService.getBooks(offset, limit);
        model.addAttribute("books", books);
        model.addAttribute("numbers", IntStream.range(0, books.getTotalPages()).toArray());
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping("/new")
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findById(id));

        Person owner = bookService.findByBookOwner(id);

        if (owner == null)
            model.addAttribute("people", peopleService.findAll());
        else
            model.addAttribute("owner", owner);

        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String changeBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.change(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBooks(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBooks(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }
}
