package com.sanos.notificaciones.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class NotificationDTOTest {

    @Test
    void testNoArgsConstructor() {
        NotificationDTO dto = new NotificationDTO();
        assertNull(dto.getMessage());
        assertNull(dto.getRecipient());
        assertNull(dto.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDTO dto = new NotificationDTO("msg", "r@mail.com", now);
        assertEquals("msg", dto.getMessage());
        assertEquals("r@mail.com", dto.getRecipient());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        NotificationDTO dto = new NotificationDTO();
        dto.setMessage("hello");
        dto.setRecipient("user@mail.com");
        LocalDateTime ts = LocalDateTime.of(2026, 1, 1, 10, 30);
        dto.setTimestamp(ts);

        assertEquals("hello", dto.getMessage());
        assertEquals("user@mail.com", dto.getRecipient());
        assertEquals(ts, dto.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDTO d1 = new NotificationDTO("msg", "r@mail.com", now);
        NotificationDTO d2 = new NotificationDTO("msg", "r@mail.com", now);
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testNotEquals() {
        NotificationDTO d1 = new NotificationDTO("msg1", "r@mail.com", LocalDateTime.now());
        NotificationDTO d2 = new NotificationDTO("msg2", "r@mail.com", LocalDateTime.now());
        assertNotEquals(d1, d2);
    }

    @Test
    void testToString() {
        NotificationDTO dto = new NotificationDTO("msg", "r@mail.com", LocalDateTime.now());
        String str = dto.toString();
        assertTrue(str.contains("msg"));
        assertTrue(str.contains("r@mail.com"));
    }
}
