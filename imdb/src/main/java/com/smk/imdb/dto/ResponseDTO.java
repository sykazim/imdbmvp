package com.smk.imdb.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseDTO {

    private HttpStatus status;

    private String message;
}
