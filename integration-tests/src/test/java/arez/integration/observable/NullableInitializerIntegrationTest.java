package arez.integration.observable;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class NullableInitializerIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        safeAction(() -> assertNull(Model.create(null).getName()));
        safeAction(() -> assertEquals(Model.create("XXX").getName(), "XXX"));
    }

    @NullMarked
    @SuppressWarnings("SameParameterValue")
    @ArezComponent
    abstract static class Model {

        static Model create(@Nullable final String name) {
            return new NullableInitializerIntegrationTest_Arez_Model(name);
        }

        @Observable(initializer = Feature.ENABLE)
        @Nullable
        abstract String getName();

        abstract void setName(@Nullable String name);
    }
}
