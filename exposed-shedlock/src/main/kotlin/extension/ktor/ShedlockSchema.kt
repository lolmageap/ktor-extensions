package extension.ktor

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

object Shedlocks : Table("shedlock") {
    val name: Column<String> = varchar("name", 255).uniqueIndex()
    val lockedAt = datetime("locked_at").default(now)
    val lockUntil = datetime("lock_until")

    override val primaryKey = PrimaryKey(name)
}

data class Shedlock(
    val name: String,
    val lockedAt: LocalDateTime,
    val lockUntil: LocalDateTime,
) {
    companion object {
        fun of(
            resultRow: ResultRow,
        ) =
            Shedlock(
                name = resultRow[Shedlocks.name],
                lockedAt = resultRow[Shedlocks.lockedAt],
                lockUntil = resultRow[Shedlocks.lockUntil],
            )
    }
}

private val now
    get() = ZonedDateTime.now(UTC).toLocalDateTime()