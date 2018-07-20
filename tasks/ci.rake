desc 'Continuous Integration task'
task 'ci' do
  previous_version = IO.read('CHANGELOG.md')[/^### \[v(\d+\.\d+)\]/, 1]
  version_parts = previous_version.split('.')
  version = "#{version_parts[0]}.#{sprintf('%02d', version_parts[1].to_i + 1)}"

  sh "bundle exec buildr clean package jacoco:report site:deploy error_codes:check_duplicates mcrt:publish_if_tagged PRODUCT_VERSION=#{version} PREVIOUS_PRODUCT_VERSION=#{previous_version}"
end
