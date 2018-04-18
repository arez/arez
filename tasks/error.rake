require File.expand_path(File.dirname(__FILE__) + '/util')

def load_error_codes
  error_map = {}

  `cd #{WORKSPACE_DIR} && git ls-files`.split("\n").
    select {|f| f =~ /\.java$/}.
    select {|f| !(f =~ /[\/\\]src[\/\\]test[\/\\]/)}.
    each do |f|
    content = IO.read(f)
    matches = content.scan(/"Arez\-(\d\d\d\d): /)
    matches.each do |match|
      match = match.first
      if error_map[match]
        raise "Duplicate error code used - Arez-#{match}.\n First use: #{error_map[match]}\nSecond use: #{f}"
      else
        error_map[match] = f
      end
    end
  end
  error_map
end

desc 'Verify that there are no duplicate error numbers'
task 'error_codes:check_duplicates' do
  # Scanning them is sufficient to check for duplicates
  load_error_codes
end

desc 'Print out a list of all error codes used in codebase'
task 'error_codes:print' do
  error_codes = load_error_codes
  puts load_error_codes.keys.sort.collect{|k| "Arez-#{k}: #{error_codes[k]}"}.join("\n")
end

desc 'Print out a list of all error codes unused used in codebase'
task 'error_codes:print_unused' do
  keys = load_error_codes.keys.sort
  max_value = keys.last.to_i
  1.upto(max_value).collect{|v| '%04d' % v }.each do |v|
    unless keys.delete(v.to_s)
      puts "Arez-#{v} unused"
    end
  end
end

desc 'Print out next error_code'
task 'error_codes:print_next' do
  puts "Arez-#{sprintf('%04d',load_error_codes.keys.sort.last.to_i + 1)}"
end
