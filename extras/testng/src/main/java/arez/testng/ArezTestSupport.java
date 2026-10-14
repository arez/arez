package arez.testng;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Disposable;
import arez.Function;
import arez.Observer;
import arez.Procedure;
import arez.SafeFunction;
import arez.SafeProcedure;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public interface ArezTestSupport extends IHookable {
    @BeforeMethod
    default void preTest() throws Exception {
        BrainCheckTestUtil.resetConfig(false);
        ArezTestUtil.resetConfig(false);
    }

    @AfterMethod
    default void postTest() {
        ArezTestUtil.resetConfig(true);
        BrainCheckTestUtil.resetConfig(true);
    }

    @Override
    default void run(final IHookCallBack callBack, final ITestResult testResult) {
        new ArezTestHook().run(callBack, testResult);
    }

    default ArezContext context() {
        return Arez.context();
    }

    default Disposable pauseScheduler() {
        return context().pauseScheduler();
    }

    default void observer(final Procedure procedure) {
        context().observer(procedure, Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
    }

    default void action(final Procedure action) throws Throwable {
        context().action(action);
    }

    default <T extends @Nullable Object> T action(final Function<T> action) throws Throwable {
        return context().action(action);
    }

    default void safeAction(final SafeProcedure action) {
        context().safeAction(action);
    }

    default <T extends @Nullable Object> T safeAction(final SafeFunction<T> action) {
        return context().safeAction(action);
    }
}
