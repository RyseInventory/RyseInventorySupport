package io.github.rysefoxx.inventory.bot.spring.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
open class Application {
    companion object {
        lateinit var context: ConfigurableApplicationContext
    }
}