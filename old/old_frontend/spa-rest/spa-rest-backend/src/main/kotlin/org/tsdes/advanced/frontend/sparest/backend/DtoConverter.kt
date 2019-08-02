package org.tsdes.advanced.frontend.sparest.backend

import org.tsdes.advanced.frontend.sparest.backend.db.Book
import org.tsdes.advanced.frontend.sparest.dto.BookDto


object DtoConverter {


    fun transform(book: Book) : BookDto{

        return BookDto(
                title = book.title,
                author = book.author,
                year = book.year,
                id = book.id.toString()
        )
    }


    fun transform(books: Iterable<Book>) : List<BookDto>{

        return books.map { transform(it) }
    }
}