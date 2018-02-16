package com.example.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.ComponentState;
import arez.component.Identifiable;
import java.util.stream.Stream;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@Singleton
final class Arez_RepositoryWithExplicitNonStandardIdRepository extends RepositoryWithExplicitNonStandardIdRepository implements Disposable, Identifiable<Long> {
  private static volatile long $$arezi$$_nextId;

  private final long $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final Observable<Stream<RepositoryWithExplicitNonStandardId>> $$arez$$_entities;

  @Inject
  Arez_RepositoryWithExplicitNonStandardIdRepository() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = $$arezi$$_nextId++;
    this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? getContext().createComponent( "RepositoryWithExplicitNonStandardIdRepository", $$arezi$$_id(), getRepositoryName(), () -> super.preDispose(), null ) : null;
    this.$$arezi$$_disposedObservable = getContext().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getRepositoryName() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_entities = getContext().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getRepositoryName() + ".entities" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.entities() : null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    this.$$arezi$$_state = ComponentState.COMPONENT_READY;
  }

  @Override
  @Nonnull
  protected final ArezContext getContext() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'RepositoryWithExplicitNonStandardIdRepository'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final long $$arezi$$_id() {
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Long getArezId() {
    return $$arezi$$_id();
  }

  @Nonnull
  protected final String getRepositoryName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'RepositoryWithExplicitNonStandardIdRepository'" );
    }
    return "RepositoryWithExplicitNonStandardIdRepository";
  }

  @Override
  public boolean isDisposed() {
    if ( getContext().isTransactionActive() && !this.$$arezi$$_disposedObservable.isDisposed() )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        getContext().safeAction( Arez.areNamesEnabled() ? getRepositoryName() + ".dispose" : null, () -> { {
          super.preDispose();
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_entities.dispose();
        } } );
      }
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
    }
  }

  @Nonnull
  @Override
  public Stream<RepositoryWithExplicitNonStandardId> entities() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getRepositoryName() + "'" );
    }
    this.$$arez$$_entities.reportObserved();
    return super.entities();
  }

  @Nonnull
  @Override
  protected Observable getEntitiesObservable() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getRepositoryName() + "'" );
    }
    return $$arez$$_entities;
  }

  @Override
  public void destroy(@Nonnull final RepositoryWithExplicitNonStandardId arg0) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getRepositoryName() + "'" );
    }
    try {
      getContext().safeAction(Arez.areNamesEnabled() ? getRepositoryName() + ".destroy" : null, true, () -> super.destroy(arg0), arg0 );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Nonnull
  @Override
  public RepositoryWithExplicitNonStandardId create(@Nonnull final String packageName,
      @Nonnull final String name) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getRepositoryName() + "'" );
    }
    try {
      return getContext().safeAction(Arez.areNamesEnabled() ? getRepositoryName() + ".create_packageName_name" : null, true, () -> super.create(packageName,name), packageName, name );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arezi$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_RepositoryWithExplicitNonStandardIdRepository) ) {
      return false;
    } else {
      final Arez_RepositoryWithExplicitNonStandardIdRepository that = (Arez_RepositoryWithExplicitNonStandardIdRepository) o;;
      return $$arezi$$_id() == that.$$arezi$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + getRepositoryName() + "]";
    } else {
      return super.toString();
    }
  }
}
