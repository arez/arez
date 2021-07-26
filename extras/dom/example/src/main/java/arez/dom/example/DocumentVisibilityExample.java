package arez.dom.example;

import akasha.Console;
import arez.Arez;
import arez.dom.DocumentVisibility;
import com.google.gwt.core.client.EntryPoint;

public class DocumentVisibilityExample
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final DocumentVisibility v = DocumentVisibility.create();
    Arez.context().observer( () -> Console.log( "Document Visibility: " + v.getVisibility() ) );
  }
}
