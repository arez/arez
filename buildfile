require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/gwt'
require 'buildr/jacoco'
require 'buildr/top_level_generate_dir'
require 'buildr/shade'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

TEST_DEPS = [:guiceyloops]
GWT_DEPS = [:akasha, :jsinterop_base]
DAGGER_RUNTIME_DEPS = [:javax_inject, :dagger_core]
DAGGER_PROCESSOR_DEPS =
  [
    :javax_inject,
    :dagger_core,
    :dagger_producers,
    :dagger_spi,
    :dagger_compiler,
    :guava_failureaccess,
    :kotlinx_metadata_jvm,
    :kotlin_stdlib,
    :kotlin_stdlib_common,
    :googlejavaformat,
    :errorprone,
    :javapoet,
    :guava
  ]

# JDK options passed to test environment. Essentially turns assertions on.
AREZ_TEST_OPTIONS =
  {
    'braincheck.environment' => 'development',
    'arez.environment' => 'development'
  }

GWT_EXAMPLES = {
  'promise-example' => %w(arez.promise.example.ObservablePromiseExample),
  'dom-example' => %w(arez.dom.example.BrowserLocationExample
                      arez.dom.example.DocumentVisibilityExample
                      arez.dom.example.GeoPositionExample
                      arez.dom.example.IdleStatusExample
                      arez.dom.example.MediaQueryExample
                      arez.dom.example.NetworkStatusExample
                      arez.dom.example.WindowSizeExample)
}

