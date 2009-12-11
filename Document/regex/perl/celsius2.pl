#!/usr/bin/perl
$celsius = 20;
while ($celsius <= 45)
{
  $fahrenheit = ($celsius * 9 / 5) + 32; # calculate Fahrenheit
  print "$celsius C is $fahrenheit F.\n";
  $celsius = $celsius + 5;
}
