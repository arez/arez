package com.example.persist;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.annotations.Priority;
import arez.component.Identifiable;
import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Converter;
import arez.persist.runtime.Scope;
import arez.persist.runtime.Store;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@ArezComponent(disposeNotifier = Feature.DISABLE, requireId = Feature.DISABLE)
@Generated("arez.persist.processor.ArezPersistProcessor")
@NullMarked
abstract class MultiStorePersistModel_PersistSidecar {
  private static int c_nextTaskId;

  @ComponentDependency
  final Scope _scope;

  @ComponentDependency
  final MultiStorePersistModel _peer;

  private final Store _aStore;

  private final Store _appStore;

  private final Store _bStore;

  MultiStorePersistModel_PersistSidecar(
      final Scope scope,
      final MultiStorePersistModel peer,
      final Store aStore,
      final Store appStore,
      final Store bStore) {
    _scope = Objects.requireNonNull(scope);
    _peer = Objects.requireNonNull(peer);
    _aStore = Objects.requireNonNull(aStore);
    _appStore = Objects.requireNonNull(appStore);
    _bStore = Objects.requireNonNull(bStore);
  }

  static MultiStorePersistModel_PersistSidecar attach(
      final Scope scope, final MultiStorePersistModel peer) {
    assert Disposable.isNotDisposed(scope);
    assert Disposable.isNotDisposed(peer);
    final Store aStore = ArezPersist.getStore("a");
    final Store appStore = ArezPersist.getStore("app");
    final Store bStore = ArezPersist.getStore("b");
    return new Arez_MultiStorePersistModel_PersistSidecar(scope, peer, aStore, appStore, bStore);
  }

  private static void maybeAttach(final Scope scope, final MultiStorePersistModel peer) {
    if (Disposable.isNotDisposed(scope) && Disposable.isNotDisposed(peer)) {
      attach(scope, peer);
    }
  }

  static void scheduleAttach(final Scope scope, final MultiStorePersistModel peer) {
    assert Disposable.isNotDisposed(scope);
    assert Disposable.isNotDisposed(peer);
    Arez.context()
        .task(
            Arez.areNamesEnabled()
                ? "MultiStorePersistModel_PersistSidecar.attach." + ++c_nextTaskId
                : null,
            () -> maybeAttach(scope, peer));
  }

  private String getComponentId() {
    return String.valueOf(Objects.<Object>requireNonNull(Identifiable.getArezId(_peer)));
  }

  @Observe(priority = Priority.LOWEST, nestedActionsAllowed = true, depType = DepType.AREZ_OR_NONE)
  void savePersistentProperties() {
    persistState();
  }

  @PostConstruct
  void postConstruct() {
    restoreState();
  }

  @Action(verifyRequired = false)
  void restoreState() {
    final String $ap$_id = getComponentId();
    if (!_aStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state =
          _aStore.get(_scope, Keys.TYPE, $ap$_id, Converters.TYPE_CONVERTER);
      if (null != state) {
        final var $prop$_value2 = (String) state.get(Keys.PROPERTY_value2);
        if (null != $prop$_value2) {
          _peer.setValue2($prop$_value2);
        }
        final var $prop$_value = (Integer) state.get(Keys.PROPERTY_value);
        if (null != $prop$_value) {
          _peer.setValue($prop$_value);
        }
      }
    }
    if (!_appStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state =
          _appStore.get(_scope, Keys.TYPE, $ap$_id, Converters.TYPE_CONVERTER);
      if (null != state) {
        final var $prop$_value4 = (Double) state.get(Keys.PROPERTY_value4);
        if (null != $prop$_value4) {
          _peer.setValue4($prop$_value4);
        }
      }
    }
    if (!_bStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state =
          _bStore.get(_scope, Keys.TYPE, $ap$_id, Converters.TYPE_CONVERTER);
      if (null != state) {
        final var $prop$_value3 = (Double) state.get(Keys.PROPERTY_value3);
        if (null != $prop$_value3) {
          _peer.setValue3($prop$_value3);
        }
      }
    }
  }

  @Action(mutation = false, verifyRequired = false)
  void persistState() {
    if (!_aStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state = new HashMap<>();
      final String $prop$_value2 = _peer.getValue2();
      if (null != $prop$_value2) {
        state.put(Keys.PROPERTY_value2, $prop$_value2);
      }
      final int $prop$_value = _peer.getValue();
      if (0 != $prop$_value) {
        state.put(Keys.PROPERTY_value, $prop$_value);
      }
      _aStore.save(_scope, Keys.TYPE, getComponentId(), state, Converters.TYPE_CONVERTER);
    }
    if (!_appStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state = new HashMap<>();
      final Double $prop$_value4 = _peer.getValue4();
      if (null != $prop$_value4) {
        state.put(Keys.PROPERTY_value4, $prop$_value4);
      }
      _appStore.save(_scope, Keys.TYPE, getComponentId(), state, Converters.TYPE_CONVERTER);
    }
    if (!_bStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state = new HashMap<>();
      final Double $prop$_value3 = _peer.getValue3();
      if (null != $prop$_value3) {
        state.put(Keys.PROPERTY_value3, $prop$_value3);
      }
      _bStore.save(_scope, Keys.TYPE, getComponentId(), state, Converters.TYPE_CONVERTER);
    }
  }

  private static final class Keys {
    private static final String TYPE =
        Arez.areNamesEnabled() ? "MultiStorePersistModel" : MultiStorePersistModel.class.getName();

    private static final String PROPERTY_value2 = Arez.areNamesEnabled() ? "value2" : "a";

    private static final String PROPERTY_value4 = Arez.areNamesEnabled() ? "value4" : "b";

    private static final String PROPERTY_value3 = Arez.areNamesEnabled() ? "value3" : "c";

    private static final String PROPERTY_value = Arez.areNamesEnabled() ? "value" : "d";
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static final class Converters {
    private static final Converter CONVERTER_int = ArezPersist.getConverter(int.class);

    private static final Converter CONVERTER_java__lang__Double =
        ArezPersist.getConverter(Double.class);

    private static final Converter CONVERTER_java__lang__String =
        ArezPersist.getConverter(String.class);

    private static final TypeConverter TYPE_CONVERTER = createTypeConverter();

    private static TypeConverter createTypeConverter() {
      final Map<String, Converter> converters = new HashMap<>();
      converters.put("value2", CONVERTER_java__lang__String);
      converters.put("value4", CONVERTER_java__lang__Double);
      converters.put("value3", CONVERTER_java__lang__Double);
      converters.put("value", CONVERTER_int);
      return new TypeConverter(converters);
    }
  }
}
