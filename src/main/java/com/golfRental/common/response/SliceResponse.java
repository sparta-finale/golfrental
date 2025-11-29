package com.golfRental.common.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class SliceResponse<T> {

    private final List<T> content;
    private final int size;
    private final int number;
    private final boolean hasNext;

    @JsonCreator // Jackson에게 역직렬화 시 이 생성자를 사용하라고 명시
    public SliceResponse(
            @JsonProperty("content") List<T> content,       // JSON의 "content" 필드와 매핑
            @JsonProperty("size") int size,                 // JSON의 "size" 필드와 매핑
            @JsonProperty("number") int number,             // JSON의 "number" 필드와 매핑
            @JsonProperty("hasNext") boolean hasNext        // JSON의 "hasNext" 필드와 매핑
    ) {
        this.content = content;
        this.size = size;
        this.number = number;
        this.hasNext = hasNext;
    }

    // Slice -> SliceResponse 변환 메서드
    public static <T> SliceResponse<T> fromSlice(Slice<T> slicedData) {
        return new SliceResponse<>(
                slicedData.getContent(),
                slicedData.getPageable().getPageSize(),
                slicedData.getPageable().getPageNumber(),
                slicedData.hasNext()
        );
    }
}
