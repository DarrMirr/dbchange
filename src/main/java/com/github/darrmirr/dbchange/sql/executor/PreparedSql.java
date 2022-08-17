package com.github.darrmirr.dbchange.sql.executor;

import java.util.*;

/**
 * Class contains all necessary information to apply into {@link java.sql.PreparedStatement}.
 */
public final class PreparedSql {
    private final Map<String, List<Integer>> indexMap;
    private final String preparedSql;

    private PreparedSql(String preparedSql, Map<String, List<Integer>> indexMap) {
        this.preparedSql = preparedSql;
        this.indexMap = Collections.unmodifiableMap(indexMap);
    }

    public String get() {
        return preparedSql;
    }

    public List<Integer> getIndexes(String name) {
        List<Integer> indexes = indexMap.get(name);
        return indexes == null ? Collections.emptyList() : Collections.unmodifiableList(indexes);
    }

    /**
     * Convert named JDBC parameters to JDBC one due to JDBC specification does not support named parameters.
     *
     * implementation provided by @author adam_crume
     * source: https://www.infoworld.com/article/2077706/named-parameters-for-preparedstatement.html?page=2
     *
     * @param query query to parse.
     * @return instance of {@link PreparedSql}.
     */
    public static PreparedSql of(String query) {
        Map<String, List<Integer>> indexMap = new HashMap<>();
        // parameter-like strings inside quotes.
        int length = query.length();
        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
                        j++;
                    }
                    String name = query.substring(i + 1, j);
                    c = '?'; // replace the parameter with a question mark
                    i += name.length(); // skip past the end if the parameter

                    List<Integer> indexList = indexMap.computeIfAbsent(name, k -> new LinkedList<>());
                    indexList.add(index);

                    index++;
                }
            }
            parsedQuery.append(c);
        }

        return new PreparedSql(parsedQuery.toString(), indexMap);
    }
}
