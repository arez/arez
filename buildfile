require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

desc 'Arez-When: An Arez component that waits until a condition is true and then runs an effect action'
define 'arez-when' do
  project.group = 'org.realityforge.arez.when'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  core_artifact = artifact(:arez_core)
  pom.include_transitive_dependencies << core_artifact
  pom.dependency_filter = Proc.new {|dep| core_artifact == dep[:artifact]}

  project.processorpath << :arez_processor

  compile.with :javax_annotation,
               :braincheck,
               :arez_core

  gwt_enhance(project)

  package(:jar)
  package(:sources)
  package(:javadoc)

  test.options[:properties] = { 'braincheck.environment' => 'development', 'arez.environment' => 'development' }
  test.options[:java_args] = ['-ea']

  test.using :testng
  test.compile.with [:guiceyloops]

  doc.
    using(:javadoc,
          :windowtitle => 'Arez When API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/)
    )

  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Dbraincheck.environment=development -Darez.environment=development')
  ipr.add_component_from_artifact(:idea_codestyle)
end
