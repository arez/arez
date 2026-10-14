package arez.integration.observable;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class UnannotatedObservablesIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final Model model = Model.create();
        final List<String> trace = new ArrayList<>();
        safeAction(() -> model.setValue("A"));
        observer(() -> trace.add(model.getValue()));

        assertEquals(String.join(",", trace), "A");

        safeAction(() -> model.setValue("B"));

        assertEquals(String.join(",", trace), "A,B");

        safeAction(() -> {
            model.setValue("C");
            model.setValue("D");
        });

        assertEquals(String.join(",", trace), "A,B,D");
    }

    @NullMarked
    @ArezComponent
    abstract static class Model {

        static Model create() {
            return new UnannotatedObservablesIntegrationTest_Arez_Model();
        }

        abstract @Nullable String getValue();

        abstract void setValue(@Nullable String lastName);
    }
}
