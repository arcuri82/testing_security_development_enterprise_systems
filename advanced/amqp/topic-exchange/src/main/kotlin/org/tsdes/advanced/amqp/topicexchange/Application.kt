package org.tsdes.advanced.amqp.topicexchange

import org.springframework.amqp.core.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    /*
        The last kind of exchange is "Topic".
        The routing toward queues bound on the exchange
        is based on the regular expressions on the
        routing key.
        This provides fine grained control.

        A "topic" is 1 or more words separated by "."
        For example, a topic could be

        foo.bar.hello

        There are 2 special characters:
        * -> substitute for exactly one word
        # -> substitute for zero or more words.
     */

    @Bean
    fun topic(): TopicExchange {
        return TopicExchange("tut.topic")
    }

    @Bean
    fun queueX(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun queueY(): Queue {
        return AnonymousQueue()
    }


    /*
        Example of publishing "news".
        Considering topic in this form:

        author.country.kind
     */

    @Bean
    fun bindingX(topic: TopicExchange,
                 queueX: Queue): Binding {
        return BindingBuilder
                .bind(queueX)
                .to(topic)
                .with("smith.#")

        /*
            binding this queue to a topic starting with the
            word "smith", followed by any word.
            In other words, we bind this queue to receive
            news written by "smith"
         */

    }

    @Bean
    fun bindingY(topic: TopicExchange,
                 queueY: Queue): Binding {
        return BindingBuilder
                .bind(queueY)
                .to(topic)
                .with("*.norway.*")

        /*
            any word, followed by "norway", followed by any word.
            In other words, we bind this queue to receive
            news in "norway" regardless of the author and kind.
         */
    }


}