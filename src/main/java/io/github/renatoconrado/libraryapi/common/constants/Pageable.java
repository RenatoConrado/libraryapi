package io.github.renatoconrado.libraryapi.common.constants;

public class Pageable {
    public static final byte INITIAL_PAGE = 0;
    public static final byte INITIAL_PAGE_SIZE = 10;
    public static final byte MIN_PAGE = 0;
    public static final byte MIN_PAGE_SIZE = 1;

    public static int normalizePage(Integer page) {
        boolean pageIsSafe = (page != null) && (page >= MIN_PAGE);
        return pageIsSafe ? page : INITIAL_PAGE;
    }

    public static int normalizePageSize(Integer pageSize) {
        boolean pageSizeIsSafe = (pageSize != null) && (pageSize >= MIN_PAGE_SIZE);
        return pageSizeIsSafe ? pageSize : INITIAL_PAGE_SIZE;
    }
}
