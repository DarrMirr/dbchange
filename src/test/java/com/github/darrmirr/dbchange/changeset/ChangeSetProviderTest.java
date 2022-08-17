package com.github.darrmirr.dbchange.changeset;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class ChangeSetProviderTest {

    @Test
    void chain() {
        ChangeSetItem item1 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item4 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item5 = new ChangeSetItem("null", Collections.emptyMap());

        ChangeSetProvider provider1 = () -> Arrays.asList(item1, item2, item3);
        ChangeSetProvider provider2 = () -> Arrays.asList(item4, item5);

        ChangeSetProvider mergedProvider = provider1.chain(provider2);

        assertThat(mergedProvider, not(sameInstance(provider1)));
        assertThat(mergedProvider, not(sameInstance(provider2)));
        assertThat(mergedProvider.getChangeSet(), hasSize(5));
        assertThat(mergedProvider.getChangeSet(), containsInAnyOrder(item1, item2, item3, item4, item5));
        assertThat(provider1.getChangeSet(), hasSize(3));
        assertThat(provider1.getChangeSet(), containsInAnyOrder(item1, item2, item3));
        assertThat(provider2.getChangeSet(), hasSize(2));
        assertThat(provider2.getChangeSet(), containsInAnyOrder(item4, item5));
    }

    @Test
    void chainNull() {
        ChangeSetItem item1 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("null", Collections.emptyMap());

        ChangeSetProvider provider1 = () -> Arrays.asList(item1, item2, item3);

        ChangeSetProvider mergedProvider = provider1.chain(null);

        assertThat(mergedProvider, not(sameInstance(provider1)));
        assertThat(mergedProvider.getChangeSet(), containsInAnyOrder(item1, item2, item3));
    }

    @Test
    void chainNullListProvider2() {
        ChangeSetItem item1 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("null", Collections.emptyMap());

        ChangeSetProvider provider1 = () -> Arrays.asList(item1, item2, item3);
        ChangeSetProvider provider2 = () -> null;

        ChangeSetProvider mergedProvider = provider1.chain(provider2);

        assertThat(mergedProvider, not(sameInstance(provider1)));
        assertThat(mergedProvider, not(sameInstance(provider2)));
        assertThat(mergedProvider.getChangeSet(), containsInAnyOrder(item1, item2, item3));
    }

    @Test
    void chainNullListProvider1() {
        ChangeSetItem item1 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("null", Collections.emptyMap());

        ChangeSetProvider provider1 = () -> null;
        ChangeSetProvider provider2 = () -> Arrays.asList(item1, item2, item3);

        ChangeSetProvider mergedProvider = provider1.chain(provider2);

        assertThat(mergedProvider, not(sameInstance(provider1)));
        assertThat(mergedProvider, not(sameInstance(provider2)));
        assertThat(mergedProvider.getChangeSet(), containsInAnyOrder(item1, item2, item3));
    }

    @Test
    void chainEmptyListProvider2() {
        ChangeSetItem item1 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("null", Collections.emptyMap());

        ChangeSetProvider provider1 = () -> Arrays.asList(item1, item2, item3);
        ChangeSetProvider provider2 = Collections::emptyList;

        ChangeSetProvider mergedProvider = provider1.chain(provider2);

        assertThat(mergedProvider, not(sameInstance(provider1)));
        assertThat(mergedProvider, not(sameInstance(provider2)));
        assertThat(mergedProvider.getChangeSet(), containsInAnyOrder(item1, item2, item3));
    }

    @Test
    void chainEmptyListProvider1() {
        ChangeSetItem item1 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("null", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("null", Collections.emptyMap());

        ChangeSetProvider provider1 = Collections::emptyList;
        ChangeSetProvider provider2 = () -> Arrays.asList(item1, item2, item3);

        ChangeSetProvider mergedProvider = provider1.chain(provider2);

        assertThat(mergedProvider, not(sameInstance(provider1)));
        assertThat(mergedProvider, not(sameInstance(provider2)));
        assertThat(mergedProvider.getChangeSet(), containsInAnyOrder(item1, item2, item3));
    }
}