package com.cgsit.training.advancedrest.model;

import java.util.List;

/**
 * Generic search result wrapper — the standard response format for paginated queries.
 *
 * This is the pattern used by Angular/React frontends (Material Table, PrimeNG, etc.):
 *
 *   - content:        the data for the current page
 *   - page:           current page number (0-based)
 *   - size:           page size (items per page)
 *   - totalElements:  total number of matching items across all pages
 *   - totalPages:     total number of pages
 *   - first / last:   convenience flags for pagination controls
 *   - sortBy:         which field is sorted
 *   - sortDirection:  ASC or DESC
 *
 * Example JSON response:
 * {
 *   "content": [ { "id": 1, "name": "Keyboard" }, ... ],
 *   "page": 0,
 *   "size": 20,
 *   "totalElements": 142,
 *   "totalPages": 8,
 *   "first": true,
 *   "last": false,
 *   "sortBy": "name",
 *   "sortDirection": "ASC"
 * }
 *
 * The frontend uses this to render:
 *   - Paginator component (page, size, totalElements)
 *   - "Showing 1-20 of 142 results"
 *   - Sort headers (sortBy, sortDirection)
 *   - Disable next/prev buttons (first, last)
 *
 * @param <T> the type of items in the result (e.g. Product, Order)
 */
public record SearchResult<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last,
    String sortBy,
    SortDirection sortDirection
) {
    /**
     * Factory method — creates a SearchResult from a full list by applying paging.
     */
    public static <T> SearchResult<T> of(List<T> allItems, int page, int size,
                                          String sortBy, SortDirection sortDirection) {
        long totalElements = allItems.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = Math.min(page * size, allItems.size());
        int toIndex = Math.min(fromIndex + size, allItems.size());
        List<T> content = allItems.subList(fromIndex, toIndex);

        return new SearchResult<>(
            content,
            page,
            size,
            totalElements,
            totalPages,
            page == 0,
            page >= totalPages - 1,
            sortBy,
            sortDirection
        );
    }
}
