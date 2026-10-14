package arez.integration.dispose_notifier;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class DisposeNotifierIgnoresDisposedListenersTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final String name = ValueUtil.randomString();
        final var model1 = Model1.create(name);
        final var model2 = Model2.create(model1);

        Disposable.dispose(model2);

        assertTrue(Disposable.isDisposed(model1));
        assertTrue(Disposable.isDisposed(model2));
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {

        static Model1 create(final String name) {
            return new DisposeNotifierIgnoresDisposedListenersTest_Arez_Model1(name);
        }

        @Observable
        abstract String getName();

        abstract void setName(String name);
    }

    @NullMarked
    @ArezComponent
    abstract static class Model2 {
        @CascadeDispose
        @Nullable
        Model1 _other;

        static Model2 create(@Nullable final Model1 other) {
            return new DisposeNotifierIgnoresDisposedListenersTest_Arez_Model2(other);
        }

        Model2(@Nullable final Model1 other) {
            _other = other;
        }

        @PreDispose
        void preDispose() {
            final Model1 other = _other;
            if (null != other) {
                Disposable.dispose(other);
            }
        }

        @Action
        void clearOther() {
            Disposable.dispose(getOther());
        }

        @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
        @Observable
        @Nullable
        Model1 getOther() {
            return _other;
        }

        void setOther(final @Nullable Model1 other) {
            _other = other;
        }
    }
}
