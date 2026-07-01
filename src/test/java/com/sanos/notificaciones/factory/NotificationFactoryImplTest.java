package com.sanos.notificaciones.factory;

import com.sanos.notificaciones.dto.NotificationDTO;
import com.sanos.notificaciones.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationFactoryImplTest {

    private NotificationFactoryImpl factory;

    @BeforeEach
    void setUp() {
        factory = new NotificationFactoryImpl();
    }

    // === toDTO ===

    @Test
    void toDTO_withValidNotification() {
        LocalDateTime now = LocalDateTime.now();
        Notification n = new Notification(1L, "Hola", "test@mail.com", now);

        NotificationDTO dto = factory.toDTO(n);

        assertNotNull(dto);
        assertEquals("Hola", dto.getMessage());
        assertEquals("test@mail.com", dto.getRecipient());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void toDTO_withNull_returnsNull() {
        assertNull(factory.toDTO(null));
    }

    @Test
    void toDTO_preservesAllFields() {
        LocalDateTime ts = LocalDateTime.of(2026, 6, 30, 15, 30);
        Notification n = new Notification(99L, "Mensaje importante", "admin@sanos.com", ts);

        NotificationDTO dto = factory.toDTO(n);

        assertEquals("Mensaje importante", dto.getMessage());
        assertEquals("admin@sanos.com", dto.getRecipient());
        assertEquals(ts, dto.getTimestamp());
    }

    // === toEntity ===

    @Test
    void toEntity_withValidDTO() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDTO dto = new NotificationDTO("Alerta", "user@mail.com", now);

        Notification entity = factory.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Alerta", entity.getMessage());
        assertEquals("user@mail.com", entity.getRecipient());
        assertEquals(now, entity.getTimestamp());
        assertNull(entity.getId()); // ID no se setea
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(factory.toEntity(null));
    }

    @Test
    void toEntity_doesNotSetId() {
        NotificationDTO dto = new NotificationDTO("msg", "r@mail.com", LocalDateTime.now());
        Notification entity = factory.toEntity(dto);
        assertNull(entity.getId());
    }

    // === Round-trip ===

    @Test
    void roundTrip_toDTO_thenToEntity() {
        LocalDateTime ts = LocalDateTime.of(2026, 1, 15, 8, 0);
        Notification original = new Notification(10L, "Round trip", "rt@mail.com", ts);

        NotificationDTO dto = factory.toDTO(original);
        Notification entity = factory.toEntity(dto);

        assertEquals(original.getMessage(), entity.getMessage());
        assertEquals(original.getRecipient(), entity.getRecipient());
        assertEquals(original.getTimestamp(), entity.getTimestamp());
        assertNull(entity.getId()); // ID no se copia en round-trip
    }
}
