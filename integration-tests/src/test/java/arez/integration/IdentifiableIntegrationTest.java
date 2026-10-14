package arez.integration;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.component.Identifiable;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
@SuppressWarnings("ConstantConditions")
public final class IdentifiableIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void requireId_DISABLE() {
        final Model1 model = new IdentifiableIntegrationTest_Arez_Model1();

        assertFalse(model instanceof Identifiable);
    }

    @Test
    public void requireId_ENABLE() {
        final Model2 model = new IdentifiableIntegrationTest_Arez_Model2(33);

        assertTrue(model instanceof Identifiable);
        assertEquals(Identifiable.getArezId(model), (Integer) 33);
        assertEquals(Identifiable.asIdentifiable(model), model);
    }

    @Test
    public void requireId_DEFAULT() {
        final Model3 model = new IdentifiableIntegrationTest_Arez_Model3();

        assertTrue(model instanceof Identifiable);
        assertEquals(Identifiable.getArezId(model), (Integer) 1);
        assertEquals(Identifiable.asIdentifiable(model), model);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true, requireId = Feature.DISABLE)
    abstract static class Model1 {}

    @NullMarked
    @ArezComponent(allowEmpty = true, requireId = Feature.ENABLE)
    abstract static class Model2 {
        private final int id;

        Model2(final int id) {
            this.id = id;
        }

        @ComponentId
        int getId() {
            return id;
        }
    }

    @NullMarked
    @ArezComponent(allowEmpty = true, requireId = Feature.AUTODETECT)
    abstract static class Model3 {}
}
