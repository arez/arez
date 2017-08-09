require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'

PROVIDED_DEPS = [:javax_jsr305, :jetbrains_annotations]
COMPILE_DEPS = []
OPTIONAL_DEPS = []
TEST_DEPS = [:guiceyloops]

desc 'Arez: Simple, Scalable State Management Library'
define 'arez' do
  project.group = 'org.realityforge.arez'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/arez')
  pom.add_developer('realityforge', 'Peter Donald')

  define 'core' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with PROVIDED_DEPS,
                 COMPILE_DEPS

    test.options[:properties] = {'arez.dynamic_provider' => 'true'}

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  ipr.add_default_testng_configuration(:jvm_args => '-Darez.dynamic_provider=true')
  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.extra_modules << '../mobx/mobx.iml'
end
