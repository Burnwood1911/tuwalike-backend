package com.tuwalike.wedding.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NetworkResponse {

    public Boolean isSuccess;

    private String responseBody;

    private int code;

}