desc 'Arez: Simple, Scalable State Management Library'
define 'arez' do
  project.group = 'org.realityforge.arez'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all,-processing,-serial'
  project.compile.options.warnings = true
  project.compile.options.other = %w(-Werror -Xmaxerrs 10000 -Xmaxwarns 10000)

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('arez/arez')
  pom.add_developer('realityforge', 'Peter Donald')

  desc 'Arez Core'
  define 'core' do
    deps = artifacts(:javax_annotation, :jsinterop_annotations, :jetbrains_annotations, :braincheck_core, :grim_annotations)
    pom.include_transitive_dependencies << deps
    pom.dependency_filter = Proc.new { |dep| dep[:scope].to_s != 'test' && deps.include?(dep[:artifact]) }

    compile.with deps

    compile.options[:processor_path] << artifacts(:grim_processor, :javax_json)

    test.options[:properties] =
      AREZ_TEST_OPTIONS.merge('arez.core.compile_target' => compile.target.to_s,
                              'arez.diagnostic_messages_file' => _('src/test/java/arez/diagnostic_messages.json'))
    test.options[:java_args] = ['-ea']

    gwt_enhance(project)

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with TEST_DEPS, :braincheck_testng, :jdepend, :javax_json
  end

  desc 'API Test'
  define 'api-test' do
    test.compile.with :javax_annotation,
                      :javax_json,
                      :gir

    test.options[:properties] =
      {
        'arez.api_test.store_api_diff' => ENV['STORE_API_DIFF'] == 'true',
        'arez.prev.version' => ENV['PREVIOUS_PRODUCT_VERSION'],
        'arez.prev.jar' => artifact("org.realityforge.arez:arez-core:jar:#{ENV['PREVIOUS_PRODUCT_VERSION'] || project.version}").to_s,
        'arez.next.version' => ENV['PRODUCT_VERSION'],
        'arez.next.jar' => project('core').package(:jar).to_s,
        'arez.api_test.fixture_dir' => _('src/test/resources/fixtures').to_s,
        'arez.revapi.jar' => artifact(:revapi_diff).to_s
      }
    test.options[:java_args] = ['-ea']
    test.using :testng

    test.compile.enhance do
      mkdir_p _('src/test/resources/fixtures')
      artifact("org.realityforge.arez:arez-core:jar:#{ENV['PREVIOUS_PRODUCT_VERSION']}").invoke
      project('core').package(:jar).invoke
      artifact(:revapi_diff).invoke
    end unless ENV['TEST'] == 'no' || ENV['PRODUCT_VERSION'].nil? || ENV['PREVIOUS_PRODUCT_VERSION'].nil?

    test.exclude '*ApiDiffTest' if ENV['PRODUCT_VERSION'].nil? || ENV['PREVIOUS_PRODUCT_VERSION'].nil?

    project.jacoco.enabled = false
  end

  desc 'Arez Annotation processor'
  define 'processor' do
    pom.dependency_filter = Proc.new { |_| false }

    compile.with :javax_annotation,
                 :proton_core,
                 :javapoet

    test.with :compile_testing,
              Buildr::Util.tools_jar,
              :proton_qa,
              :truth,
              :error_prone_annotations,
              :junit,
              :hamcrest_core,
              DAGGER_PROCESSOR_DEPS,
              :sting_core,
              :sting_processor,
              project('core').package(:jar),
              project('core').compile.dependencies

    package(:jar)
    package(:sources)
    package(:javadoc)

    package(:jar).enhance do |jar|
      jar.merge(artifact(:javapoet))
      jar.merge(artifact(:proton_core))
      jar.enhance do |f|
        Buildr::Shade.shade(f,
                            f,
                            'com.squareup.javapoet' => 'arez.processor.vendor.javapoet',
                            'org.realityforge.proton' => 'arez.processor.vendor.proton')
      end
    end

    test.using :testng
    test.options[:properties] = { 'arez.fixture_dir' => _('src/test/fixtures') }
    test.compile.with TEST_DEPS

    iml.test_source_directories << _('src/test/fixtures/input')
    iml.test_source_directories << _('src/test/fixtures/expected')
    iml.test_source_directories << _('src/test/fixtures/bad_input')
  end

  desc 'Arez Integration Tests'
  define 'integration-tests' do
    test.options[:properties] = AREZ_TEST_OPTIONS.merge('arez.integration_fixture_dir' => _('src/test/resources'))
    test.options[:java_args] = ['-ea']

    test.using :testng
    test.compile.with TEST_DEPS,
                      DAGGER_RUNTIME_DEPS,
                      :sting_core,
                      GWT_DEPS,
                      :javax_json,
                      :jsonassert,
                      :android_json,
                      project('core').package(:jar),
                      project('core').compile.dependencies,
                      project('processor').package(:jar),
                      project('processor').compile.dependencies,
                      :sting_processor,
                      DAGGER_PROCESSOR_DEPS
    test.compile.options[:processor] = true

    # The generators are configured to generate to here.
    iml.test_source_directories << _('generated/processors/test/java')
  end

  desc 'Test Arez in downstream projects'
  define 'downstream-test' do
    compile.with :gir,
                 :javax_annotation

    test.options[:properties] =
      AREZ_TEST_OPTIONS.merge(
        'arez.prev.version' => ENV['PREVIOUS_PRODUCT_VERSION'] || project.version,
        'arez.next.version' => ENV['PRODUCT_VERSION'] || project.version,
        'arez.next.jar' => project('core').package(:jar).to_s,
        'arez.build_j2cl_variants' => (ENV['J2CL'] != 'no'),
        'arez.deploy_test.fixture_dir' => _('src/test/resources/fixtures').to_s,
        'arez.deploy_test.work_dir' => _(:target, 'deploy_test/workdir').to_s
      )
    test.options[:java_args] = ['-ea']

    local_test_repository_url = URI.join('file:///', project._(:target, :local_test_repository)).to_s
    compile.enhance do
      projects_to_upload = projects(%w(core processor arez:extras:promise arez:extras:testng))
      old_release_to = repositories.release_to
      begin
        # First we install them in a local repository so we don't have to access the network during local builds
        repositories.release_to = local_test_repository_url
        projects_to_upload.each do |prj|
          prj.packages.each do |pkg|
            # Uninstall version already present in local maven cache
            pkg.uninstall
            # Install version into local repository
            pkg.upload
          end
        end
        if ENV['STAGE_RELEASE'] == 'true'
          # Then we install it to a remote repository so that TravisCI can access the builds when it attempts
          # to perform a release
          repositories.release_to =
            { :url => 'https://stocksoftware.jfrog.io/stocksoftware/staging', :username => ENV['STAGING_USERNAME'], :password => ENV['STAGING_PASSWORD'] }
          projects_to_upload.each do |prj|
            prj.packages.each(&:upload)
          end
        end
      ensure
        repositories.release_to = old_release_to
      end
    end unless ENV['TEST'] == 'no' # These artifacts only required when running tests.

    test.compile.enhance do
      cp = project.compile.dependencies.map(&:to_s) + [project.compile.target.to_s]

      properties = {}
      # Take the version that we are releasing else fallback to project version
      properties['arez.prev.version'] = ENV['PREVIOUS_PRODUCT_VERSION'] || project.version
      properties['arez.next.version'] = ENV['PRODUCT_VERSION'] || project.version
      properties['arez.build_j2cl_variants'] = ENV['J2CL'] != 'no'
      properties['arez.deploy_test.work_dir'] = _(:target, 'deploy_test/workdir').to_s
      properties['arez.deploy_test.fixture_dir'] = _('src/test/resources/fixtures').to_s
      properties['arez.deploy_test.local_repository_url'] = local_test_repository_url
      properties['arez.deploy_test.store_statistics'] = ENV['STORE_BUILD_STATISTICS'] == 'true'
      properties['arez.deploy_test.build_before'] = (ENV['STORE_BUILD_STATISTICS'] != 'true' && ENV['BUILD_BEFORE'] != 'no')

      Java::Commands.java 'arez.downstream.CollectHeartRateMonitorStats', { :classpath => cp, :properties => properties } unless ENV['BUILD_STATS'] == 'no'
      Java::Commands.java 'arez.downstream.CollectWebSpeechDemoStats', { :classpath => cp, :properties => properties } unless ENV['BUILD_STATS'] == 'no'
      Java::Commands.java 'arez.downstream.CollectDrumLoopBuildStats', { :classpath => cp, :properties => properties } unless ENV['BUILD_STATS'] == 'no'
      Java::Commands.java 'arez.downstream.CollectFluxChallengeBuildStats', { :classpath => cp, :properties => properties } unless ENV['BUILD_STATS'] == 'no'
      Java::Commands.java 'arez.downstream.BuildDownstream', { :classpath => cp, :properties => properties } unless ENV['DOWNSTREAM'] == 'no'
      Java::Commands.java 'arez.downstream.CollectBuildStats', { :classpath => cp, :properties => properties } unless ENV['BUILD_STATS'] == 'no'
    end

    # Only run this test when preparing for release, never on TravisCI (as produces different byte sizes)
    test.exclude '*BuildStatsTest' if ENV['PRODUCT_VERSION'].nil? || ENV['BUILD_STATS'] == 'no' || !ENV['TRAVIS_BUILD_NUMBER'].nil?
    test.exclude '*BuildOutputTest' if ENV['BUILD_STATS'] == 'no'

    test.using :testng
    test.compile.with :javax_annotation,
                      :javacsv,
                      :grim_asserts,
                      :javax_json,
                      :gwt_symbolmap,
                      :jetbrains_annotations,
                      :testng

    project.jacoco.enabled = false
  end

  define 'extras' do
    define 'promise' do
      deps = artifacts(:javax_annotation, :jsinterop_annotations, :jsinterop_base, :jetbrains_annotations, :braincheck_core, :grim_annotations, :akasha) + [project('core').package(:jar)]
      pom.include_transitive_dependencies << deps
      pom.dependency_filter = Proc.new { |dep| deps.include?(dep[:artifact]) }

      compile.with deps,
                   project('processor').package(:jar),
                   project('processor').compile.dependencies

      compile.options[:processor] = true

      gwt_enhance(project)

      package(:jar)
      package(:sources)
      package(:javadoc)

      project.jacoco.enabled = false
    end

    define 'promise-example' do
      compile.with project('promise').package(:jar),
                   project('promise').compile.dependencies,
                   :gwt_user

      gwt_enhance(project, :package_jars => false)

      gwt_modules = {}
      GWT_EXAMPLES['promise-example'].each do |gwt_module|
        gwt_modules[gwt_module] = false
      end
      iml.add_gwt_facet(gwt_modules,
                        :settings => { :compilerMaxHeapSize => '1024' },
                        :gwt_dev_artifact => :gwt_dev)

      project.no_ipr
    end

    desc 'TestNG: Arez utilities for writing TestNG tests'
    define 'testng' do
      deps = artifacts(:javax_annotation, :testng, :braincheck_core) + [project('core').package(:jar)]
      pom.include_transitive_dependencies << deps
      pom.dependency_filter = Proc.new { |dep| deps.include?(dep[:artifact]) }

      compile.with deps

      package(:jar)
      package(:sources)
      package(:javadoc)
    end

    desc 'Dom: Arez browser components that make DOM properties observable'
    define 'dom' do
      deps = artifacts(:javax_annotation,
                       :braincheck_core,
                       :jetbrains_annotations,
                       :javaemul_internal_annotations,
                       :grim_annotations,
                       :jsinterop_base,
                       :jsinterop_annotations,
                       :akasha) + [project('core').package(:jar)]
      pom.include_transitive_dependencies << deps
      pom.dependency_filter = Proc.new { |dep| deps.include?(dep[:artifact]) }

      compile.with deps

      compile.with deps,
                   project('processor').package(:jar),
                   project('processor').compile.dependencies

      compile.options[:processor] = true

      gwt_enhance(project)

      package(:jar)
      package(:sources)
      package(:javadoc)

      test.options[:properties] = { 'arez.environment' => 'development' }
      test.options[:java_args] = ['-ea']

      test.using :testng
      test.compile.with [:guiceyloops]
    end

    define 'dom-example' do
      compile.with project('dom').package(:jar),
                   project('dom').compile.dependencies,
                   :gwt_user

      gwt_enhance(project, :package_jars => false)

      gwt_modules = {}
      GWT_EXAMPLES['dom-example'].each do |gwt_module|
        gwt_modules[gwt_module] = false
      end

      iml.add_gwt_facet(gwt_modules,
                        :settings => { :compilerMaxHeapSize => '1024' },
                        :gwt_dev_artifact => :gwt_dev)

      project.no_ipr
    end
    project.no_iml
  end

  desc 'Arez Examples used in documentation'
  define 'doc-examples' do
    compile.with project('core').package(:jar),
                 project('core').compile.dependencies,
                 project('processor').package(:jar),
                 project('processor').compile.dependencies,
                 :sting_processor,
                 DAGGER_PROCESSOR_DEPS,
                 :gwt_user,
                 DAGGER_RUNTIME_DEPS,
                 :sting_core,
                 GWT_DEPS

    compile.options[:processor] = true

    project.jacoco.enabled = false
  end

  doc.from(projects(%w(core processor extras:promise extras:testng extras:dom))).
    using(:javadoc,
          :windowtitle => 'Arez API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/),
          :group => {
            'Core Packages' => 'arez:arez.spy*',
            'Component Packages' => 'arez.annotations*:arez.component*:arez.processor*',
            'Extras Packages' => 'arez.promise*:arez.dom*:arez.testng*'
          }
    )

  cleanup_javadocs(project, 'arez')

  iml.excluded_directories << project._('node_modules')
  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => "-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.output_fixture_data=false -Darez.deploy_test.build_before=true -Darez.fixture_dir=processor/src/test/resources -Darez.integration_fixture_dir=integration-tests/src/test/resources -Darez.api_test.fixture_dir=api-test/src/test/resources/fixtures -Darez.deploy_test.fixture_dir=downstream-test/src/test/resources/fixtures -Darez.deploy_test.work_dir=target/arez_downstream-test/deploy_test/workdir -Darez.prev.version=X -Darez.prev.jar=#{artifact("org.realityforge.arez:arez-core:jar:#{ENV['PREVIOUS_PRODUCT_VERSION'] || project.version}")} -Darez.next.version=X -Darez.next.jar=#{project('core').package(:jar)} -Darez.core.compile_target=target/arez_core/idea/classes -Darez.revapi.jar=#{artifact(:revapi_diff)} -Darez.diagnostic_messages_file=core/src/test/java/arez/diagnostic_messages.json -Darez.check_diagnostic_messages=false")

  ipr.add_testng_configuration('core',
                               :module => 'core',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.output_fixture_data=false -Darez.core.compile_target=../target/arez_core/idea/classes -Darez.check_diagnostic_messages=false -Darez.diagnostic_messages_file=src/test/java/arez/diagnostic_messages.json')
  ipr.add_testng_configuration('core - update invariant messages',
                               :module => 'core',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.output_fixture_data=true -Darez.core.compile_target=../target/arez_core/idea/classes -Darez.check_diagnostic_messages=true -Darez.diagnostic_messages_file=src/test/java/arez/diagnostic_messages.json')
  ipr.add_testng_configuration('processor',
                               :module => 'processor',
                               :jvm_args => '-ea -Darez.output_fixture_data=true -Darez.fixture_dir=src/test/fixtures')
  ipr.add_testng_configuration('integration-tests',
                               :module => 'integration-tests',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.output_fixture_data=true -Darez.integration_fixture_dir=src/test/resources')

  GWT_EXAMPLES.each_pair do |project_name, gwt_modules|
    gwt_modules.each do |gwt_module|
      short_name = gwt_module.gsub(/.*\./, '')
      ipr.add_gwt_configuration(project,
                                :iml_name => project_name,
                                :name => "#{project_name}: #{short_name}",
                                :gwt_module => gwt_module,
                                :start_javascript_debugger => false,
                                :open_in_browser => false,
                                :vm_parameters => '-Xmx2G',
                                :shell_parameters => "-strict -style PRETTY -XmethodNameDisplayMode FULL -nostartServer -incremental -codeServerPort 8889 -bindAddress 0.0.0.0 -deploy #{_(:generated, :gwt, 'deploy')} -extra #{_(:generated, :gwt, 'extra')} -war #{_(:generated, :gwt, 'war')}",
                                :launch_page => "http://127.0.0.1:8888/#{gwt_module}/#{short_name}.html")
    end
  end

  ipr.nonnull_assertions = false

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all,-processing,-serial -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end

Buildr.projects.each do |project|
  unless project.name == 'arez'
    project.doc.options.merge!('Xdoclint:all' => true)
  end
end
