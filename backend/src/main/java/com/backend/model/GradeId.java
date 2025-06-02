package com.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GradeId implements Serializable {
    private Long testID;
    private Integer userID;

    public Long getTestID() {
        return testID;
    }
    public void setTestID(Long testID) {
        this.testID = testID;
    }
    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}