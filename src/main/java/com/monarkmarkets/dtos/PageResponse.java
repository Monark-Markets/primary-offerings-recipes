package com.monarkmarkets.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
	private Pagination pagination;
	private List<T> items;
}