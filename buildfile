require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

desc 'Arez-SpyTools: Arez utilities that enhance the spy capabilities'
define 'arez-spytools' do
  project.group = 'org.realityforge.arez.spytools'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all,-processing,-serial'
  project.compile.options.warnings = true
  project.compile.options.other = %w(-Werror -Xmaxerrs 10000 -Xmaxwarns 10000)

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('arez/arez-spytools')
  pom.add_developer('realityforge', 'Peter Donald')

  jetbrains_annotations_artifact = artifact(:jetbrains_annotations)
  core_artifact = artifact(:arez_core)
  dom_artifact = artifact(:elemental2_dom)
  pom.include_transitive_dependencies << jetbrains_annotations_artifact
  pom.include_transitive_dependencies << core_artifact
  pom.include_transitive_dependencies << dom_artifact
  pom.dependency_filter = Proc.new {|dep| core_artifact == dep[:artifact] || dom_artifact == dep[:artifact] || jetbrains_annotations_artifact == dep[:artifact]}

  compile.with :javax_annotation,
               :braincheck,
               :grim_annotations,
               :jetbrains_annotations,
               :jsinterop_base,
               :jsinterop_annotations,
               :elemental2_core,
               :elemental2_dom,
               :elemental2_promise,
               :arez_core

  compile.options[:processor_path] << [:arez_processor]

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
          :windowtitle => 'Arez SpyTools API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/)
    )

  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Darez.environment=development')

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all,-processing,-serial -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end
