require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|

  s.name         = "react-native-heat-map"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-heat-map
                   DESC
  s.homepage     = "https://github.com/cawfree/react-native-heat-map"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Alex Thomas (@cawfree)" => "hello@cawfree.com" }
  s.platforms    = { :ios => "9.0", :tvos => "10.0" }
  s.source       = { :git => "https://github.com/cawfree/react-native-heat-map.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency 'AFNetworking', '~> 3.0'
  s.dependency 'LFHeatMap', '~> 1.0.2'

end

