package com.rolin.orangesmart.exception.errorEnum;

import com.rolin.orangesmart.exception.ErrorCodeContext;
import com.rolin.orangesmart.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum DictionaryErrorEnum implements ErrorEnum {

    BASE_DICTIONARY_CATEGORY_EXIST_ERROR("0001", "已存在相同字典分類"),
    BASE_DICTIONARY_CATEGORY_DELETE_ERROR("0004", "不存在該條數據,不可刪除"),

    BASE_DICTIONARY_TAG_EXIST_ERROR("0101", "已存在相同字典項"),
    BASE_DICTIONARY_TAG_NULL_ERROR("0102", "字典项编号、字典项类别Id不能为空"),
    BASE_DICTIONARY_TAG_DELETE_ERROR("0104", "不存在該條數據,不可刪除"),

    BASE_DICTIONARY_ITEM_EXIST_ERROR("0201", "已存在相同语言字典項"),
    BASE_SECURITY_ITEM_NOT_EXIST_ERROR("0202", "不存在该字典項多语言Id数据"),
    ;
    private final String code;

    private final String message;

    private DictionaryErrorEnum(String code, String message) {
        this.code = ErrorCodeContext.of(super.getClass().getSimpleName(), code);
        this.message = message;
    }

}