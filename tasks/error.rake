require File.expand_path(File.dirname(__FILE__) + '/util')

def load_diagnostic_messages
  JSON.load(IO.read("#{WORKSPACE_DIR}/core/src/test/java/arez/diagnostic_messages.json"))
end

desc 'Print out a list of all error codes unused used in codebase'
task 'error_codes:print_unused' do
  keys = load_diagnostic_messages.collect{|m|m['code']}.sort
  max_value = keys.last.to_i
  1.upto(max_value).each do |v|
    unless keys.delete(v)
      puts "Arez-#{'%04d' % v} unused"
    end
  end
end

desc 'Print out new error_code that has not yet been used'
task 'error_codes:print_new_error_code' do
  puts "Arez-#{sprintf('%04d', load_diagnostic_messages.collect{|m|m['code']}.sort.last.to_i + 1)}"
end

desc 'Print out next error code. Reusing any that have been retired.'
task 'error_codes:next' do
  found = false
  keys = load_diagnostic_messages.collect{|m|m['code']}.sort
  max_value = keys.last.to_i
  1.upto(max_value).each do |v|
    unless keys.delete(v)
      puts "Arez-#{'%04d' % v} (previously used)"
      found = true
      break
    end
  end
  puts "Arez-#{sprintf('%04d',max_value + 1)} (new)" unless found
end
