require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

desc 'Arez-IdleStatus: Arez Browser component that tracks when the user is idle'
define 'arez-idlestatus' do
  project.group = 'org.realityforge.arez.idlestatus'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  dom_artifact = artifact(:arez_component)
  pom.include_transitive_dependencies << dom_artifact
  pom.dependency_filter = Proc.new {|dep| dom_artifact == dep[:artifact]}

  project.processorpath << :arez_processor

  compile.with :javax_jsr305,
               :anodoc,
               :jsinterop_base,
               :jsinterop_base_sources,
               :jsinterop_annotations,
               :jsinterop_annotations_sources,
               :elemental2_core,
               :elemental2_dom,
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
          :windowtitle => 'Arez IdleStatus API Documentation',
          :linksource => true,
          :timestamp => false,
          :link => %w(https://arez.github.io/api https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/)
    )

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
end
