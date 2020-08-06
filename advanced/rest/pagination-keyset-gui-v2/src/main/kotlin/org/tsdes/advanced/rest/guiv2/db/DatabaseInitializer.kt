package org.tsdes.advanced.rest.guiv2.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class DatabaseInitializer(
        @Autowired val repository: BookRepository
) {

    @PostConstruct
    fun initialize(){
        repository.run {
            deleteAll()
            save(Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979))
            save(Book("The Lord of the Rings", "J. R. R. Tolkien", 1954))
            save(Book("The Last Wish", "Andrzej Sapkowski", 1993))
            save(Book("A Game of Thrones", "George R. R. Martin", 1996))
            save(Book("The Call of Cthulhu", "H. P. Lovecraft", 1928))
        }
        for(i in 0 until 100){
            repository.save(Book("Fake $i", "Foo", 1900 + (i/3)))
        }
    }
}