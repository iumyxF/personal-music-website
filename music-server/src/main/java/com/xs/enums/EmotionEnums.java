package com.xs.enums;

import lombok.Getter;

/**
 * @author fzy
 * @description: 情感类型
 * @date 2025-11-07 09:17
 */
@Getter
public enum EmotionEnums {

    /**
     * 正面
     */
    POSITIVE(0),

    /**
     * 负面
     */
    NEGATIVE(1),

    /**
     * 中性
     */
    NEUTRAL(2);

    private final Integer code;

    EmotionEnums(Integer code) {
        this.code = code;
    }
}
