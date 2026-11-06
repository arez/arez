package arez.doc.examples.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MyComponentRepository extends AbstractRepository<Integer, MyComponent, MyComponentRepository> {
    public static MyComponentRepository newRepository() {
        return new Arez_MyComponentRepository();
    }

    @Action
    public MyComponent create(final int id, final String name) {
        final MyComponent component = new Arez_MyComponent(id, name);
        attach(component);
        return component;
    }

    @Override
    @Action
    public void destroy(final MyComponent entity) {
        super.destroy(entity);
    }

    @Nullable
    public MyComponent findById(final int id) {
        return findByArezId(id);
    }

    public MyComponent getById(final int id) {
        return getByArezId(id);
    }

    /**
     * Return a list of the loaded components.
     *
     * @return the loaded components.
     */
    public List<MyComponent> findAll() {
        return scanAll();
    }

    /**
     * Return the first loaded component with the given name.
     *
     * @param name the name to match.
     * @return a matching component or null.
     */
    @Nullable
    public MyComponent findByName(final String name) {
        return scanByQuery(component -> component.getName().equals(name));
    }
}
