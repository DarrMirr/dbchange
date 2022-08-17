package com.github.darrmirr.dbchange.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class FileUtilsTest {

    @Test
    void readFile() {
        String fileContent = FileUtils.readFile("sql/read_file_test.sql");
        assertThat(fileContent, equalTo("select 1;" + System.lineSeparator() + "select 2;"));
    }

}