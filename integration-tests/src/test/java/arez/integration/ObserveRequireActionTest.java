package arez.integration;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class ObserveRequireActionTest extends AbstractArezIntegrationTest {
    @Test
    public void accessingObservableOutsideTransactionShouldThrowException() {
        final var component = CodeModel.create(ValueUtil.randomString(), ValueUtil.randomString());
        assertThrows(component::getName);

        safeAction(component::getName);
    }

    @Test
    public void mutatingObservableOutsideTransactionShouldThrowException() {
        final var component = CodeModel.create(ValueUtil.randomString(), ValueUtil.randomString());
        assertThrows(() -> component.setName("X"));

        safeAction(() -> component.setName("X"));
    }

    @Test
    public void accessingMemoizedOutsideTransactionShouldThrowException() {
        final var component = CodeModel.create(ValueUtil.randomString(), ValueUtil.randomString());
        assertThrows(component::getQualifiedName);

        safeAction(component::getQualifiedName);
    }

    @NullMarked
    @ArezComponent
    abstract static class CodeModel {

        private String _name;

        private String _packageName;

        @Nullable
        private String _qualifiedName;

        static CodeModel create(final String packageName, final String name) {
            return new ObserveRequireActionTest_Arez_CodeModel(packageName, name);
        }

        CodeModel(final String packageName, final String name) {
            _packageName = packageName;
            _name = name;
        }

        @Observable
        String getName() {
            return _name;
        }

        void setName(final String name) {
            _name = name;
        }

        @Observable
        String getPackageName() {
            return _packageName;
        }

        void setPackageName(final String packageName) {
            _packageName = packageName;
        }

        @Memoize
        String getQualifiedName() {
            final String rawQualifiedName = getRawQualifiedName();
            if (null == rawQualifiedName) {
                return getPackageName() + "." + getName();
            } else {
                return rawQualifiedName;
            }
        }

        @Nullable
        @Observable
        String getRawQualifiedName() {
            return _qualifiedName;
        }

        @Observable(name = "rawQualifiedName")
        void setQualifiedName(@Nullable final String qualifiedName) {
            _qualifiedName = qualifiedName;
        }
    }
}
