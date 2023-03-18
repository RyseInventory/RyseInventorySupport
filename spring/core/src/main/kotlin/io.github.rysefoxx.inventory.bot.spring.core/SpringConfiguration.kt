/*
 * Copyright (c)  2023 Rysefoxx
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.rysefoxx.inventory.bot.spring.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
@PropertySource("classpath:spring.properties")
@ComponentScan("io.github.rysefoxx.inventory.bot")
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = ["io.github.rysefoxx.inventory.bot"],
    entityManagerFactoryRef = "gameModeEntityManager",
    transactionManagerRef = "gameModeTransactionManager"
)
open class SpringConfiguration @Autowired constructor(env: Environment) {

    private var env: Environment? = env

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    open fun gameModeDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    open fun gameModeEntityManager(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = gameModeDataSource()
        em.setPackagesToScan("io.github.rysefoxx.inventory.bot")
        return getLocalContainerEntityManagerFactoryBean(em, env!!)
    }

    @Bean
    open fun gameModeTransactionManager(): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = gameModeEntityManager().getObject()
        return transactionManager
    }

    private fun getLocalContainerEntityManagerFactoryBean(
        em: LocalContainerEntityManagerFactoryBean,
        env: Environment
    ): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties = HashMap<String, Any?>()
        properties["hibernate.hbm2ddl.auto"] = env.getProperty("hibernate.hbm2ddl.auto")
        properties["hibernate.dialect"] = env.getProperty("spring.jpa.database-platform")
        properties["hibernate.logging"] = env.getProperty("warn")
        properties["hibernate.implicit_naming_strategy"] = SpringImplicitNamingStrategy::class.java.name
        em.jpaPropertyMap = properties
        return em
    }

}