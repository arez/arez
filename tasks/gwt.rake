require 'buildr/gwt'

#
# Enhance the Buildr project to compile gwt sources.
# For each discovered gwt modules, this task will create
# a synthetic gwt module that includes a single entrypoint to
# compile against. It will also create a gwt classifier jar
# that includes all the sources.
#
def gwt_enhance(project)
  extra_deps = project.iml.main_generated_resource_directories.flatten.compact.collect do |a|
    a.is_a?(String) ? file(a) : a
  end + project.iml.main_generated_source_directories.flatten.compact.collect do |a|
    a.is_a?(String) ? file(a) : a
  end

  gwt_modules = []
  source_paths = project.compile.sources + project.iml.main_generated_resource_directories.flatten.compact + project.iml.main_generated_source_directories.flatten.compact
  source_paths.each do |base_dir|
    Dir["#{base_dir}/**/*.gwt.xml"].each do |filename|
      gwt_modules << filename.gsub("#{base_dir}/", '').gsub('.gwt.xml', '').gsub('/', '.')
    end
  end

  base_synthetic_module_dir = project._(:generated, :synthetic_gwt_module, :main, :resources)
  t = project.task('gwt_synthetic_module') do
    gwt_modules.each do |gwt_module|
      file = "#{base_synthetic_module_dir}/#{gwt_module.gsub('.', '/')}Test.gwt.xml"
      mkdir_p File.dirname(file)
      IO.write(file, <<CONTENT)
<module>
  <inherits name="#{gwt_module}"/>
  <inherits name="com.google.gwt.user.User"/>
  <collapse-all-properties/>
</module>
CONTENT
    end
  end
  dir = project.file(base_synthetic_module_dir => [t.name])

  dependencies =
    project.compile.dependencies + [project.compile.target] + extra_deps + [dir] + [Buildr.artifact(:gwt_user)]
  unless ENV['GWT'] == 'no'
    gwt_modules.each do |m|
      project.gwt([m], { :java_args => %w(-Xms512M -Xmx1024M), :dependencies => dependencies, :output_key => m })
    end
  end

  project.package(:jar, :classifier => :gwt).tap do |j|
    extra_deps.each do |dep|
      j.include("#{dep}/*")
    end
    j.include("#{project._(:source, :main, :java)}/*")
  end

  config = {}
  gwt_modules.each do |gwt_module|
    config[gwt_module] = false
  end
  project.iml.add_gwt_facet(config, :settings => { :compilerMaxHeapSize => '1024' }, :gwt_dev_artifact => :gwt_dev)
end
