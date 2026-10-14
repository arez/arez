package arez.integration.dispose;

import static org.testng.Assert.*;

import arez.ArezTestUtil;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class DisposedNotEqualIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        /*
         * To get equal ids we have to disable a few features and then set requireEquals=ENABLED on component.
         * In the real world this matters as it is more often the case that the entity disappears and then reappears
         * and non-disposed variant should not match disposed variant.
         *
         * This happens in replicant where the same graph is unloaded and then reloaded (i.e. navigate away then back).
         */
        ArezTestUtil.disableNativeComponents();
        ArezTestUtil.disableRegistries();

        final String id = ValueUtil.randomString();
        final var model1 = Model1.create(id);
        final var model2 = Model1.create(id);
        final var model3 = Model1.create(id);

        Disposable.dispose(model3);

        assertEquals(model1, model2);
        assertNotEquals(model1, model3);
        assertEquals(model2, model1);
        assertNotEquals(model2, model3);
        assertNotEquals(model3, model1);
        assertNotEquals(model3, model2);

        Disposable.dispose(model2);

        assertNotEquals(model1, model2);
        assertNotEquals(model1, model3);
        assertNotEquals(model2, model1);
        assertEquals(model2, model3);
        assertNotEquals(model3, model1);
        assertEquals(model3, model2);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true, requireEquals = Feature.ENABLE)
    abstract static class Model1 {

        static Model1 create(final String id) {
            return new DisposedNotEqualIntegrationTest_Arez_Model1(id);
        }

        private final String _id;

        Model1(final String id) {
            _id = id;
        }

        @ComponentId
        String getId() {
            return _id;
        }
    }
}
