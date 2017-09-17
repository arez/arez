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

    gwt_enhance(project, ['org.realityforge.arez.annotations.Annotations'])

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

    gwt_enhance(project, %w(org.realityforge.arez.Arez org.realityforge.arez.ArezDev))

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  define 'extras' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with project('core').package(:jar, :classifier => :gwt),
                 project('core').compile.dependencies

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    gwt_enhance(project, ['org.realityforge.arez.extras.Extras'])

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

    iml.test_source_directories << _('src/test/resources/input')
    iml.test_source_directories << _('src/test/resources/expected')
    iml.test_source_directories << _('src/test/resources/bad_input')
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

    # The generators are configured to generate to here.
    iml.main_source_directories << _('generated/processors/main/java')
  end

  define 'gwt-example' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with project('annotations').package(:jar, :classifier => :gwt),
                 project('annotations').compile.dependencies,
                 project('core').package(:jar, :classifier => :gwt),
                 project('core').compile.dependencies,
                 project('extras').package(:jar, :classifier => :gwt),
                 project('extras').compile.dependencies,
                 project('processor').package(:jar),
                 project('processor').compile.dependencies,
                 :elemental2_core,
                 :elemental2_dom,
                 :elemental2_promise,
                 :jsinterop_base,
                 :jsinterop_base_sources,
                 :jsinterop_annotations,
                 :jsinterop_annotations_sources

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS

    iml.add_gwt_facet({ 'org.realityforge.arez.gwt.examples.OnlineTest' => true }, :settings => { :compilerMaxHeapSize => '1024' }, :gwt_dev_artifact => :gwt_dev)

    # The generators are configured to generate to here.
    iml.main_source_directories << _('generated/processors/main/java')
  end

  doc.from(projects(%w(arez:annotations arez:core arez:processor arez:extras))).using(:javadoc, :windowtitle => 'Arez')

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Darez.dynamic_provider=true -Darez.logger=proxy -Darez.environment=development -Darez.output_fixture_data=false -Darez.fixture_dir=processor/src/test/resources')
  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.extra_modules << '../mobx/mobx.iml'
  ipr.extra_modules << '../mobx-react/mobx-react.iml'
  ipr.extra_modules << '../mobx-react-devtools/mobx-react-devtools.iml'
  ipr.extra_modules << '../mobx-utils/mobx-utils.iml'

  ipr.add_gwt_configuration(project('gwt-example'),
                            :gwt_module => 'org.realityforge.arez.gwt.examples.OnlineTest',
                            :start_javascript_debugger => false,
                            :vm_parameters => "-Xmx3G -Djava.io.tmpdir=#{_('tmp/gwt')}",
                            :shell_parameters => "-port 8888 -codeServerPort 8889 -bindAddress 0.0.0.0 -war #{_(:generated, 'gwt-export')}/")


  ipr.add_component('CompilerConfiguration') do |component|
    component.annotationProcessing do |xml|
      xml.profile(:default => true, :name => 'Default', :enabled => true) do
        xml.sourceOutputDir :name => 'generated/processors/main/java'
        xml.sourceTestOutputDir :name => 'generated/processors/test/java'
        xml.outputRelativeToContentRoot :value => true
      end
    end
  end
end
