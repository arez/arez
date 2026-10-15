package arez.integration.basic;

import static org.testng.Assert.assertNotNull;

import arez.ObservableValue;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.persist.Persist;
import arez.persist.PersistType;
import arez.persist.StoreTypes;
import arez.persist.runtime.ArezPersist;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@PersistType
@ArezComponent(
        requireId = Feature.ENABLE,
        requireEquals = Feature.ENABLE,
        observable = Feature.ENABLE,
        disposeNotifier = Feature.ENABLE)
public abstract class TreeNode {
    @NonNull
    private final List<TreeNode> _children = new ArrayList<>();

    private final String _id;
    private final int _depth;

    @NonNull
    public static TreeNode create(@NonNull final String id, final int depth) {
        final TreeNode node = new Arez_TreeNode(id, depth);
        TreeNode_PersistSidecar.attach(ArezPersist.getRootScope(), node);
        return node;
    }

    TreeNode(@NonNull final String id, final int depth) {
        assertNotNull(id);
        _id = id;
        _depth = depth;
    }

    public boolean isLeafNode() {
        return getChildren().isEmpty();
    }

    public int getDepth() {
        return _depth;
    }

    @Observable(writeOutsideTransaction = Feature.ENABLE, readOutsideTransaction = Feature.ENABLE)
    public abstract @Nullable String getLabel();

    public abstract void setLabel(@Nullable String label);

    @Persist(store = StoreTypes.LOCAL)
    @Observable(writeOutsideTransaction = Feature.ENABLE, readOutsideTransaction = Feature.ENABLE)
    public abstract boolean isExpanded();

    public abstract void setExpanded(boolean expanded);

    @Action
    public void toggleExpanded() {
        setExpanded(!isExpanded());
    }

    @Observable(expectSetter = false, readOutsideTransaction = Feature.ENABLE)
    @NonNull
    public List<TreeNode> getChildren() {
        return _children;
    }

    @SuppressWarnings("Arez:PublicRefMethod")
    @ObservableValueRef
    public abstract ObservableValue<?> getChildrenObservableValue();

    @ComponentId
    String getId() {
        return _id;
    }
}
