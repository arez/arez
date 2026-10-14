package arez.integration.cascade_dispose;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class ObservableCascadeDisposeIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario1() {
        final var model2 = Model2.create();
        final var model1 = Model1.create(model2);

        assertFalse(Disposable.isDisposed(model1));
        assertFalse(Disposable.isDisposed(model2));

        Disposable.dispose(model1);

        assertTrue(Disposable.isDisposed(model1));
        assertTrue(Disposable.isDisposed(model2));
    }

    @Test
    public void scenario2() {
        final var model2a = Model2.create();
        final var model2b = Model2.create();
        final var model1 = Model1.create(model2a);

        assertFalse(Disposable.isDisposed(model1));
        assertFalse(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        model1.setModel2(model2b);

        Disposable.dispose(model1);

        assertTrue(Disposable.isDisposed(model1));
        assertFalse(Disposable.isDisposed(model2a));
        assertTrue(Disposable.isDisposed(model2b));
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {

        static Model1 create(final Model2 name) {
            return new ObservableCascadeDisposeIntegrationTest_Arez_Model1(name);
        }

        @SuppressWarnings("unused")
        @CascadeDispose
        abstract Model2 getModel2();

        @Observable(writeOutsideTransaction = Feature.ENABLE)
        abstract void setModel2(Model2 model2);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model2 {

        static Model2 create() {
            return new ObservableCascadeDisposeIntegrationTest_Arez_Model2();
        }
    }
}
