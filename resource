#!/usr/bin/env ruby

require 'rubygems'
require 'nokogiri'
require 'pathname'
require 'colored'
require 'yaml'

class Resourcer

  def initialize(ekylibre_path)
    @ekylibre_path = Pathname.new(File.expand_path(ekylibre_path))
    @root = Pathname.new(File.expand_path("."))
  end

  def do!
    do_procedure_natures!
  end

  private

  def do_procedure_natures!
    puts " * ".green + "Procedure natures"
    for source, dest in {eng: "values", fra: "values-fr"}
      reference = YAML.load_file(@ekylibre_path.join("config", "locales", source.to_s, "nomenclatures.yml"))[source.to_s]["nomenclatures"]["procedure_natures"]
      items = reference["items"].sort{|a,b| a[1] <=> b[1]}
      entries, values = items.map{|x| x[1]}, items.map{|x| x[0]}
      builder = Nokogiri::XML::Builder.new(encoding: 'UTF-8') do |xml|
        xml.resources {
          xml.send("string-array", name: "procedureNatures_entries") {
            for name in entries
              xml.item(name);
            end
          }
          xml.send("string-array", name: "procedureNatures_values") {
            for name in values
              xml.item(name);
            end
          }
        }
      end
      File.write(@root.join("res", dest, "arrays-procedure_natures.xml"), builder.to_xml)
    end

  end
  
end


Resourcer.new("../ekylibre").do!
