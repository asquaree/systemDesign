package com.example.monzobank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlVistedModel {

    private String url;
    private boolean visited;
    private LocalDateTime lastAccessTime;
}
