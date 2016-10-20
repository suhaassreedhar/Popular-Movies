package com.example.suhaas.project1.model;

import java.util.List;

public class ApiResult<T> {
    private int page;
    private List<T> results;
    private int total_results;
    private int total_pages;

    public List<T> getResults() {
        return results;
    }

}
