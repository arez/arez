package arez.doc.examples.sting;

import sting.Injectable;

@Injectable
public class ExampleConsumer
{
  private final MyService _myService;

  ExampleConsumer( final MyService myService )
  {
    _myService = myService;
  }

  public void performSomeAction( int value )
  {
    _myService.performAction( value );
  }
}
