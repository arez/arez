require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

desc 'Arez-TestNG: Arez utilities for writing TestNG tests'
define 'arez-testng' do
  project.group = 'org.realityforge.arez.testng'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('arez/arez-testng')
  pom.add_developer('realityforge', 'Peter Donald')

  core_artifact = artifact(:arez_core)
  pom.include_transitive_dependencies << core_artifact
  pom.dependency_filter = Proc.new {|dep| core_artifact == dep[:artifact]}

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

  doc.
    using(:javadoc,
          :windowtitle => 'Arez TestNG API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api)
    )

  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Darez.environment=development')
  ipr.add_component_from_artifact(:idea_codestyle)
end
