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
abstract class CustomNamePersistModel_PersistSidecar {
  private static int c_nextTaskId;

  @ComponentDependency
  final Scope _scope;

  @ComponentDependency
  final CustomNamePersistModel _peer;

  private final Store _appStore;

  CustomNamePersistModel_PersistSidecar(
      final Scope scope, final CustomNamePersistModel peer, final Store appStore) {
    _scope = Objects.requireNonNull(scope);
    _peer = Objects.requireNonNull(peer);
    _appStore = Objects.requireNonNull(appStore);
  }

  static CustomNamePersistModel_PersistSidecar attach(
      final Scope scope, final CustomNamePersistModel peer) {
    assert Disposable.isNotDisposed(scope);
    assert Disposable.isNotDisposed(peer);
    final Store appStore = ArezPersist.getStore("app");
    return new Arez_CustomNamePersistModel_PersistSidecar(scope, peer, appStore);
  }

  private static void maybeAttach(final Scope scope, final CustomNamePersistModel peer) {
    if (Disposable.isNotDisposed(scope) && Disposable.isNotDisposed(peer)) {
      attach(scope, peer);
    }
  }

  static void scheduleAttach(final Scope scope, final CustomNamePersistModel peer) {
    assert Disposable.isNotDisposed(scope);
    assert Disposable.isNotDisposed(peer);
    Arez.context()
        .task(
            Arez.areNamesEnabled()
                ? "CustomNamePersistModel_PersistSidecar.attach." + ++c_nextTaskId
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
    if (!_appStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state =
          _appStore.get(_scope, Keys.TYPE, $ap$_id, Converters.TYPE_CONVERTER);
      if (null != state) {
        final var $prop$_v = (Integer) state.get(Keys.PROPERTY_v);
        if (null != $prop$_v) {
          _peer.setV($prop$_v);
        }
      }
    }
  }

  @Action(mutation = false, verifyRequired = false)
  void persistState() {
    if (!_appStore.isDisposed() && Disposable.isNotDisposed(_scope)) {
      final Map<String, @Nullable Object> state = new HashMap<>();
      final int $prop$_v = _peer.getValue();
      if (0 != $prop$_v) {
        state.put(Keys.PROPERTY_v, $prop$_v);
      }
      _appStore.save(_scope, Keys.TYPE, getComponentId(), state, Converters.TYPE_CONVERTER);
    }
  }

  private static final class Keys {
    private static final String TYPE =
        Arez.areNamesEnabled() ? "CustomNamePersistModel" : CustomNamePersistModel.class.getName();

    private static final String PROPERTY_v = Arez.areNamesEnabled() ? "v" : "a";
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static final class Converters {
    private static final Converter CONVERTER_int = ArezPersist.getConverter(int.class);

    private static final TypeConverter TYPE_CONVERTER = createTypeConverter();

    private static TypeConverter createTypeConverter() {
      final Map<String, Converter> converters = new HashMap<>();
      converters.put("v", CONVERTER_int);
      return new TypeConverter(converters);
    }
  }
}
