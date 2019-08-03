package org.tsdes.advanced.rest.guiv1

import org.tsdes.advanced.rest.guiv1.db.Book
import org.tsdes.advanced.rest.guiv1.dto.BookDto


object DtoConverter {


    fun transform(book: Book) : BookDto {

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