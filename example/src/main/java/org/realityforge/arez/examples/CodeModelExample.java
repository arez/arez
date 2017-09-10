package org.realityforge.arez.examples;

import org.realityforge.arez.ArezContext;

public final class CodeModelExample
{
  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = new ArezContext();

    ExampleUtil.logAllErrors( context );

    final CodeModel codeModel = new Arez_CodeModel( context, "com.example", "MyType" );

    context.autorun( "Printer",
                     false,
                     () -> System.out.println( "Qualified Name: " + codeModel.getQualifiedName() ),
                     true );

    context.procedure( "Specific Qualified Name", true, () -> codeModel.setQualifiedName( "com.biz.Fred" ) );
    context.procedure( "Reset Qualified Name to default", true, () -> codeModel.setQualifiedName( null ) );
    context.procedure( "Change Local Name", true, () -> codeModel.setName( "MyType2" ) );
  }
}
