desc 'Update the api_differences for the next version'
task 'update_api_diff' do
  derive_versions

  sh "buildr clean arez:api-test:test TEST=only GWT=no PRODUCT_VERSION=#{ENV['PRODUCT_VERSION']} PREVIOUS_PRODUCT_VERSION=#{ENV['PREVIOUS_PRODUCT_VERSION']} STORE_API_DIFF=true"
end

desc 'Test the api differences for the next version'
task 'test_api_diff' do
  derive_versions

  sh "buildr clean arez:api-test:test TEST=only GWT=no PRODUCT_VERSION=#{ENV['PRODUCT_VERSION']} PREVIOUS_PRODUCT_VERSION=#{ENV['PREVIOUS_PRODUCT_VERSION']}"
end

desc 'Regenerate api diffs'
task 'regenerate_api_diffs' do
  (128...133).each do |v|
    apidiff_generate("org.realityforge.arez:arez-core:jar", "0.#{v}", "0.#{v + 1}")
  end
end

def apidiff_generate(partial_spec, old_version, new_version)

  revapi_diff = Buildr.artifact(:revapi_diff)

  old_api = Buildr.artifact("#{partial_spec}:#{old_version}")
  new_api = Buildr.artifact("#{partial_spec}:#{new_version}")

  revapi_diff.invoke
  old_api.invoke
  new_api.invoke

  output_file = "#{WORKSPACE_DIR}/api-test/src/test/resources/fixtures/#{old_version}-#{new_version}.json"
  mkdir_p File.dirname(output_file)

  sh ['java', '-jar', revapi_diff.to_s, '--old-api', old_api.to_s, '--new-api', new_api.to_s, '--output-file', output_file].join(' ')

  sh "git add #{output_file}"
  output_file
end
