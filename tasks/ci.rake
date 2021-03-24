desc 'Continuous Integration task'
task 'ci' do
  derive_versions
  sh "bundle exec buildr clean package jacoco:report site:deploy J2CL=#{ENV['J2CL']} PRODUCT_VERSION=#{ENV['PRODUCT_VERSION']} PREVIOUS_PRODUCT_VERSION=#{ENV['PREVIOUS_PRODUCT_VERSION']}"
end
