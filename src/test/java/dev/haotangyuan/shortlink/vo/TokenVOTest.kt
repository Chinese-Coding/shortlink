package dev.haotangyuan.shortlink.vo

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

class TokenVOTest {
    @Test
    fun exposesIdAndUpdateTimeForListResponse() {
        val updateTime = Date(1710000000000L)
        val token = TokenVO(id = 123L, updateTime = updateTime)

        assertEquals(123L, token.id)
        assertEquals(updateTime, token.updateTime)
    }

    @Test
    fun serializesSnowflakeIdWithoutJavaScriptPrecisionLoss() {
        val token = TokenVO(id = 2075578198782910466L)

        val json = ObjectMapper().writeValueAsString(token)

        assertThat(json).contains("\"id\":\"2075578198782910466\"")
    }
}
