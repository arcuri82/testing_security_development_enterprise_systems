package org.tsdes.advanced.rest.evomaster

import com.p6spy.engine.spy.P6SpyDriver
import org.evomaster.client.java.controller.EmbeddedSutController
import org.evomaster.client.java.controller.InstrumentedSutStarter
import org.evomaster.client.java.controller.api.dto.AuthenticationDto
import org.evomaster.client.java.controller.api.dto.SutInfoDto
import org.evomaster.client.java.controller.db.DbCleaner
import org.evomaster.client.java.controller.problem.ProblemInfo
import org.evomaster.client.java.controller.problem.RestProblem
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.tsdes.advanced.rest.guiv1.BookApplication
import java.sql.Connection


class EvoMasterDriver : EmbeddedSutController() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val controller = EvoMasterDriver()
            val starter = InstrumentedSutStarter(controller)

            starter.start()
        }
    }

    private var ctx: ConfigurableApplicationContext? = null
    private var connection: Connection? = null


    protected fun getSutPort(): Int {
        return (ctx!!.environment
                .propertySources.get("server.ports")!!
                .source as Map<*, *>)["local.server.port"] as Int
    }


    override fun getPackagePrefixesToCover(): String {
        return "org.tsdes.advanced"
    }

    override fun getConnection(): Connection? {
        return connection
    }

    override fun getPreferredOutputFormat(): SutInfoDto.OutputFormat {
        return SutInfoDto.OutputFormat.KOTLIN_JUNIT_5
    }

    override fun isSutRunning(): Boolean {
        return ctx != null && ctx!!.isRunning
    }

    override fun resetStateOfSUT() {
        DbCleaner.clearDatabase_H2(connection)
    }

    override fun startSut(): String {

        ctx = SpringApplication.run(BookApplication::class.java,
                "--server.port=0",
                "--spring.datasource.url=jdbc:p6spy:h2:mem:testdb;DB_CLOSE_DELAY=-1;",
                "--spring.datasource.driver-class-name=" + P6SpyDriver::class.java.name,
                "--spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "--spring.datasource.username=sa",
                "--spring.datasource.password"
        )!!


        connection?.close()

        val jdbc = ctx!!.getBean(JdbcTemplate::class.java)
        connection = jdbc.dataSource!!.connection

        return "http://localhost:" + getSutPort()
    }

    override fun stopSut() {
        ctx?.stop()
    }

    override fun getInfoForAuthentication(): MutableList<AuthenticationDto>? {
        return null
    }

    override fun getProblemInfo(): ProblemInfo {
        return RestProblem(
                "http://localhost:" + getSutPort() + "/v2/api-docs",
                listOf("/error")
        )
    }

    override fun getDatabaseDriverName(): String {
        return "org.h2.Driver"
    }
}