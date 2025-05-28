package io.github.renatoconrado.libraryapi.repository;

import io.github.renatoconrado.libraryapi.model.Author;
import io.github.renatoconrado.libraryapi.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest class TransactionTest {

    private final AuthorRepository authorRepository;

    @Autowired TransactionTest(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    @Test void TransactionalTest() {
        Book book1 = new Book();
        Author author = new Author();
        Book book2 = new Book();
    }
}
