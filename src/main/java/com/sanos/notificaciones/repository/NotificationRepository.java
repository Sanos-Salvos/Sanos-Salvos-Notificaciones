package com.sanos.notificaciones.repository;

import com.sanos.notificaciones.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}