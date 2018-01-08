package arez.doc.examples.inject2;

import javax.inject.Inject;

public class ExampleConsumer
{
  private final MyService _myService;
  private final MyEntityRepository _repository;

  @Inject
  public ExampleConsumer( final MyService myService, final MyEntityRepository repository )
  {
    _myService = myService;
    _repository = repository;
  }

  public void performSomeAction()
  {
    _repository.findAll().forEach( e -> {
      _myService.performAction( e.getValue() );
    } );
  }
}
