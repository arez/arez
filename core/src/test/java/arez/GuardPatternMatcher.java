package arez;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

@SuppressWarnings( "NonJREEmulationClassesInClientCode" )
final class GuardPatternMatcher
  implements Guards.OnGuardListener
{
  @Override
  public void onGuard( @Nonnull final Guards.Type type,
                       @Nonnull final String message,
                       @Nonnull final StackTraceElement[] stackTrace )
  {
    final Matcher matcher = Pattern.compile( "^Arez-(\\d\\d\\d\\d): (.*)$" ).matcher( message );
    if ( matcher.matches() )
    {
      final int code = Integer.parseInt( matcher.group( 1 ) );
      final String msg = matcher.group( 2 );

      DiagnosticMessages.matchOrRecordDiagnosticMessage( code, type, msg );
    }
  }
}
