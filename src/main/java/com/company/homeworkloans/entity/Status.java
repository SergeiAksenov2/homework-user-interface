package com.company.homeworkloans.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum Status implements EnumClass<Integer> {

    VALUE0(10),
    VALUE(20),
    VALUE2(30);

    private final Integer id;

    Status(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static Status fromId(Integer id) {
        for (Status at : Status.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}