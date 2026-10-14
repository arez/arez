package arez.integration.cascade_dispose;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class BasicCascadeDisposeIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final Model2 model2 = Model2.create();
        final Model1 model1 = Model1.create(model2);

        assertFalse(Disposable.isDisposed(model1));
        assertFalse(Disposable.isDisposed(model2));

        Disposable.dispose(model1);

        assertTrue(Disposable.isDisposed(model1));
        assertTrue(Disposable.isDisposed(model2));
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {
        @CascadeDispose
        final Model2 _model2;

        static Model1 create(final Model2 name) {
            return new BasicCascadeDisposeIntegrationTest_Arez_Model1(name);
        }

        Model1(final Model2 model2) {
            _model2 = model2;
        }
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model2 {

        static Model2 create() {
            return new BasicCascadeDisposeIntegrationTest_Arez_Model2();
        }
    }
}
