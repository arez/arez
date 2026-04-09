require 'buildr/release_tool'

Buildr::ReleaseTool.define_release_task do |t|
  t.extract_version_from_changelog
  t.zapwhite
  t.ensure_git_clean
  t.verify_no_todo
  t.build
  t.patch_changelog('arez/arez-persist')
  t.patch_maven_version_in_readme
  t.tag_project
  t.stage('MavenCentralPublish', 'Publish archive to Maven Central') do
    task('upload_to_maven_central').invoke
  end
  t.stage('DeploySite', 'Deploy the website') do
    task('site:publish_if_tagged').invoke
  end
  t.patch_changelog_post_release
  t.push_changes
  t.github_release('arez/arez-persist')
end
