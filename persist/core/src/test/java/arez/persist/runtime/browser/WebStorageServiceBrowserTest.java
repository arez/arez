package arez.persist.runtime.browser;

import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.Nullable;

public final class WebStorageServiceBrowserTest {
    private WebStorageServiceBrowserTest() {}

    @JsMethod
    public static boolean roundTrip() {
        final var service = WebStorageService.createLocalStorageService("arez-persist-browser-test");
        try {
            final var converter = new TypeConverter(Map.of());
            final var attributes = new HashMap<String, @Nullable Object>();
            attributes.put("name", "Sample");
            attributes.put("optional", null);
            final var scope = ArezPersist.findOrCreateScope("browser.roundtrip");
            final var encoded = service.encodeState(attributes, converter);
            service.commit(Map.of(scope, Map.of("Widget", Map.of("42", new StorageService.Entry(null, encoded)))));
            final var restored = new HashMap<Scope, Map<String, Map<String, StorageService.Entry>>>();
            service.restore(restored);
            final Map<String, Map<String, StorageService.Entry>> types = Objects.requireNonNull(restored.get(scope));
            final Map<String, StorageService.Entry> entries = Objects.requireNonNull(types.get("Widget"));
            final StorageService.Entry entry = Objects.requireNonNull(entries.get("42"));
            return 1 == restored.size()
                    && 1 == types.size()
                    && 1 == entries.size()
                    && attributes.equals(service.decodeState(entry.getEncoded(), converter));
        } finally {
            service.commit(Map.of());
            service.dispose();
        }
    }

    @JsMethod
    public static int decodedPropertyCount(final Object encoded) {
        final var service = WebStorageService.createLocalStorageService("arez-persist-browser-test");
        try {
            return service.decodeState(encoded, new TypeConverter(Map.of())).size();
        } finally {
            service.dispose();
        }
    }
}
