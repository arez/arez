package arez.doc.examples.at_observe;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observe;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class CurrencyView {
    // DOC ELIDE START
    @NullMarked
    @JsType(isNative = true, name = "Element", namespace = JsPackage.GLOBAL)
    private static class Element {
        @JsProperty
        native void setInnerHTML(@Nullable String html);
    }

    @NullMarked
    @JsType(isNative = true, name = "Document", namespace = JsPackage.GLOBAL)
    private static class Document {
        @JsMethod
        native @Nullable Element getElementById(@Nullable String id);
    }
    // DOC ELIDE END

    @CascadeDispose
    final Currency bitcoin = new Arez_Currency();

    @Observe
    void renderView() {
        final Element element = document().getElementById("currencyTracker");
        assert null != element;
        element.setInnerHTML("1 BTC = $" + bitcoin.getAmount() + "AUD");
    }

    // DOC ELIDE START
    @JsProperty(name = "document", namespace = JsPackage.GLOBAL)
    private static native Document document();
    // DOC ELIDE END
}
