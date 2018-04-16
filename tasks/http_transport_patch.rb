raise 'Patch already integrated into buildr code' unless Buildr::VERSION.to_s == '1.5.5'

class URI::HTTP #:nodoc:
  private
  def connect
    if proxy = proxy_uri
      proxy = URI.parse(proxy) if String === proxy
      http = Net::HTTP.new(host, port, proxy.host, proxy.port, proxy.user, proxy.password)
    else
      http = Net::HTTP.new(host, port)
    end
    if self.instance_of? URI::HTTPS
      require 'net/https'
      http.use_ssl = true
    end
    http.read_timeout = 500
    yield http
  end
end
