require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/gwt'
require 'buildr/top_level_generate_dir'
require 'buildr/shade'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

# JDK options passed to test environment. Essentially turns assertions on.
TEST_OPTIONS =
  {
    'braincheck.environment' => 'development',
    'arez.environment' => 'development',
    'arez.persist.environment' => 'development'
  }

desc 'Arez-Persist: Arez extension for persisting observable properties'
define 'arez-persist' do
  project.group = 'org.realityforge.arez.persist'
  compile.options.source = '17'
  compile.options.target = '17'
  compile.options.lint = 'all,-processing,-serial'
  project.compile.options.warnings = true
  project.compile.options.other = %w(-Werror -Xmaxerrs 10000 -Xmaxwarns 10000)

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('arez/arez-persist')
  pom.add_developer('realityforge', 'Peter Donald')

  desc 'The Core Library'
  define 'core' do
    deps = artifacts(:javax_annotation,
                     :grim_annotations,
                     :braincheck,
                     :arez_core,
                     :jetbrains_annotations,
                     :akasha,
                     :jsinterop_base,
                     :jsinterop_annotations)
    pom.include_transitive_dependencies << deps
    pom.dependency_filter = Proc.new { |dep| dep[:scope].to_s != 'test' && deps.include?(dep[:artifact]) }

    compile.with deps

    compile.options[:processor_path] << [:arez_processor, :grim_processor, :javax_json]

    test.options[:properties] =
      TEST_OPTIONS.merge('arez.persist.core.compile_target' => compile.target.to_s)
    test.options[:java_args] = ['-ea']

    gwt_enhance(project)

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.compile.with :guiceyloops,
                      :jdepend,
                      :arez_testng,
                      :mockito,
                      :byte_buddy,
                      :objenesis

  end

  desc 'The Annotation processor'
  define 'processor' do
    pom.dependency_filter = Proc.new { |_| false }

    compile.with :javax_annotation,
                 :proton_core,
                 :javapoet

    test.with :proton_qa,
              :arez_processor,
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
                            'com.squareup.javapoet' => 'arez.persist.processor.vendor.javapoet',
                            'org.realityforge.proton' => 'arez.persist.processor.vendor.proton')
      end
    end

    test.using :testng
    test.options[:properties] = { 'arez.persist.fixture_dir' => _('src/test/fixtures') }
    test.compile.with :guiceyloops

    iml.test_source_directories << _('src/test/fixtures/input')
    iml.test_source_directories << _('src/test/fixtures/expected')
    iml.test_source_directories << _('src/test/fixtures/bad_input')
  end

  desc 'Arez Integration Tests'
  define 'integration-tests' do
    test.options[:properties] = TEST_OPTIONS
    test.options[:java_args] = ['-ea']

    test.using :testng
    test.compile.with :guiceyloops,
                      :arez_testng,
                      :javax_json,
                      project('core').package(:jar),
                      project('core').compile.dependencies

    test.compile.options[:processor_path] << [:arez_processor, project('processor').package(:jar), project('processor').compile.dependencies]

    # The generators are configured to generate to here.
    iml.test_source_directories << _('generated/processors/test/java')
  end

  doc.from(projects(%w(core processor))).
    using(:javadoc,
          :windowtitle => 'Arez-Persist API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/ https://arez.github.io/api)
    ).sourcepath = project('core').compile.sources + project('processor').compile.sources

  cleanup_javadocs(project, 'arez/persist')

  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => "-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.persist.environment=development -Darez.persist.output_fixture_data=false -Darez.persist.fixture_dir=processor/src/test/resources -Darez.persist.core.compile_target=target/arez-persist_core/idea/classes")

  ipr.add_testng_configuration('core',
                               :module => 'core',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.persist.environment=development -Darez.persist.output_fixture_data=false -Darez.persist.core.compile_target=../target/arez-persist_core/idea/classes')
  ipr.add_testng_configuration('processor',
                               :module => 'processor',
                               :jvm_args => '-ea -Darez.persist.output_fixture_data=true -Darez.persist.fixture_dir=src/test/fixtures')
  ipr.add_testng_configuration('integration-tests',
                               :module => 'integration-tests',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Darez.environment=development -Darez.persist.environment=development')

  ipr.nonnull_assertions = false

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all,-processing,-serial -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end
