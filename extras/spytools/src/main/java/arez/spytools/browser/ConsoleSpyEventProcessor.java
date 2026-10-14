package arez.spytools.browser;

import arez.Arez;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionSkippedEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComponentCreateCompleteEvent;
import arez.spy.ComponentCreateStartEvent;
import arez.spy.ComponentDisposeCompleteEvent;
import arez.spy.ComponentDisposeStartEvent;
import arez.spy.ComputableValueActivateEvent;
import arez.spy.ComputableValueCreateEvent;
import arez.spy.ComputableValueDeactivateEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObservableValueCreateEvent;
import arez.spy.ObservableValueDisposeEvent;
import arez.spy.ObserveCompleteEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.ObserveStartEvent;
import arez.spy.ObserverCreateEvent;
import arez.spy.ObserverDisposeEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.ReactionCycleCompleteEvent;
import arez.spy.ReactionCycleStartEvent;
import arez.spy.TaskCompleteEvent;
import arez.spy.TaskStartEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import arez.spytools.AbstractSpyEventProcessor;
import arez.spytools.SpyUtil;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import org.jspecify.annotations.Nullable;

/**
 * A SpyEventHandler that prints spy events to the browser console.
 * The events are grouped according to their nesting levels and are colored to make them easy
 * to digest. This class is designed to be easy to sub-class.
 */
public class ConsoleSpyEventProcessor extends AbstractSpyEventProcessor {
    @JsFunction
    private interface StringifyReplacerCallback {
        @Nullable
        Object replace(String key, @Nullable Object value);
    }

    @JsType(isNative = true, name = "JSON", namespace = JsPackage.GLOBAL)
    private static final class NativeJSON {
        @JsMethod
        private static native String stringify(@Nullable Object value, StringifyReplacerCallback replacer);
    }

    @JsType(isNative = true, name = "globalThis.console", namespace = JsPackage.GLOBAL)
    private static final class NativeConsole {
        @JsMethod
        private static native void groupCollapsed(Object message, Object styling);

        @JsMethod
        private static native void groupEnd();

        @JsMethod
        private static native void log(Object message);

        @JsMethod
        private static native void log(Object message, Object styling);

        @JsMethod
        private static native void log(Object message, Object styling, @Nullable Object value);
    }

    /*
     * The SVG icons for REACTION_SCHEDULED_COLOR and COMPUTED_COLOR were adjusted variants from the mobx dev tools at https://github.com/andykog/mobx-devtools/blob/master/src/frontend/TabChanges/icons.jsx
     * They were then converted to base64 via http://base64online.org/encode/
     * and converted to excessive css you see below
     */
    @CssRules
    private static final String COMPONENT_COLOR = "color: #410B13; font-weight: normal;";

    @CssRules
    private static final String OBSERVABLE_COLOR = "color: #CF8A3B; font-weight: normal;";

    @CssRules
    private static final String COMPUTED_COLOR =
            "color: #FFBA49; font-weight: normal; background-repeat: no-repeat; background-size: contain;"
                + " padding-left: 20px; background-image:"
                + " url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNSIgaGVpZ2h0PSIxNSIgdmlld0JveD0iMCAwIDE1IDE1Ij4KICA8ZyBmaWxsPSIjN0I1NkEzIj4KICAgIDxjaXJjbGUgY3g9IjMuNzUiIGN5PSIxMS44MyIgcj0iMiIvPgogICAgPGNpcmNsZSBjeD0iMy43NSIgY3k9IjMuMTciIHI9IjIiLz4KICAgIDxjaXJjbGUgY3g9IjExLjI1IiBjeT0iNy41IiByPSIyIi8+CiAgPC9nPgogIDxnIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzdCNTZBMyIgc3Ryb2tlTWl0ZXJsaW1pdD0iMTAiPgogICAgPHBhdGggZD0iTTYuMjUgNy41bC0yLjUgNC4zMyIvPgogICAgPHBhdGggZD0iTTYuMjUgNy41bC0yLjUtNC4zMyIvPgogICAgPHBhdGggZD0iTTYuMjUgNy41aDUiLz4KICA8L2c+Cjwvc3ZnPgo=);";

