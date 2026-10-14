package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class DisposeIntegrationTest extends AbstractArezIntegrationTest {
    /**
     * Another example that incorporates actions, computable and disposes.
     */
    @Test
    public void codeModelScenario() throws Throwable {
        final ArezContext context = Arez.context();

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording(context);

        final var codeModel = CodeModel.create("com.example", "MyType");

        final Observer observer = context.observer("Printer", () -> {
            observeADependency();
            recorder.mark("qualifiedName", codeModel.getQualifiedName());
        });

        context.action("Specific Qualified Name", () -> codeModel.setQualifiedName("com.biz.Fred"));
        context.action("Reset Qualified Name to default", () -> codeModel.setQualifiedName(null));
        context.action("Change Local Name", () -> codeModel.setName("MyType2"));

        observer.dispose();
        Disposable.dispose(codeModel);

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @SuppressWarnings("WeakerAccess")
    @ArezComponent
    abstract static class CodeModel {

        private String _name;

        private String _packageName;

        @Nullable
        private String _qualifiedName;

        static CodeModel create(final String packageName, final String name) {
            return new DisposeIntegrationTest_Arez_CodeModel(packageName, name);
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
