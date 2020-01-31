package com.detectify.screenshot.repository;

import com.detectify.screenshot.model.ScreenshotRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenshotRequestRepository extends JpaRepository<ScreenshotRequest, Long> {
}
