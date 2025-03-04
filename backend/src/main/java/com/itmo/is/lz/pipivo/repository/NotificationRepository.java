package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
