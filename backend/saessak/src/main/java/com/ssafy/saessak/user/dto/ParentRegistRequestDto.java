package com.ssafy.saessak.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class ParentRegistRequestDto {

    private String parentName;
    private String parentEmail;

}
