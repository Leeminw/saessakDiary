package com.ssafy.saessak.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="teacher")
public class Teacher extends User {

    @Column(name = "teacher_device")
    private String teacherDevice;

    @Column(name = "teacher_alarm", nullable = false)
    private boolean teacherAlarm;

    public void setToken(String token){
        this.teacherDevice = token;
    }

    public void setAlarm() {
        if(!this.teacherAlarm) {
            this.teacherAlarm = true;
        } else {
            this.teacherAlarm = false;
        }
    }

    public void mapping_classroom(Classroom classroom) {
        super.mapping_classroom(classroom);
    }

}
