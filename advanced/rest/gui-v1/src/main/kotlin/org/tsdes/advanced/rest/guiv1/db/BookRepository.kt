package org.tsdes.advanced.rest.guiv1.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.tsdes.advanced.rest.guiv1.db.Book

@Repository
interface BookRepository : CrudRepository<Book, Long>