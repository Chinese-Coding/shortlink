package dev.haotangyuan.shortlink.dto.req

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.TimeZone

class TokenCreateReqDTOTest {
    private val objectMapper = ObjectMapper()

    @Test
    fun parsesConsoleDateTimeFormat() {
        val request = objectMapper.readValue(
            """
            {
              "name": "console-token",
              "validDate": "2026-06-02 23:59:59"
            }
            """.trimIndent(),
            TokenCreateReqDTO::class.java,
        )

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("GMT+8")
        }
        assertEquals(format.parse("2026-06-02 23:59:59"), request.validDate)
    }
}
