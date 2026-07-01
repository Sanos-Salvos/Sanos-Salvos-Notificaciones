package com.sanos.notificaciones.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testNoArgsConstructor() {
        Notification n = new Notification();
        assertNull(n.getId());
        assertNull(n.getMessage());
        assertNull(n.getRecipient());
        assertNull(n.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Notification n = new Notification(1L, "Hola", "test@mail.com", now);
        assertEquals(1L, n.getId());
        assertEquals("Hola", n.getMessage());
        assertEquals("test@mail.com", n.getRecipient());
        assertEquals(now, n.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        Notification n = new Notification();
        n.setId(5L);
        n.setMessage("msg");
        n.setRecipient("r@mail.com");
        LocalDateTime ts = LocalDateTime.of(2026, 6, 30, 12, 0);
        n.setTimestamp(ts);

        assertEquals(5L, n.getId());
        assertEquals("msg", n.getMessage());
        assertEquals("r@mail.com", n.getRecipient());
        assertEquals(ts, n.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Notification n1 = new Notification(1L, "msg", "r@mail.com", now);
        Notification n2 = new Notification(1L, "msg", "r@mail.com", now);
        assertEquals(n1, n2);
        assertEquals(n1.hashCode(), n2.hashCode());
    }

    @Test
    void testNotEquals() {
        Notification n1 = new Notification(1L, "msg1", "r@mail.com", LocalDateTime.now());
        Notification n2 = new Notification(2L, "msg2", "r@mail.com", LocalDateTime.now());
        assertNotEquals(n1, n2);
    }

    @Test
    void testToString() {
        Notification n = new Notification(1L, "msg", "r@mail.com", LocalDateTime.now());
        String str = n.toString();
        assertTrue(str.contains("msg"));
        assertTrue(str.contains("r@mail.com"));
    }
}
