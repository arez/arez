package arez.spytools.browser.react4j;

import arez.spytools.browser.ConsoleSpyEventProcessor;
import arez.spytools.browser.StringifyReplacer;
import jsinterop.base.Any;

/**
 * A customized console event processor that avoids accessing "key" and "ref" attributes.
 * This causes warnings when accessing props.key and props.ref in react components.
 */
public class ReactArezConsoleSpyEventProcessor extends ConsoleSpyEventProcessor {

    @Override
    protected StringifyReplacer getStringifyReplacer() {
        return new StringifyReplacer() {
            protected boolean includeProperty(final Any value, final String propertyName) {
                return !"key".equals(propertyName) && !"ref".equals(propertyName);
            }
        };
    }
}
