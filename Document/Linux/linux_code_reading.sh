LNX=/home/eddie/linux-2.6
CSCOPE=/home/eddie/cscope
cd $LNX
echo $LNX 	
find  .                                                           \
-path "./arch/*" ! -path "./arch/x86*" -prune -o               \
-path "./fs/*/*" ! -path "./fs/ext3*" -prune -o               \
-path "./net/*/*" ! -path "./net/ipv4*" -prune -o               \
-path "./tmp*" -prune -o                                           \
-path "./Documentation*" -prune -o                                 \
-path "./scripts*" -prune -o                                       \
-name "*.[chxsS]" -print \
> $CSCOPE/cscope.files
