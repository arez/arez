require File.expand_path(File.dirname(__FILE__) + '/util')

SITE_DIR = "#{WORKSPACE_DIR}/reports/site"

desc 'Copy the javadocs to docs dir'
task 'site:javadocs' do
  javadocs_dir = "#{WORKSPACE_DIR}/target/arez/doc"
  file(javadocs_dir).invoke
  mkdir_p SITE_DIR
  cp_r javadocs_dir, "#{SITE_DIR}/api"
end

desc 'Copy the favicons to docs dir'
task 'site:favicons' do
  favicons_dir = "#{WORKSPACE_DIR}/assets/favicons"
  mkdir_p SITE_DIR
  cp_r Dir["#{favicons_dir}/*.png"], SITE_DIR
  cp_r Dir["#{favicons_dir}/*.json"], SITE_DIR
  cp_r Dir["#{favicons_dir}/*.xml"], SITE_DIR
  cp_r Dir["#{favicons_dir}/*.ico"], SITE_DIR
end

desc 'Build the website'
task 'site:build' do
  rm_rf SITE_DIR
  sh "yarn build #{SITE_DIR}"
  mkdir_p File.dirname(SITE_DIR)
  mv "#{WORKSPACE_DIR}/website/build/arez", SITE_DIR
  task('site:javadocs').invoke
  task('site:favicons').invoke
end

desc 'Check that the website does not have any broken links'
task 'site:link_check' do
  require 'webrick'
  require 'socket'

  # Get a free port and web address
  socket = Socket.new(:INET, :STREAM, 0)
  socket.bind(Addrinfo.tcp('127.0.0.1', 0))
  address = socket.local_address.ip_address
  port = socket.local_address.ip_port
  socket.close

  webserver = WEBrick::HTTPServer.new(:Port => port, :DocumentRoot => SITE_DIR)
  Thread.new {webserver.start}

  trap('INT') {webserver.shutdown}
  begin
    sh "yarn blc --ordered --recursive --filter-level 3 http://#{address}:#{port} --exclude https://github.com/arez/arez/compare/ --exclude https://github.com/arez/arez.github.io/settings --exclude https://docs.oracle.com/javase/8/docs/api"
  ensure
    webserver.shutdown
  end
end

desc 'Serve the website for developing documentation'
task 'site:serve' do
  sh 'yarn start'
end

desc 'Build the website'
task 'site:deploy' => ['site:build'] do
  # Verify the site is valid first
  task('site:link_check').invoke

  # Only publish the site off the master branch if running out of Travis
  if ENV['TRAVIS_BRANCH'].nil? || ENV['TRAVIS_BRANCH'] == 'master'
    origin_url = 'https://github.com/arez/arez.github.io.git'

    travis_build_number = ENV['TRAVIS_BUILD_NUMBER']
    if travis_build_number
      origin_url = origin_url.gsub('https://github.com/', 'git@github.com:')
    end

    in_dir(SITE_DIR) do
      sh 'git init'
      sh 'git add .'
      message =
        travis_build_number.nil? ?
          'Publish website' :
          "Publish website - Travis build: #{travis_build_number}"

      sh "git commit -m \"#{message}\""
      sh "git remote add origin #{origin_url}"
      sh 'git push -f origin master:master'
    end
  end
end
