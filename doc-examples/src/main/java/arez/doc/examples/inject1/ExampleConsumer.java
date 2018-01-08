package arez.doc.examples.inject1;

import javax.inject.Inject;

public class ExampleConsumer
{
  @Inject
  MyService _myService;
  @Inject
  MyEntityRepository _repository;

  public void performSomeAction()
  {
    _repository.findAll().forEach( e -> {
      _myService.performAction( e.getValue() );
    } );
  }
}
