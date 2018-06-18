require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

GWT_EXAMPLES=%w(arez.promise.example.ObservablePromiseExample)

desc 'Arez-Promise: Arez component that wraps a Promise and makes it observable'
define 'arez-promise' do
  project.group = 'org.realityforge.arez.promise'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  dom_artifact = artifact(:arez_component)
  pom.include_transitive_dependencies << dom_artifact
  pom.dependency_filter = Proc.new {|dep| dom_artifact == dep[:artifact]}

  project.processorpath << :arez_processor

  compile.with :javax_jsr305,
               :jsinterop_base,
               :jsinterop_annotations,
               :elemental2_core,
               :elemental2_promise,
               :braincheck,
               :arez_annotations,
               :arez_core,
               :arez_component

  gwt_enhance(project)

  package(:jar)
  package(:sources)
  package(:javadoc)

  doc.
    using(:javadoc,
          :windowtitle => 'Arez ObservablePromise API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/)
    )

  iml.excluded_directories << project._('tmp')
  ipr.extra_modules << 'example/example.iml'

  ipr.add_component_from_artifact(:idea_codestyle)

  GWT_EXAMPLES.each do |gwt_module|
    short_name = gwt_module.gsub(/.*\./, '')
    ipr.add_gwt_configuration(project,
                              :iml_name => 'example',
                              :name => "GWT Example: #{short_name}",
                              :gwt_module => gwt_module,
                              :start_javascript_debugger => false,
                              :vm_parameters => "-Xmx3G -Djava.io.tmpdir=#{_("tmp/gwt/#{short_name}")}",
                              :shell_parameters => "-port 8888 -codeServerPort 8889 -bindAddress 0.0.0.0 -war #{_(:generated, 'gwt-export', short_name)}/")
  end
end

define 'example', :base_dir => "#{File.dirname(__FILE__)}/example" do
  compile.options.source = '1.8'
  compile.options.target = '1.8'

  compile.with project('arez-promise').package(:jar),
               project('arez-promise').compile.dependencies,
               :elemental2_dom,
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
