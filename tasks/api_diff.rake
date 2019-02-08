JAPI_DEPS = %w(
  com.github.siom79.japicmp:japicmp:jar:0.13.0:jar-with-dependencies
  com.google.guava:guava:jar:18.0
  com.sun.istack:istack-commons-runtime:jar:2.16
  com.sun.xml.bind:jaxb-core:jar:2.2.7
  com.sun.xml.bind:jaxb-impl:jar:2.2.7
  com.sun.xml.fastinfoset:FastInfoset:jar:1.2.12
  javax.activation:activation:jar:1.1
  javax.xml.bind:jaxb-api:jar:2.2.7
  org.javassist:javassist:jar:3.23.1-GA
)

def api_diff(from_spec, to_spec, report_dir = 'reports/japicmp')
  from = Buildr.artifact(from_spec)
  to = Buildr.artifact(to_spec)
  japicmp = Buildr.artifact('com.github.siom79.japicmp:japicmp:jar:jar-with-dependencies:0.13.0')


  from.invoke
  to.invoke
  japicmp.invoke

  mkdir_p report_dir
  Java::Commands.java ['-jar', japicmp.to_s, '--old', from.to_s, '--new', to.to_s, '--only-incompatible', '-a', 'protected', '--html-file', "#{report_dir}/index.html", '--report-only-filename']

end

task 'api_diff' do
  api_diff('org.realityforge.arez:arez-core:jar:0.100', 'org.realityforge.arez:arez-core:jar:0.127')
end

