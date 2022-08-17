package com.github.darrmirr.dbchange.sql.mapper;

import java.sql.ResultSet;

@FunctionalInterface
public interface ResultMapper<T> {

    T map(ResultSet resultSet);
}
