package com.annchao.springboot_mall.util;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class Page<T> {

    private Integer limit;
    private Integer offset;
    private Integer total;

    // query results from database, deata type is List of generic T
    private List<T> results;


    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getResults() {
        return this.results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }


}
