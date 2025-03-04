package com.itmo.is.lz.pipivo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "beers")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BeerDocument {
    @Id
    private Long id;

    @Field(type=FieldType.Text)
    private String name;

    @Field(type=FieldType.Long)
    private Long abv;

    @Field(type=FieldType.Long)
    private Long ibu;

    @Field(type=FieldType.Long)
    private Long og;

    @Field(type=FieldType.Long)
    private Long srm;

    @Field(type=FieldType.Double)
    private Double price;

    @Field(type=FieldType.Integer)
    private Integer fermentation_type;

    @Field(type=FieldType.Double)
    private Double volume;

    @Field(type=FieldType.Text)
    private String country;

    @Field(type=FieldType.Text)
    private String image_path;

}