package com.github.darrmirr.dbchange.sql.executor;

import com.github.darrmirr.dbchange.changeset.ChangeSetItem;

import java.util.List;

/**
 * Interface for objects that executes sql queries against RDBMS.
 */
public interface SqlExecutor {

     /**
      * Execute sql queries against RDBMS.
      *
      * @param changeSet list of changes to apply against RDBMS.
      */
     void execute(List<ChangeSetItem> changeSet);
}

