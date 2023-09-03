package com.workintech.jpa.onetomany.controller;

import com.workintech.jpa.onetomany.entity.Author;
import com.workintech.jpa.onetomany.entity.Book;
import com.workintech.jpa.onetomany.mapping.AuthorResponse;
import com.workintech.jpa.onetomany.mapping.BookResponse;
import com.workintech.jpa.onetomany.service.AuthorService;
import com.workintech.jpa.onetomany.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("author")
public class AuthorController {
    private AuthorService authorService;
    private BookService bookService;
    @Autowired
    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/")
    public List<AuthorResponse> findAll() {
       List<Author> authors = authorService.findAll();
       List<AuthorResponse>  authorResponses = new ArrayList<>();
       for(Author author : authors) {
           List<BookResponse> bookResponses = new ArrayList<>();
           for(Book book : author.getBooks()) {
               bookResponses.add(new BookResponse(book.getId(),book.getName(),book.getCategory().getName()));
           }
           authorResponses.add(new AuthorResponse(author.getId(),author.getFirstName(),author.getLastName(),
                   bookResponses));
       }
       return authorResponses;
    }
    @PostMapping("/")
    public AuthorResponse save(@RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return new AuthorResponse(savedAuthor.getId(),savedAuthor.getFirstName(),savedAuthor.getLastName(),null);
    }
    @PostMapping("/{bookId}")
    public AuthorResponse save(@RequestBody Author author,@PathVariable int bookId) {
        Book book = bookService.findById(bookId);
        author.addBook(book);
        book.setAuthor(author);
        authorService.save(author);
        return new AuthorResponse(author.getId(),author.getFirstName(),author.getLastName(),null);
    }

    @PutMapping("/{authorId}")
    public AuthorResponse update(@RequestBody Author updatedAuthor ,@PathVariable int authorId) {
        Author existingAuthor = authorService.findById(authorId);
        if (existingAuthor != null) {
            existingAuthor.setFirstName(updatedAuthor.getFirstName());
            existingAuthor.setLastName(updatedAuthor.getLastName());
            authorService.save(existingAuthor);
            return new AuthorResponse(existingAuthor.getId(), existingAuthor.getFirstName(), existingAuthor.getLastName(),
                    null);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public AuthorResponse delete(@PathVariable int id) {
        Author author = authorService.findById(id);
        if(author != null) {
            authorService.delete(author);
            return new AuthorResponse(author.getId(), author.getFirstName(), author.getLastName(), null);
        }
        return null;
    }
}
