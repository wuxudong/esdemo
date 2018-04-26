package com.github.wuxudong.esdemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "eshop", type = "sku")
public class Sku {
    @Id
    private Long id;

    @Field(type = FieldType.text)
    private String title;

    @Field(type = FieldType.text)
    private String detail;
    private int rank;
    private Date checkTime;

    @Override
    public String toString() {
        return "Sku{" +
                "id=" + id +
                ", rank=" + rank +
                ", checkTime=" + checkTime +
                '}';
    }
}
