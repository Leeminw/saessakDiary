package com.ssafy.saessak.attendance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendanceRequestDto {

    private int year;
    private int month;
    private int week;

}
