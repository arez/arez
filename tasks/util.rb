WORKSPACE_DIR = File.expand_path(File.dirname(__FILE__) + '/..')

DOWNSTREAM_PROJECTS=%w(arez-browserlocation arez-idlestatus arez-networkstatus arez-promise arez-spytools arez-ticker arez-timeddisposer arez-when)

def in_dir(dir)
  current = Dir.pwd
  begin
    Dir.chdir(dir)
    yield
  ensure
    Dir.chdir(current)
  end
end
