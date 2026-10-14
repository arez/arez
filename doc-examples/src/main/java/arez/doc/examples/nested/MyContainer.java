package arez.doc.examples.nested;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MyContainer {
    @NullMarked
    @ArezComponent
    public abstract static class MyComponent {
        // DOC ELIDE START
        private int _value;

        @Observable
        public int getValue() {
            return _value;
        }

        public void setValue(int value) {
            _value = value;
        }
        // DOC ELIDE END
    }
}
