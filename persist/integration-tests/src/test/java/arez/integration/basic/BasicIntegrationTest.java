package arez.integration.basic;

import static org.testng.Assert.*;

import arez.Arez;
import arez.integration.AbstractIntegrationTest;
import arez.persist.StoreTypes;
import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

public final class BasicIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void scenario() {
        // a super simple scenario where we model a tree and persist expanded state of tree

        final String type = "TreeNode";
        final Map<Scope, Map<String, Map<String, StorageService.Entry>>> initialState = new HashMap<>();
        final Map<String, StorageService.Entry> typeMap = initialState
                .computeIfAbsent(ArezPersist.getRootScope(), s -> new HashMap<>())
                .computeIfAbsent(type, t -> new HashMap<>());
        expandTreeNode(typeMap, "0");
        expandTreeNode(typeMap, "0-0");
        expandTreeNode(typeMap, "0-1");
        expandTreeNode(typeMap, "0-1-1");
        expandTreeNode(typeMap, "0-1-2");
        expandTreeNode(typeMap, "0-1-2-0");

        final var storageService = new TestStorageService(initialState);
        ArezPersist.registerStore(StoreTypes.LOCAL, storageService);

        Arez.context().safeAction(() -> {
            final var service = TreeNodeService.create();

            final TreeNode n0 = service.getRoot();
            assertNotNull(n0);
            assert null != n0;
            final TreeNode n00 = n0.getChildren().get(0);
            final TreeNode n01 = n0.getChildren().get(1);
            final TreeNode n02 = n0.getChildren().get(2);
            final TreeNode n010 = n01.getChildren().get(0);
            final TreeNode n011 = n01.getChildren().get(1);
            final TreeNode n012 = n01.getChildren().get(2);
            final TreeNode n0120 = n012.getChildren().get(0);
            final TreeNode n0121 = n012.getChildren().get(1);
            final TreeNode n0122 = n012.getChildren().get(2);

            assertTrue(n0.isExpanded());
            assertTrue(n00.isExpanded());
            assertTrue(n01.isExpanded());
            assertFalse(n02.isExpanded());
            assertFalse(n010.isExpanded());
            assertTrue(n011.isExpanded());
            assertTrue(n012.isExpanded());
            assertTrue(n0120.isExpanded());
            assertFalse(n0121.isExpanded());
            assertFalse(n0122.isExpanded());

            n0122.toggleExpanded();

            assertTrue(n0122.isExpanded());
        });

        // At the end of the action the transactions resolve and thus a commit
        // should have occurred to storage service

        final var persistedTypes = storageService.getState().get(ArezPersist.getRootScope());
        assertNotNull(persistedTypes);
        assert null != persistedTypes;
        final var persistedEntries = persistedTypes.get(type);
        assertNotNull(persistedEntries);
        assert null != persistedEntries;
        final StorageService.Entry entry = persistedEntries.get("0-1-2-2");
        assertNotNull(entry);
        assert null != entry;
        final Map<String, @Nullable Object> data = entry.getData();
        assertNotNull(data);
        assert null != data;
        assertEquals(data.get("expanded"), true);
    }

    @Test
    public void nullableValuesPersistAndRestore() {
        final Scope scope = ArezPersist.getRootScope();
        final Map<String, @Nullable Object> encoded = new HashMap<>();
        encoded.put("value", "restored");
        final Map<Scope, Map<String, Map<String, StorageService.Entry>>> initialState = new HashMap<>();
        final Map<String, StorageService.Entry> entries = initialState
                .computeIfAbsent(scope, key -> new HashMap<>())
                .computeIfAbsent("NullableValue", key -> new HashMap<>());
        entries.put("present", new StorageService.Entry(null, encoded));
        entries.put("absent", new StorageService.Entry(null, new HashMap<String, @Nullable Object>()));
        final var service = new TestStorageService(initialState);
        ArezPersist.registerStore(StoreTypes.LOCAL, service);

        Arez.context().safeAction(() -> {
            final var absent = NullableValue.create("absent");
            assertNull(absent.getValue());
            final var present = NullableValue.create("present");
            assertEquals(present.getValue(), "restored");
            present.setValue(null);
            assertNull(present.getValue());
        });
        final var persistedTypes = service.getState().get(scope);
        assertNotNull(persistedTypes);
        assert null != persistedTypes;
        final var persistedValues = persistedTypes.get("NullableValue");
        assertNotNull(persistedValues);
        assert null != persistedValues;
        assertFalse(persistedValues.containsKey("present"));

        Arez.context().safeAction(() -> {
            final var value = NullableValue.create("changed");
            value.setValue("saved");
        });
        final var savedTypes = service.getState().get(scope);
        assertNotNull(savedTypes);
        assert null != savedTypes;
        final var savedValues = savedTypes.get("NullableValue");
        assertNotNull(savedValues);
        assert null != savedValues;
        final StorageService.Entry saved = savedValues.get("changed");
        assertNotNull(saved);
        assert null != saved;
        final Map<String, @Nullable Object> data = saved.getData();
        assertNotNull(data);
        assert null != data;
        assertEquals(data.get("value"), "saved");
    }

    private void expandTreeNode(@NonNull final Map<String, StorageService.Entry> typeMap, @NonNull final String id) {
        final Map<String, @Nullable Object> encoded = new HashMap<>();
        encoded.put("expanded", true);
        typeMap.put(id, new StorageService.Entry(null, encoded));
    }
}
