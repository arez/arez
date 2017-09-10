require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'

PROVIDED_DEPS = [:javax_jsr305, :jetbrains_annotations]
COMPILE_DEPS = []
OPTIONAL_DEPS = []
TEST_DEPS = [:guiceyloops]

# JDK options passed to test environment. Essentially turns assertions on.
AREZ_TEST_OPTIONS = { 'arez.dynamic_provider' => 'true', 'arez.logger' => 'proxy', 'arez.environment' => 'development' }

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

  define 'annotations' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with PROVIDED_DEPS,
                 COMPILE_DEPS

    package(:jar)
    package(:sources)
    package(:javadoc)
  end

  define 'core' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with PROVIDED_DEPS,
                 COMPILE_DEPS

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  define 'extras' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with project('core').package(:jar),
                 project('core').compile.dependencies

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  define 'processor' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with PROVIDED_DEPS,
                 COMPILE_DEPS,
                 :autoservice,
                 :autocommon,
                 :javapoet,
                 :guava,
                 Java.tools_jar,
                 project('annotations')

    test.with :compile_testing,
              :truth,
              project('core')

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  define 'example' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with project('annotations').package(:jar),
                 project('annotations').compile.dependencies,
                 project('core').package(:jar),
                 project('core').compile.dependencies,
                 project('extras').package(:jar),
                 project('extras').compile.dependencies,
                 project('processor').package(:jar),
                 project('processor').compile.dependencies

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Darez.dynamic_provider=true -Darez.logger=proxy -Darez.environment=development')
  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.extra_modules << '../mobx/mobx.iml'
  ipr.extra_modules << '../mobx-react/mobx-react.iml'
  ipr.extra_modules << '../mobx-utils/mobx-utils.iml'
end
