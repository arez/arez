package arez.component;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComponentRef;
import arez.annotations.ContextRef;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

/**
 * Abstract base class for classes that are expected to clear references to components when the
 * component is disposed.
 */
abstract class AbstractEntryContainer<T>
{
  /**
   * Create an EntityReference for specified entity.
   * This class also sets up monitoring observer so that if the entity is disposed then the
   * onDisposeAction is invoked. The action is expected to detach the reference and may potentially
   * dispose the entity and/or remove the entry from a container.
   *
   * @param entity          the entity.
   * @param onDisposeAction the action to take when the entity is disposed.
   * @return the reference.
   */
  @Nonnull
  final EntityReference<T> createEntityReference( @Nonnull final T entity,
                                                  @Nonnull final Consumer<EntityReference<T>> onDisposeAction )
  {
    final EntityReference<T> entry = new EntityReference<>( entity );
    final String name =
      Arez.areNamesEnabled() ? getName() + ".EntityWatcher." + Identifiable.getArezId( entity ) : null;
    final Observer monitor =
      getContext().when( Arez.areNativeComponentsEnabled() ? component() : null,
                         name,
                         true,
                         () -> ComponentObservable.notObserved( entity ),
                         () -> onDisposeAction.accept( entry ),
                         true,
                         true );
    entry.setMonitor( monitor );
    return entry;
  }

  /**
   * Dispose the entry and the associated monitor and if shouldDisposeEntity is true, dispose the entity.
   *
   * @param entry the entry to detach.
   * @param shouldDisposeEntity true to also dispose the entity, false otherwise.
   */
  final void detachEntry( @Nonnull final EntityReference<T> entry, final boolean shouldDisposeEntity )
  {
    if ( shouldDisposeEntity )
    {
      Disposable.dispose( entry );
    }
    else
    {
      final Observer monitor = entry.getMonitor();
      if ( null != monitor )
      {
        Disposable.dispose( monitor );
      }
    }
  }

  /**
   * Return the native component associated with the current component.
   *
   * @return the native component associated with the current component.
   */
  @ComponentRef
  @Nonnull
  protected abstract Component component();

  /**
   * Return the context associated with the current component.
   *
   * @return the context associated with the current component.
   */
  @ContextRef
  @Nonnull
  protected abstract ArezContext getContext();

  /**
   * Return the component name associated with the current component.
   *
   * @return the component name associated with the current component.
   */
  @ComponentNameRef
  @Nonnull
  protected abstract String getName();
}