    @CssRules
    private static final String OBSERVER_COLOR = "color: #0FA13B; font-weight: normal;";

    @CssRules
    private static final String REACTION_COLOR = "color: #10a210; font-weight: normal;";

    @CssRules
    private static final String REACTION_SCHEDULED_COLOR =
            "color: #10a210; font-weight: normal; background-repeat: no-repeat; background-size: contain;"
                + " padding-left: 20px; background-image:"
                + " url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNSIgaGVpZ2h0PSIxNSIgdmlld0JveD0iMCAwIDE1IDE1Ij48ZyBmaWxsPSJub25lIiBzdHJva2U9IiMxMGEyMTAiIHN0cm9rZU1pdGVybGltaXQ9IjEwIj48cGF0aCBkPSJNMTIuNjk3IDEwLjVhNiA2IDAgMSAxIC4xMTUtNS43OTIiLz48cGF0aCBkPSJNNy41IDcuNVYzTTcuNSA3LjVMMTAgMTAiLz48L2c+PGcgZmlsbD0iIzAwMDAwMCI+PHBhdGggZD0iTTEwLjYxOCA0Ljc0M0wxMy41IDcuNWwuOTQ3LTMuODc0eiIvPjxjaXJjbGUgY3g9IjcuNSIgY3k9IjcuNSIgcj0iLjc1Ii8+PC9nPjwvc3ZnPgo=);";

    @CssRules
    private static final String TASK_COLOR = "color: #c143eb; font-weight: normal;";

    @CssRules
    private static final String ACTION_COLOR = "color: #006AEB; font-weight: normal;";

    @CssRules
    private static final String TRANSACTION_COLOR = "color: #A18008; font-weight: normal;";

    @CssRules
    private static final String ERROR_COLOR = "color: #A10001; font-weight: normal;";

    /**
     * Create the processor.
     */
    public ConsoleSpyEventProcessor() {
        on(ComponentCreateStartEvent.class, this::onComponentCreateStart);
        on(ComponentCreateCompleteEvent.class, this::onComponentCreateCompleteEvent);
        on(ComponentDisposeStartEvent.class, this::onComponentDisposeStart);
        on(ComponentDisposeCompleteEvent.class, this::onComponentDisposeComplete);

        on(ObserverCreateEvent.class, this::onObserverCreate);
        on(ObserverDisposeEvent.class, this::onObserverDispose);
        on(ObserverErrorEvent.class, this::onObserverError);

        on(ObservableValueCreateEvent.class, this::onObservableValueCreate);
        on(ObservableValueDisposeEvent.class, this::onObservableValueDispose);
        on(ObservableValueChangeEvent.class, this::onObservableValueChange);

        on(ComputableValueCreateEvent.class, this::onComputableValueCreate);
        on(ComputableValueActivateEvent.class, this::onComputableValueActivate);
        on(ComputeStartEvent.class, this::onComputeStart);
        on(ComputeCompleteEvent.class, this::onComputeComplete);
        on(ComputableValueDeactivateEvent.class, this::onComputableValueDeactivate);
        on(ComputableValueDisposeEvent.class, this::onComputableValueDispose);

        on(ObserveScheduleEvent.class, this::onObserveSchedule);
        on(ObserveStartEvent.class, this::onObserveStart);
        on(ObserveCompleteEvent.class, this::onObserveComplete);

        on(ReactionCycleStartEvent.class, this::onReactionCycleStart);
        on(ReactionCycleCompleteEvent.class, this::onReactionCycleComplete);

        on(TransactionStartEvent.class, this::TransactionStart);
        on(TransactionCompleteEvent.class, this::onTransactionComplete);

        on(ActionStartEvent.class, this::onActionStart);
        on(ActionCompleteEvent.class, this::onActionComplete);
        on(ActionSkippedEvent.class, this::onActionSkipped);

        on(TaskStartEvent.class, this::onTaskStart);
        on(TaskCompleteEvent.class, this::onTaskComplete);
    }

