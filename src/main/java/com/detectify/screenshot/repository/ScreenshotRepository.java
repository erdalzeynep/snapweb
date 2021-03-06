package com.detectify.screenshot.repository;

import com.detectify.screenshot.model.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {

    Screenshot findByIdAndScreenshotRequestId(Long id, Long requestId);

    List<Screenshot> findByScreenshotRequestId(Long requestId);
}
