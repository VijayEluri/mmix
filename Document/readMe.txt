1. How do you connect to Internet?
To use internet, execute the following command.
	sudo pon dsl-provider
Input password per prompt; password is {simple}

2. where is java installed?
the folder of java:
/usr/lib/jvm/java-1.5.0-sun/jre/bin/java
make ALT_BOOTDIR=/usr/lib/jvm/java-1.5.0-sun
command
	which java 
can also tell you the path to java.

3. Where is Boost installed?
for boost, located in /usr/local/boost_1_34_1.
g++ -I /usr/local/boost_1_34_1 /home/eddie/CPP/try_boost/Atoi.cpp -o atoi

4. obsolete: where is Sun Studio installed?
PATH=$PATH:/opt/sun/sunstudio12/bin

5. It seems I can not use su again. instealy I should use sudo before every command which need super user privilege.
Answer: we still can use su. sudo is more applicable when you only super user privilege in one command. but if you have a long session as a super user, su is still there for you.

6. How to save disk space?
sudo apt-get clean
it save about 1.3G disk space.

7. Ubuntu/kernel upgrade experience.
After upgradig from 7.04 to 7.10, vi symbolic link was overwritten by Ubuntu. so it is not a good idea to change the symbolic link in system. instead, we should directly use the newly installed application. e.g. use vim directly.


If you are going to 
I am going to san francisco.
Be sure to wear some flowers in your hair.
