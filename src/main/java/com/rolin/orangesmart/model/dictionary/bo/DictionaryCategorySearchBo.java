package com.rolin.orangesmart.model.dictionary.bo;

import java.util.Date;

import lombok.Data;

@Data
public class DictionaryCategorySearchBo {

    private String appName;

    private String searchWord;

    private String deepSearchWord;

    private Date startUpdateDate;

    private Date endUpdateDate;
}