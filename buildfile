require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/gwt'

PROVIDED_DEPS = [:javax_jsr305, :anodoc]
COMPILE_DEPS = []
OPTIONAL_DEPS = []
TEST_DEPS = [:guiceyloops]
GWT_DEPS =
  [
    :gwt_user,
    :elemental2_core,
    :elemental2_dom,
    :elemental2_promise,
    :jsinterop_base,
    :jsinterop_base_sources,
    :jsinterop_annotations,
    :jsinterop_annotations_sources
  ]

GWT_EXAMPLES=%w(IdleStatusExample BrowserLocationExample NetworkStatusExample ObservablePromiseExample TimedDisposerExample IntervalTickerExample).collect {|c| "org.realityforge.arez.gwt.examples.#{c}"}
DOC_EXAMPLES=%w().collect {|c| "org.realityforge.arez.doc.examples.#{c}"}

# JDK options passed to test environment. Essentially turns assertions on.
AREZ_TEST_OPTIONS =
  {
    'braincheck.dynamic_provider' => 'true',
    'braincheck.environment' => 'development',
    'arez.dynamic_provider' => 'true',
    'arez.logger' => 'proxy',
    'arez.environment' => 'development'
  }

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

    gwt_enhance(project)

    package(:jar)
    package(:sources)
    package(:javadoc)
  end

  define 'core' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with PROVIDED_DEPS,
                 COMPILE_DEPS,
                 :braincheck

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    gwt_enhance(project)

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

    gwt_enhance(project)

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS
  end

  define 'browser-extras' do
    pom.provided_dependencies.concat PROVIDED_DEPS + [:jetbrains_annotations]

    compile.with project('annotations').package(:jar, :classifier => :gwt),
                 project('annotations').compile.dependencies,
                 project('core').package(:jar, :classifier => :gwt),
                 project('core').compile.dependencies,
                 project('extras').package(:jar, :classifier => :gwt),
                 project('extras').compile.dependencies,
                 project('processor').package(:jar),
                 project('processor').compile.dependencies,
                 :jetbrains_annotations,
                 GWT_DEPS

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS

    gwt_enhance(project)

    # The generators are configured to generate to here.
    iml.main_source_directories << _('generated/processors/main/java')
  end

  define 'processor' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with PROVIDED_DEPS,
                 COMPILE_DEPS,
                 :autoservice,
                 :autocommon,
                 :javapoet,
                 :guava,
                 project('annotations')

    test.with :compile_testing,
              Java.tools_jar,
              :truth,
              project('core').package(:jar),
              project('core').compile.dependencies

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.options[:properties] = {'arez.fixture_dir' => _('src/test/resources')}
    test.compile.with TEST_DEPS

    iml.test_source_directories << _('src/test/resources/input')
    iml.test_source_directories << _('src/test/resources/expected')
    iml.test_source_directories << _('src/test/resources/bad_input')
  end

  define 'integration-tests' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    test.options[:properties] = AREZ_TEST_OPTIONS.merge('arez.integration_fixture_dir' => _('src/test/resources'))
    test.options[:java_args] = ['-ea']

    test.using :testng
    test.compile.with TEST_DEPS,
                      :javax_json,
                      :jsonassert,
                      :android_json,
                      project('annotations').package(:jar),
                      project('annotations').compile.dependencies,
                      project('core').package(:jar),
                      project('core').compile.dependencies,
                      project('extras').package(:jar),
                      project('extras').compile.dependencies,
                      project('processor').package(:jar),
                      project('processor').compile.dependencies

    # The generators are configured to generate to here.
    iml.test_source_directories << _('generated/processors/test/java')
  end

  define 'gwt-example' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with project('browser-extras').package(:jar, :classifier => :gwt),
                 project('browser-extras').compile.dependencies

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    test.using :testng
    test.compile.with TEST_DEPS

    gwt_modules = {}
    GWT_EXAMPLES.each do |gwt_module|
      gwt_modules[gwt_module] = false
    end
    iml.add_gwt_facet(gwt_modules, :settings => { :compilerMaxHeapSize => '1024' }, :gwt_dev_artifact => :gwt_dev)
  end

  define 'doc-examples' do
    pom.provided_dependencies.concat PROVIDED_DEPS

    compile.with project('browser-extras').package(:jar, :classifier => :gwt),
                 project('browser-extras').compile.dependencies

    test.options[:properties] = AREZ_TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    test.using :testng
    test.compile.with TEST_DEPS

    gwt_modules = {}
    DOC_EXAMPLES.each do |gwt_module|
      gwt_modules[gwt_module] = false
    end
    iml.add_gwt_facet(gwt_modules, :settings => { :compilerMaxHeapSize => '1024' }, :gwt_dev_artifact => :gwt_dev)

    # The generators are configured to generate to here.
    iml.main_source_directories << _('generated/processors/main/java')
  end

  doc.from(projects(%w(annotations core processor extras browser-extras))).
    using(:javadoc,
          :windowtitle => 'Arez API Documentation',
          :linksource => true,
          :link => %w(https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/),
          :group => {
            'Core Packages' => 'org.realityforge.arez:org.realityforge.arez.spy*',
            'Annotation Packages' => 'org.realityforge.arez.annotations*:org.realityforge.arez.processor*',
            'Extras Packages' => 'org.realityforge.arez.extras*',
            'Browser Extras Packages' => 'org.realityforge.arez.browser*'
          }
    )

  iml.excluded_directories << project._('node_modules')
  iml.excluded_directories << project._('tmp/gwt')
  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Dbraincheck.dynamic_provider=true -Dbraincheck.environment=development -Darez.dynamic_provider=true -Darez.logger=proxy -Darez.environment=development -Darez.output_fixture_data=false -Darez.fixture_dir=processor/src/test/resources -Darez.integration_fixture_dir=integration-tests/src/test/resources')
  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.extra_modules << '../mobx/mobx.iml'
  ipr.extra_modules << '../mobx-docs/mobx-docs.iml'
  ipr.extra_modules << '../mobx-react/mobx-react.iml'
  ipr.extra_modules << '../mobx-react-devtools/mobx-react-devtools.iml'
  ipr.extra_modules << '../mobx-utils/mobx-utils.iml'

  GWT_EXAMPLES.each do |gwt_module|
    short_name = gwt_module.gsub(/.*\./, '')
    ipr.add_gwt_configuration(project('gwt-example'),
                              :name => "GWT Example: #{short_name}",
                              :gwt_module => gwt_module,
                              :start_javascript_debugger => false,
                              :vm_parameters => "-Xmx3G -Djava.io.tmpdir=#{_("tmp/gwt/#{short_name}")}",
                              :shell_parameters => "-port 8888 -codeServerPort 8889 -bindAddress 0.0.0.0 -war #{_(:generated, 'gwt-export', short_name)}/")
  end
  DOC_EXAMPLES.each do |gwt_module|
    short_name = gwt_module.gsub(/.*\./, '')
    ipr.add_gwt_configuration(project('doc-examples'),
                              :name => "Doc Example: #{short_name}",
                              :gwt_module => gwt_module,
                              :start_javascript_debugger => false,
                              :vm_parameters => "-Xmx3G -Djava.io.tmpdir=#{_("tmp/gwt/#{short_name}")}",
                              :shell_parameters => "-port 8888 -codeServerPort 8889 -bindAddress 0.0.0.0 -war #{_(:generated, 'gwt-export', short_name)}/")
  end

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
