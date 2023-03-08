package io.github.rysefoxx.inventory.bot.spring

import io.github.rysefoxx.inventory.bot.Bootstrap
import io.github.rysefoxx.inventory.bot.log.Logger
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.sql.SQLException
import javax.sql.DataSource

@Component
class TableInitialization @Autowired constructor(private var dataSource: DataSource) {

    init {
        buildTables()
    }

    private fun buildTables() {
        val data = try {
            Bootstrap::class.java.classLoader.getResourceAsStream("data.sql")
                .use { inputStream ->
                    String(IOUtils.toByteArray(inputStream), Charsets.UTF_8)
                        .split(";")
                        .filter { it.isNotBlank() }
                        .toList()
                }
        } catch (e: IOException) {
            Logger.error("Could not load data.sql", e)
            return
        }

        data.forEach { query ->
            try {
                dataSource.connection.use { connection ->
                    connection.prepareStatement(query).use { statement ->
                        statement.execute()
                    }
                }
            } catch (e: SQLException) {
                Logger.error("Failed to execute query: $query", e)
            }
        }
    }
}