    /**
     * Handle the TaskStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onTaskStart(final SpyUtil.NestingDelta d, final TaskStartEvent e) {
        log(d, "%cTask Start " + e.getTask().getName(), TASK_COLOR);
    }

    /**
     * Handle the TaskCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onTaskComplete(final SpyUtil.NestingDelta d, final TaskCompleteEvent e) {
        log(d, "%cTask Complete " + e.getTask().getName(), TASK_COLOR);
    }

    /**
     * Handle the ReactionCycleStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onReactionCycleStart(final SpyUtil.NestingDelta d, final ReactionCycleStartEvent e) {
        log(d, "%cReaction Cycle Start", REACTION_COLOR);
    }

    /**
     * Handle the ReactionCycleCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onReactionCycleComplete(final SpyUtil.NestingDelta d, final ReactionCycleCompleteEvent e) {
        log(d, "%cReaction Cycle Complete [" + e.getDuration() + "]", REACTION_COLOR);
    }

    /**
     * Handle the ComponentCreateStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComponentCreateStart(final SpyUtil.NestingDelta d, final ComponentCreateStartEvent e) {
        log(d, "%cComponent Create Start " + e.getComponentInfo().getName(), COMPONENT_COLOR);
    }

    /**
     * Handle the ComponentCreateCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComponentCreateCompleteEvent(final SpyUtil.NestingDelta d, final ComponentCreateCompleteEvent e) {
        log(d, "%cComponent Create Complete " + e.getComponentInfo().getName(), COMPONENT_COLOR);
    }

    /**
     * Handle the ComponentDisposeStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComponentDisposeStart(final SpyUtil.NestingDelta d, final ComponentDisposeStartEvent e) {
        log(d, "%cComponent Dispose Start " + e.getComponentInfo().getName(), COMPONENT_COLOR);
    }

    /**
     * Handle the ComponentDisposeCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComponentDisposeComplete(final SpyUtil.NestingDelta d, final ComponentDisposeCompleteEvent e) {
        log(d, "%cComponent Dispose Complete " + e.getComponentInfo().getName(), COMPONENT_COLOR);
    }

    /**
     * Handle the ObserverCreateEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObserverCreate(final SpyUtil.NestingDelta d, final ObserverCreateEvent e) {
        log(d, "%cObserver Create " + e.getObserver().getName(), OBSERVER_COLOR);
    }

    /**
     * Handle the ObserverDisposeEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObserverDispose(final SpyUtil.NestingDelta d, final ObserverDisposeEvent e) {
        log(d, "%cObserver Dispose " + e.getObserver().getName(), OBSERVER_COLOR);
    }

    /**
     * Handle the ObserverErrorEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObserverError(final SpyUtil.NestingDelta d, final ObserverErrorEvent e) {
        log(
                d,
                "%cObserver Error " + e.getObserver().getName() + " " + e.getError() + " " + e.getThrowable(),
                ERROR_COLOR);
    }

    /**
     * Handle the ObservableCreateEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObservableValueCreate(final SpyUtil.NestingDelta d, final ObservableValueCreateEvent e) {
        log(d, "%cObservable Create " + e.getObservableValue().getName(), OBSERVABLE_COLOR);
    }

    /**
     * Handle the ObservableDisposeEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObservableValueDispose(final SpyUtil.NestingDelta d, final ObservableValueDisposeEvent e) {
        log(d, "%cObservable Dispose " + e.getObservableValue().getName(), OBSERVABLE_COLOR);
    }

    /**
     * Handle the ObservableChangedEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObservableValueChange(final SpyUtil.NestingDelta d, final ObservableValueChangeEvent e) {
        if (Arez.arePropertyIntrospectorsEnabled()) {
            log(
                    SpyUtil.NestingDelta.NONE,
                    "%cObservable Changed " + e.getObservableValue().getName() + " Value: %o ",
                    OBSERVABLE_COLOR,
                    e.getValue());
        } else {
            log(
                    SpyUtil.NestingDelta.NONE,
                    "%cObservable Changed " + e.getObservableValue().getName(),
                    OBSERVABLE_COLOR);
        }
    }

    /**
     * Handle the ComputableValueCreateEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComputableValueCreate(final SpyUtil.NestingDelta d, final ComputableValueCreateEvent e) {
        log(d, "%cComputed Value Create " + e.getComputableValue().getName(), COMPUTED_COLOR);
    }

    /**
     * Handle the ComputableValueActivateEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComputableValueActivate(final SpyUtil.NestingDelta d, final ComputableValueActivateEvent e) {
        log(d, "%cComputed Value Activate " + e.getComputableValue().getName(), COMPUTED_COLOR);
    }

    /**
     * Handle the ComputeStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComputeStart(final SpyUtil.NestingDelta d, final ComputeStartEvent e) {
        log(d, "%cCompute Start " + e.getComputableValue().getName(), COMPUTED_COLOR);
    }

    /**
     * Handle the ComputeCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComputeComplete(final SpyUtil.NestingDelta d, final ComputeCompleteEvent e) {
        log(d, "%cCompute Complete " + e.getComputableValue().getName() + " [" + e.getDuration() + "]", COMPUTED_COLOR);
    }

    /**
     * Handle the ComputableValueDeactivateEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComputableValueDeactivate(final SpyUtil.NestingDelta d, final ComputableValueDeactivateEvent e) {
        log(d, "%cComputed Value Deactivate " + e.getComputableValue().getName(), COMPUTED_COLOR);
    }

    /**
     * Handle the ComputableValueDisposeEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onComputableValueDispose(final SpyUtil.NestingDelta d, final ComputableValueDisposeEvent e) {
        log(d, "%cComputed Value Dispose " + e.getComputableValue().getName(), COMPUTED_COLOR);
    }

    /**
     * Handle the ObserveScheduleEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObserveSchedule(final SpyUtil.NestingDelta d, final ObserveScheduleEvent e) {
        log(d, "%cObserve Scheduled " + e.getObserver().getName(), REACTION_SCHEDULED_COLOR);
    }

    /**
     * Handle the ObserveStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObserveStart(final SpyUtil.NestingDelta d, final ObserveStartEvent e) {
        log(d, "%cObserve Start " + e.getObserver().getName(), REACTION_COLOR);
    }

    /**
     * Handle the ObserveCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onObserveComplete(final SpyUtil.NestingDelta d, final ObserveCompleteEvent e) {
        log(d, "%cObserve Complete " + e.getObserver().getName() + " [" + e.getDuration() + "]", REACTION_COLOR);
    }

    /**
     * Handle the TransactionStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void TransactionStart(final SpyUtil.NestingDelta d, final TransactionStartEvent e) {
        log(
                d,
                "%cTransaction Start " + e.getName()
                        + " Mutation="
                        + e.isMutation()
                        + " Tracker="
                        + (e.getTracker() == null ? null : e.getTracker().getName()),
                TRANSACTION_COLOR);
    }

    /**
     * Handle the TransactionCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onTransactionComplete(final SpyUtil.NestingDelta d, final TransactionCompleteEvent e) {
        log(
                d,
                "%cTransaction Complete " + e.getName()
                        + " Mutation="
                        + e.isMutation()
                        + " Tracker="
                        + (e.getTracker() == null ? null : e.getTracker().getName())
                        + " ["
                        + e.getDuration()
                        + "]",
                TRANSACTION_COLOR);
    }

    /**
     * Handle the ActionStartEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onActionStart(final SpyUtil.NestingDelta d, final ActionStartEvent e) {
        final String message = (e.isTracked() ? "Tracked " : "") + "Action Start " + e.getName()
                + parametersToString(e.getParameters());
        log(d, "%c" + message, ACTION_COLOR);
    }

    /**
     * Handle the ActionCompleteEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onActionComplete(final SpyUtil.NestingDelta d, final ActionCompleteEvent e) {
        final String message = (e.isTracked() ? "Tracked " : "") + "Action Complete "
                + e.getName()
                + parametersToString(e.getParameters())
                + (e.returnsResult() && null == e.getThrowable() ? " = " + e.getResult() : "")
                + (null != e.getThrowable() ? "threw " + e.getThrowable() : "")
                + " Duration ["
                + e.getDuration()
                + "]";
        log(d, "%c" + message, ACTION_COLOR);
    }

    /**
     * Handle the ActionSkippedEvent.
     *
     * @param d the change in nesting level.
     * @param e the event.
     */
    protected void onActionSkipped(final SpyUtil.NestingDelta d, final ActionSkippedEvent e) {
        final String message = (e.isTracked() ? "Tracked " : "") + "Action Skipped " + e.getName()
                + parametersToString(e.getParameters());
        log(d, "%c" + message, ACTION_COLOR);
    }

