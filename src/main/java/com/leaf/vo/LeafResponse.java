package com.leaf.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LeafResponse <T> {
    private T result;
}
