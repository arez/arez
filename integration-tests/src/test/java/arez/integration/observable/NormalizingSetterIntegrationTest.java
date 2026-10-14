package arez.integration.observable;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class NormalizingSetterIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void disallowNullInSetter() {
        final Model model = Model.create(ValueUtil.randomString());

        final AtomicInteger callCount = new AtomicInteger();
        observer(() -> {
            callCount.incrementAndGet();
            //noinspection ResultOfMethodCallIgnored
            model.getName();
        });

        assertEquals(callCount.get(), 1);

        // No Change
        safeAction(() -> model.setName(model.getName()));

        assertEquals(callCount.get(), 1);

        safeAction(() -> model.setName("Fred"));

        assertEquals(callCount.get(), 2);
        safeAction(() -> assertEquals(model.getName(), "Fred"));

        // Normalize by stripping whitespace at end .. thus no change
        safeAction(() -> model.setName("Fred   "));

        assertEquals(callCount.get(), 2);
        safeAction(() -> assertEquals(model.getName(), "Fred"));
    }

    @NullMarked
    @ArezComponent
    abstract static class Model {

        private String _name;

        static Model create(final String name) {
            return new NormalizingSetterIntegrationTest_Arez_Model(name);
        }

        Model(final String name) {
            _name = name;
        }

        @Observable(setterAlwaysMutates = false)
        String getName() {
            return _name;
        }

        void setName(final String name) {
            _name = name.trim();
        }
    }
}
