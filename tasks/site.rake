WORKSPACE_DIR = File.expand_path(File.dirname(__FILE__) + '/..')
SITE_DIR = "#{WORKSPACE_DIR}/reports/site"

desc 'Copy the javadocs to docs dir'
task 'site:javadocs' do
  javadocs_dir = "#{WORKSPACE_DIR}/target/arez/doc"
  file(javadocs_dir).invoke
  mkdir_p SITE_DIR
  cp_r javadocs_dir, "#{SITE_DIR}/api"
end

desc 'Build the website'
task 'site:build' do
  rm_rf SITE_DIR
  mkdir_p File.dirname(SITE_DIR)
  task('site:javadocs').invoke
  sh "jekyll build --source #{WORKSPACE_DIR}/docs --destination #{SITE_DIR}"
end

desc 'Serve the website for developing documentation'
task 'site:serve' do
  rm_rf SITE_DIR
  mkdir_p File.dirname(SITE_DIR)
  sh "jekyll serve --source #{WORKSPACE_DIR}/docs --destination #{SITE_DIR}"
end

def in_dir(dir)
  current = Dir.pwd
  begin
    Dir.chdir(dir)
    yield
  ensure
    Dir.chdir(current)
  end
end

desc 'Build the website'
task 'site:deploy' => ['site:build'] do
  origin_url = `git remote get-url origin`

  travis_build_number = ENV['TRAVIS_BUILD_NUMBER']
  if travis_build_number
    origin_url = origin_url.gsub('https://github.com/', 'git@github.com:')
  end

  in_dir(SITE_DIR) do
    sh 'git init'
    sh 'git add .'
    sh 'git commit -m "Publish website"'
    sh "git remote add origin #{origin_url}"
    sh 'git push -f origin master:gh-pages'
  end
end
