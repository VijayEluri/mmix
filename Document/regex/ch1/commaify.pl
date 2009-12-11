$text = "The population of 281421906 is growing";
$text =~ s/(?<=\d)(?=(\d\d\d)+$)/,/g;
print "$text\n";
$text = "The population of 281421906 is growing";
$text =~ s/(?<=\d)(?=(\d\d\d)+)/,/g;
print "$text\n";
$text = "The population of 281421906 is growing";
$text =~ s/(?<=\d)(?=(\d\d\d)+\b)/,/g;
print "$text\n";
$text = "The population of 281421906 is growing";
$text =~ s/(?<=\d)(?=(\d\d\d)+(?!\d))/,/g;
print "$text\n";
$text = "The population of 281421906";
$text =~ s/(?<=\d)(?=(\d\d\d)+\b)/,/g;
print "$text\n";
$text = "The population of 281421906 is growing";
$text =~ s/(\d)(?=(\d\d\d)+\b)/$1,/g;
print "$text\n";
$text = "The population of 281421906 is growing";
$text =~ s/(\d)((\d\d\d)+\b)/$1,$2/g;
print "$text\n";
$text = "The population of 281421906 is growing";
while ($text =~ s/(\d)((\d\d\d)+\b)/$1,$2/g){
//;
}
print "$text\n";
