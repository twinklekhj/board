package io.github.twinklekhj.board.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class PageVO<T> implements Serializable {
    private T[] items;
    private long count;

    private int pageIdx;
    private int pageCnt;
    private int pageSize;

    public static <T> PageVOBuilder<T> builder(){
        return new PageVOBuilder<T>();
    }

    public static <T> PageVOBuilder<T> builder(List<T> items) {
        return (PageVOBuilder<T>) builder()
                .items(items.toArray())
                .pageSize(items.size())
                .count(items.size());
    }

    public static <T> PageVOBuilder<T> builder(T... items) {
        return (PageVOBuilder<T>) builder()
                .items(items).
                pageSize(items.length)
                .count(items.length);
    }

    public static <T> PageVOBuilder<T> builder(Page<T> page){
        return (PageVOBuilder<T>) builder()
                .items(page.toList().toArray())
                .count(page.getTotalElements())
                .pageIdx(page.getNumber() + 1)
                .pageSize(page.getSize())
                .pageCnt(page.getTotalPages());
    }
}
