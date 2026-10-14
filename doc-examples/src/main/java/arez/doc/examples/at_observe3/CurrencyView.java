package arez.doc.examples.at_observe3;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class CurrencyView {
    // A read-only observer that renders
    @Observe(executor = Executor.EXTERNAL)
    public @Nullable ReactNode render() {
        // Render component here
        // DOC ELIDE START
        return null;
        // DOC ELIDE END
    }

    void onRenderDepsChange() {
        // Schedule this component
        scheduleRender();
    }

    // DOC ELIDE START
    @NullMarked
    static class ReactNode {}

    private void scheduleRender() {}
    // DOC ELIDE END
}
