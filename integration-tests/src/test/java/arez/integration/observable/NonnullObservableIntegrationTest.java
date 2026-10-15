package arez.integration.observable;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class NonnullObservableIntegrationTest extends AbstractArezIntegrationTest {
    // Deliberately violate the nonnull contract to test the generated runtime assertion.
    @SuppressWarnings({"ConstantConditions", "NullAway", "ParameterMissingNullable"})
    @Test
    public void disallowNullInSetter() {
        final var model = Model.create(ValueUtil.randomString());
        safeAction(() -> assertThrows(AssertionError.class, () -> model.setLastName(null)));
    }

    @NullMarked
    @ArezComponent
    abstract static class Model {

        static Model create(final String lastName) {
            return new NonnullObservableIntegrationTest_Arez_Model(lastName);
        }

        @Observable
        abstract String getLastName();

        abstract void setLastName(String lastName);
    }
}
