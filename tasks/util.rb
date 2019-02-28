WORKSPACE_DIR = File.expand_path(File.dirname(__FILE__) + '/..')

DOWNSTREAM_PROJECTS=%w(arez-browserlocation arez-dom arez-networkstatus arez-promise arez-spytools)

# Project -> [Branch1, Branch2, ...]
DOWNSTREAM_EXAMPLES =
  {
    'react4j-todomvc' => %w(raw raw_maven arez arez_maven dagger dagger_maven raw_maven_j2cl arez_maven_j2cl dagger_maven_j2cl),
    'react4j-drumloop' => %w(master),
    'react4j-flux-challenge' => %w(master),
  }

def in_dir(dir)
  current = Dir.pwd
  begin
    Dir.chdir(dir)
    yield
  ensure
    Dir.chdir(current)
  end
end

def derive_versions
  changelog = IO.read('CHANGELOG.md')
  ENV['PREVIOUS_PRODUCT_VERSION'] ||= changelog[/^### \[v(\d+\.\d+)\]/, 1]

  next_version = ENV['PRODUCT_VERSION']
  unless next_version
    version_parts = ENV['PREVIOUS_PRODUCT_VERSION'].split('.')
    next_version = "#{version_parts[0]}.#{sprintf('%02d', version_parts[1].to_i + 1)}"
    ENV['PRODUCT_VERSION'] = next_version
  end
end
