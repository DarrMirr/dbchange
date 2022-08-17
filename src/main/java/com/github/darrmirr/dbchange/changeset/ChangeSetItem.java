package com.github.darrmirr.dbchange.changeset;

import java.util.Collections;
import java.util.Map;

/**
 * One instance of {@link ChangeSetItem} describes one change into RDBMS.
 * Object immutable after its creation.
 */
public class ChangeSetItem {
    private final String query;
    private final Map<String, Object> params;

    public ChangeSetItem(String query, Map<String, Object> params) {
        this.query = query;
        this.params = Collections.unmodifiableMap(params);
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
