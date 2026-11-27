package dev.haotangyuan.shortlink.dto.req

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PageReqDTOTest {
    @Test
    fun paginationValuesAreKeptWithinSafeBounds() {
        val request = PageReqDTO().apply {
            current = 0
            size = 10_000
        }

        assertEquals(1L, request.current)
        assertEquals(100L, request.size)
    }
}
