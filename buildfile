require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

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
  testng_artifact = artifact(:testng)
  pom.include_transitive_dependencies << core_artifact
  pom.include_transitive_dependencies << testng_artifact
  pom.dependency_filter = Proc.new {|dep| core_artifact == dep[:artifact] || core_artifact == dep[:testng_artifact]}

  compile.with :javax_annotation,
               :testng,
               :braincheck,
               :arez_core

  package(:jar)
  package(:sources)
  package(:javadoc)

  doc.
    using(:javadoc,
          :windowtitle => 'Arez TestNG API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api https://arez.github.io/api)
    )

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all,-processing,-serial -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end
