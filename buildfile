require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

GWT_EXAMPLES =
  {
    'BrowserLocationExample' => 'browser_location',
    'DocumentVisibilityExample' => 'document_visibility',
    'GeoPositionExample' => 'geo_position',
    'IdleStatusExample' => 'idle_status',
    'MediaQueryExample' => 'media_query',
    'NetworkStatusExample' => 'network_status',
    'WindowSizeExample' => 'window_size'
  }

desc 'Arez-Dom: Arez browser components that make DOM properties observable'
define 'arez-dom' do
  project.group = 'org.realityforge.arez.dom'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('arez/arez-dom')
  pom.add_developer('realityforge', 'Peter Donald')

  dom_artifact = artifact(:arez_core)
  pom.include_transitive_dependencies << dom_artifact
  pom.dependency_filter = Proc.new {|dep| dom_artifact == dep[:artifact]}

  project.processorpath << :arez_processor

  compile.with :javax_annotation,
               :braincheck,
               :grim_annotations,
               :jsinterop_base,
               :jsinterop_annotations,
               :elemental2_core,
               :elemental2_promise,
               :elemental2_dom,
               :arez_core,
               # gwt_user is present for @DoNotAutobox annotation
               :gwt_user

  gwt_enhance(project)

  package(:jar)
  package(:sources)
  package(:javadoc)

  test.options[:properties] = { 'arez.environment' => 'development' }
  test.options[:java_args] = ['-ea']

  test.using :testng
  test.compile.with [:guiceyloops]

  doc.
    using(:javadoc,
          :windowtitle => 'Arez DOM API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/)
    )

  iml.excluded_directories << project._('tmp')
  ipr.extra_modules << 'example/example.iml'

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Darez.environment=development')
  ipr.add_component_from_artifact(:idea_codestyle)

  GWT_EXAMPLES.each_pair do |gwt_module, path|
    short_name = gwt_module
    ipr.add_gwt_configuration(project,
                              :iml_name => 'example',
                              :name => "GWT Example: #{short_name}",
                              :gwt_module => "arez.dom.example.#{gwt_module}",
                              :start_javascript_debugger => false,
                              :vm_parameters => '-Xmx2G',
                              :shell_parameters => "-port 8888 -codeServerPort 8889 -bindAddress 0.0.0.0 -war #{_(:generated, 'gwt-export', short_name)}/",
                              :launch_page => "http://127.0.0.1:8888/#{path}")
  end
end

define 'example', :base_dir => "#{File.dirname(__FILE__)}/example" do
  compile.options.source = '1.8'
  compile.options.target = '1.8'

  compile.with project('arez-dom').package(:jar),
               project('arez-dom').compile.dependencies,
               :gwt_user

  gwt_enhance(project)

  gwt_modules = {}
  GWT_EXAMPLES.each do |gwt_module|
    gwt_modules[gwt_module] = false
  end
  iml.add_gwt_facet(gwt_modules,
                    :settings => { :compilerMaxHeapSize => '1024' },
                    :gwt_dev_artifact => :gwt_dev)

  project.no_ipr
end

task('idea' => 'example:idea')
task('package' => 'example:package')
