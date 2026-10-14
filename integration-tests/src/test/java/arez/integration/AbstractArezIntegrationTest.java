package arez.integration;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezTestUtil;
import arez.Observer;
import arez.ObserverError;
import arez.Procedure;
import arez.SafeFunction;
import arez.SafeProcedure;
import arez.integration.util.SpyEventRecorder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@NullMarked
@SuppressWarnings({"SameParameterValue"})
public abstract class AbstractArezIntegrationTest {

    private final List<String> _observerErrors = new ArrayList<>();

    private boolean _captureObserverErrors;

    @Nullable
    private String _currentMethod;

    protected final void captureObserverErrors() {
        _captureObserverErrors = true;
    }

    @BeforeMethod
    public void handleTestMethodName(final Method method) {
        _currentMethod = method.getName();
        ArezTestUtil.resetConfig(false);
        _captureObserverErrors = false;
        _observerErrors.clear();
        Arez.context().addObserverErrorHandler(this::onObserverError);
    }

    private void onObserverError(
            final Observer observer, final ObserverError error, @Nullable final Throwable throwable) {
        final var message = "Observer: " + observer.getName() + " Error: " + error + " " + throwable;
        _observerErrors.add(message);
        if (!_captureObserverErrors) {
            System.out.println(message);
            if (null != throwable) {
                throwable.printStackTrace(System.out);
            }
        }
    }

    @AfterMethod
    protected void afterTest() {
        ArezTestUtil.resetConfig(true);
        if (!_captureObserverErrors && !_observerErrors.isEmpty()) {
            fail("Unexpected Observer Errors: " + String.join("\n", _observerErrors));
        }
    }

    protected final void safeAction(final SafeProcedure action) {
        Arez.context().safeAction(action);
    }

    protected final <T extends @Nullable Object> @Nullable T safeAction(final SafeFunction<T> action) {
        return Arez.context().safeAction(action);
    }

    protected final List<String> getObserverErrors() {
        assert _captureObserverErrors;
        return _observerErrors;
    }

    protected final void assertMatchesFixture(final SpyEventRecorder recorder) throws IOException, JSONException {
        recorder.assertMatchesFixture(fixtureDir().resolve(getFixtureFilename()), outputFiles());
    }

    protected static void observeADependency() {
        Arez.context().observable().reportObserved();
    }

    private String getFixtureFilename() {
        return getClass().getName().replace(".", "/") + "." + _currentMethod + ".json";
    }

    private Path fixtureDir() {
        final String fixtureDir = System.getProperty("arez.integration_fixture_dir");
        assertNotNull(
                fixtureDir,
                "Expected System.getProperty( \"arez.integration_fixture_dir\" ) to return fixture directory if"
                        + " arez.output_fixture_data=true");

        return new File(fixtureDir).toPath();
    }

    private boolean outputFiles() {
        return System.getProperty("arez.output_fixture_data", "false").equals("true");
    }

    protected static void assertInvariant(final ThrowingRunnable throwingRunnable, final String message) {
        assertThrowsWithMessage(IllegalStateException.class, throwingRunnable, message);
    }

    private static <T extends Throwable> void assertThrowsWithMessage(
            final Class<T> exceptionType, final ThrowingRunnable runnable, final String message) {
        assertEquals(expectThrows(exceptionType, runnable).getMessage(), message);
    }

    protected final Observer observer(final Procedure executable) {
        return Arez.context().observer(() -> {
            observeADependency();
            executable.call();
        });
    }
}
