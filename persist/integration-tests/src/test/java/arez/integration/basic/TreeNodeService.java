package arez.integration.basic;

import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.Nullable;

@ArezComponent
public abstract class TreeNodeService {
    public static TreeNodeService create() {
        return new Arez_TreeNodeService();
    }

    @PostConstruct
    void postConstruct() {
        rebuildTree();
    }

    @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
    @Observable
    public abstract @Nullable TreeNode getRoot();

    public abstract void setRoot(@Nullable TreeNode root);

    @Action
    public void rebuildTree() {
        final TreeNode existing = getRoot();
        if (null != existing) {
            cascadeDisposeTree(existing);
        }
        final var root = TreeNode.create("0", 1);
        root.getChildren().clear();
        root.setLabel("Root");
        for (int i = 0; i < 3; i++) {
            final var nodeL1 = TreeNode.create("0-" + i, 2);
            nodeL1.setLabel("N" + i);
            root.getChildren().add(nodeL1);
            for (int j = 0; j < 3; j++) {
                final var nodeL2 = TreeNode.create("0-" + i + "-" + j, 3);
                nodeL2.setLabel("N" + i + "." + j);
                nodeL1.getChildren().add(nodeL2);
                for (int k = 0; k < 3; k++) {
                    final var nodeL3 = TreeNode.create("0-" + i + "-" + j + "-" + k, 4);
                    nodeL3.setLabel("N" + i + "." + j + "." + k);
                    nodeL2.getChildren().add(nodeL3);
                }
            }
        }
        setRoot(root);
    }

    private void cascadeDisposeTree(final TreeNode node) {
        node.getChildren().forEach(this::cascadeDisposeTree);
        Disposable.dispose(node);
    }
}
