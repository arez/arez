package org.realityforge.arez.examples;

import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observer;

public final class CodeModelExample
{
  public static void main( final String[] args )
    throws Throwable
  {
    final ArezContext context = Arez.context();

    ExampleUtil.logAllErrors( context );
    context.getSpy().addSpyEventHandler( ExamplesSpyUtil::emitEvent );

    final CodeModel codeModel = CodeModel.create( "com.example", "MyType" );

    final Observer observer =
      context.autorun( "Printer",
                       () -> System.out.println( "Qualified Name: " + codeModel.getQualifiedName() ) );

    context.procedure( "Specific Qualified Name", true, () -> codeModel.setQualifiedName( "com.biz.Fred" ) );
    context.procedure( "Reset Qualified Name to default", true, () -> codeModel.setQualifiedName( null ) );
    context.procedure( "Change Local Name", true, () -> codeModel.setName( "MyType2" ) );

    observer.dispose();
    context.procedure( "Dispose Model", true, ( (Disposable) codeModel )::dispose );
  }
}
