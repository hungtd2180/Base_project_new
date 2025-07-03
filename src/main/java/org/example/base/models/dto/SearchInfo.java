package org.example.base.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.base.utils.ObjectUtils;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor@Data@NoArgsConstructor
public class SearchInfo {
    private String query;
    private int pageNumber;
    private int pageSize;
    private String orders;

    public SearchInfo(String query, Pageable pageable) {
        this.query = query;
        this.pageNumber = pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        if (!ObjectUtils.isEmpty(pageable) && !ObjectUtils.isEmpty(pageable.getSort()) && !pageable.getSort().isUnsorted()) {
            this.orders = pageable.getSort().toString();
        } else {
            this.orders = "";
        }
    }
}
