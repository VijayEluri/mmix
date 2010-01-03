1. in home network, proxy is disabled.
fail to clone from 
	git clone http://github.com/ueddieu/mmix.git
but succeed 
	git clone git://github.com/ueddieu/mmix.git
	
	
git push git://github.com/ueddieu/mmix 
--it is only for read only access
$ git push git://github.com/ueddieu/mmix
fatal: remote error:
  You can't push to git://github.com/ueddieu/mmix.git
  Use git@github.com:ueddieu/mmix.git
  
$ git push git@github.com:ueddieu/mmix.git
Permission denied (publickey).
fatal: The remote end hung up unexpectedly

--we need to regist our public key in github first.  

ssh-keygen
just enter again and again.

add your key to git hub on 
	https://github.com/account
	
	
then push again.
git push git@github.com:ueddieu/mmix.git

you win.
Merge git://github.com/ueddieu/mmix 

2. in company network, proxy is enabled. so this proxy setting is
necessary for SVN and GIT.

export http_proxy="http://web-proxy.fc.hp.com:8080"
git config --global http_proxy="http://web-proxy.fc.hp.com:8080"

3. In company and home, I can use the following servers.
testdl380d.cup.hp.com siu

	export http_proxy="http://web-proxy.fc.hp.com:8080"
	git clone http://github.com/ueddieu/mmix.git
	Initialized empty Git repository in /home/seagull/mmix/.git/


4. install cweb.
ftp://ftp.cs.stanford.edu/pub/cweb/cweb.tar.gz
curl ftp://ftp.cs.stanford.edu/pub/cweb/cweb.tar.gz \
-o cweb.tar.gz


