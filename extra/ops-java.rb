#!/usr/bin/env ruby
# Generate Java opcode table
in_vminst = false
instructions = Hash.new
STDIN.each do |line|
  if /^enum vminst {/ =~ line
    in_vminst = true;
  end
  next if !in_vminst
  if /^\s+(i.*)=([0-9]+)/ =~ line
    instructions[$2.to_i] = ($1.clone[1..-1]).upcase
  elsif /^};$/ =~ line
    break
  end
end

puts "public enum Op {"
0.upto(255) do |opcode|
  if instructions.has_key?(opcode)
    puts "   #{instructions[opcode]}(#{"0x%02x" % opcode}),"
  end
end
puts "}"
