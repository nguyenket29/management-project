package com.hau.ketnguyen.it.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface PageableUtils {
    int MAX_ITEM_PER_PAGE = 100;

    static Pageable of(int page, int size) {
        return PageRequest.of(page, Math.min(size, MAX_ITEM_PER_PAGE));
    }

    static Pageable of(int page, int size, String sort, String sortName) {
        Sort sorts;
        if ("asc".equalsIgnoreCase(sort)) {
            sorts = Sort.by(sortName).ascending();
        } else {
            sorts = Sort.by(sortName).descending();
        }
        return PageRequest.of(page, Math.min(size, MAX_ITEM_PER_PAGE), sorts);
    }
}
