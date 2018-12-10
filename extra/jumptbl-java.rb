#!/usr/bin/env ruby
# Generate Java jumptbl. Feed this Op.java
#
in_vminst = false
instructions = Hash.new
STDIN.each do |line|
  if /^public enum Op {/ =~ line
    in_vminst = true;
  end
  next if !in_vminst
  if /^\s+([A-Z0-9]+)\(0x([0-9a-f]{2})/ =~ line
    instructions[$2.to_i(16)] = ($1.clone[0..-1]).upcase
  end
  break if /.*;$/ =~ line
end
puts "	private static final Instruction[] jmptbl = {"
0.upto(255) do |opcode|
  if instructions.has_key?(opcode)
    puts "		new #{instructions[opcode]}(),\t\t// #{"0x%02x" % opcode}"
  else
    puts "		NOINST,"
  end
end
puts "	};"