    /**
     * Convert array of parameters into something consumable by a human.
     * Java objects use toString() while native javascript objects use <code>JSON.stringify()</code>.
     *
     * @param parameters the parameters to convert
     * @return a string representation of the parameters
     */
    protected String parametersToString(final @Nullable Object[] parameters) {
        if (0 == parameters.length) {
            return "()";
        } else {
            final StringifyReplacer filter = getStringifyReplacer();
            final var sb = new StringBuilder();
            sb.append("(");
            boolean requireComma = false;
            for (final Object parameter : parameters) {
                if (requireComma) {
                    sb.append(", ");
                } else {
                    requireComma = true;
                }
                sb.append(NativeJSON.stringify(parameter, (k, value) -> filter.handleValue(Js.asAny(value))));
            }
            sb.append(")");
            return sb.toString();
        }
    }

    /**
     * Create replacer callback.
     * This method has been extracted to allow sub classes to override.
     *
     * @return the stringify replacer callback function.
     */
    protected StringifyReplacer getStringifyReplacer() {
        return new StringifyReplacer();
    }

    /**
     * Log specified message with parameters.
     *
     * @param delta   the nesting delta.
     * @param message the message.
     * @param styling the styling parameter. It is assumed that the message has a %c somewhere in it to identify the start of the styling.
     */
    protected void log(final SpyUtil.NestingDelta delta, final String message, @CssRules final String styling) {
        if (SpyUtil.NestingDelta.INCREASE == delta) {
            NativeConsole.groupCollapsed(message, styling);
        } else if (SpyUtil.NestingDelta.DECREASE == delta) {
            NativeConsole.log(message, styling);
            NativeConsole.groupEnd();
        } else {
            NativeConsole.log(message, styling);
        }
    }

    /**
     * Log specified message with parameters.
     *
     * @param delta   the nesting delta.
     * @param message the message.
     * @param styling the styling parameter. It is assumed that the message has a %c somewhere in it to identify the start of the styling.
     * @param value   an arbitrary value parameter added to console log.
     */
    @SuppressWarnings("SameParameterValue")
    protected void log(
            final SpyUtil.NestingDelta delta,
            final String message,
            @CssRules final String styling,
            @Nullable final Object value) {
        if (SpyUtil.NestingDelta.INCREASE == delta) {
            NativeConsole.groupCollapsed(message, styling);
        } else if (SpyUtil.NestingDelta.DECREASE == delta) {
            NativeConsole.log(message, styling);
            NativeConsole.groupEnd();
        } else {
            NativeConsole.log(message, styling, value);
        }
    }

    @Override
    protected void handleUnhandledEvent(final Object event) {
        NativeConsole.log(event);
    }
}
