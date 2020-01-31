package com.detectify.screenshot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ScreenshotRequest extends AbstractEntity {

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date requestDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "screenshotRequest", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("screenshotRequest")
    private List<Screenshot> screenshotList = new ArrayList<>();

    public ScreenshotRequest() {
    }

    public List<Screenshot> getScreenshotList() {
        return screenshotList;
    }

    public void setScreenshotList(List<Screenshot> screenshotList) {
        this.screenshotList = screenshotList;
    }
}

