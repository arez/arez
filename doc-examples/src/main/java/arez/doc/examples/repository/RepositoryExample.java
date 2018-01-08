package arez.doc.examples.repository;

public class RepositoryExample
{
  public static void main( String[] args )
  {
    //EXAMPLE START
    final MyComponentRepository repository = MyComponentRepository.newRepository();
    final int myComponentId = 22;
    final MyComponent component = repository.create( myComponentId, "MyName" );

    assert repository.findById( -1 ) == null;
    assert repository.findById( component.getId() ) == component;
    assert repository.getById( component.getId() ) == component;
    assert repository.findAll().size() == 1;
    assert repository.findByQuery( c -> c.getName().equals( "MyName" ) ) == component;
    assert repository.findByQuery( c -> c.getName().equals( "NotMyName" ) ) == null;
    //EXAMPLE END
  }
}
