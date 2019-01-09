def cleanup_javadocs(project, base_path)
  project.javadoc do
    Dir["#{project.doc.target}/#{base_path}/**/*.html"].each do |f|
      content = IO.read(f, :encoding => 'UTF-8')
      content.gsub!(/@Nonnull<\/a>[ \n\t]+/, '@Nonnull</a> ')
      content.gsub!(/@Nonnull[ \n\t]+/, '@Nonnull ')
      content.gsub!(/@Nullable<\/a>[ \n\t]+/, '@Nullable</a> ')
      content.gsub!(/@Nullable[ \n\t]+/, '@Nullable ')
      IO.write(f, content, :encoding => 'UTF-8')
    end
  end
end
