package com.devs.powerfit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> items;
    private int totalPages;
    private long totalItems;
    private int currentPage;
}
