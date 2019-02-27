desc 'Continuous Integration task'
task 'ci' do
  contents = IO.read('CHANGELOG.md')
  if contents =~ /^### Unreleased/
    previous_version = contents[/^### \[v(\d+\.\d+)\]/, 1]
    version_parts = previous_version.split('.')
    version = "#{version_parts[0]}.#{sprintf('%02d', version_parts[1].to_i + 1)}"
  else
    version = contents[/^### \[v(\d+\.\d+)\]/, 1]
    previous_version = contents[contents.index(version)...100000][/^### \[v(\d+\.\d+)\]/, 1]
  end

  sh "bundle exec buildr clean package jacoco:report site:deploy PRODUCT_VERSION=#{version} PREVIOUS_PRODUCT_VERSION=#{previous_version}"
end
