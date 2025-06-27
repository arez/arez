require 'buildr/gpg'
require 'net/http'
require 'uri'
require 'net/http/post/multipart'
require 'netrc'

# Recursively get all files in a directory
def get_all_files(dir)
  Dir.glob(File.join(dir, '**', '*'), File::FNM_DOTMATCH).reject do |f|
    File.basename(f) == '.' || File.basename(f) == '..'
  end
end
# Create a zip file from the contents of a directory
def zip_directory(input_dir, output_zip)
  FileUtils.rm_rf output_zip
  input_dir = File.expand_path(input_dir)
  entries = get_all_files(input_dir)

  Zip::File.open(output_zip, Zip::File::CREATE) do |zipfile|
    entries.each do |file|
      next if File.directory?(file)

      # Create relative path for zip entry
      relative_path = file.sub("#{input_dir}/", '')
      zipfile.add(relative_path, file)
    end
  end
end

def upload_signature(pkg)
  artifact = Buildr.artifact(pkg.to_spec_hash.merge(:type => "#{pkg.type}.asc"))
  artifact.from(Buildr::GPG.sign_task(pkg))
  artifact.invoke
  artifact.upload
end

def install_artifacts_to_directory(local_test_repository_dir, project_names)
  local_test_repository_url = "file://#{local_test_repository_dir}"
  FileUtils.rm_rf local_test_repository_dir
  projects_to_upload = Buildr.projects(project_names)
  old_release_to = repositories.release_to
  begin
    # First we install them in a local repository so we don't have to access the network during local builds
    repositories.release_to = local_test_repository_url
    projects_to_upload.each do |prj|
      prj.packages.each do |pkg|
        upload_signature(pkg)
        upload_signature(pkg.pom) if pkg.pom

        pkg.upload
      end
    end
  ensure
    repositories.release_to = old_release_to
  end
end

def upload_to_sonatype(key, version, zip_file)

  # Define the endpoint and parameters
  uri = URI.parse("https://central.sonatype.com/api/v1/publisher/upload?name=#{key}-v#{version}&publishingType=AUTOMATIC")
  upload_io = UploadIO.new(zip_file, "application/zip")
  form_data = { "bundle" => upload_io }


  # Load the data form the ~/.netrc file
  login, password = Netrc.read['central.sonatype.com']
  bearer_token = Base64.strict_encode64("#{login}:#{password}")

  # Create the request
  request = Net::HTTP::Post::Multipart.new(uri.path + '?' + uri.query, form_data)
  request['Authorization'] = "Bearer #{bearer_token}"

  http = Net::HTTP.new(uri.host, uri.port)
  http.use_ssl = true
  response = http.request(request)
  unless response.is_a?(Net::HTTPSuccess)
    raise "Upload failed with status #{response.code}: #{response.body}"
  end
end

desc 'Create Package and deploy to Maven Central'
task 'upload_to_maven_central' do
  workspace_dir = File.expand_path(File.dirname(__FILE__) + '/../')

  local_test_repository_dir = "#{workspace_dir}/target/arez_release_package"
  version = ENV['PRODUCT_VERSION'] || Buildr.project('arez').version
  output_zip = "#{workspace_dir}/target/arez-#{version}.zip"

  # Phase 1 - Put the output in to a directory
  install_artifacts_to_directory(local_test_repository_dir,
                                 %w(arez:core arez:processor arez:extras:dom arez:extras:promise arez:extras:spytools arez:extras:testng))

  # Phase 2 - Zip up the directory ready for release to Maven Central
  zip_directory(local_test_repository_dir, output_zip)
  upload_to_sonatype('arez', version, output_zip)

  # Phase 3 - Upload to Maven Central
  upload_to_sonatype(version, output_zip)
end
