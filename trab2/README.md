# Trabalho 1 - Aaque de dicion�rio

```
docker-compose up

docker-compose exec ppd bash -c 'cd /home/ppd/trab2; exec bash'
docker-compose exec ppd-glassfish bash -c 'cd /home/ppd/trab2; exec bash'

 find . -name *.java > sources.txt


 asadmin add-resources 
 asadmin list-jms-resources
```

docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ppd-glassfish



javac -classpath ./libs/javax.jms-api-2.0.1.jar:./libs/imq-5.0.jar br/ufes/inf/nemo/chatclient/Chat.java

javac  -d bin -sourcepath src -classpath ./libs/javax.jms-api-2.0.1.jar:./libs/imq-5.0.jar ./src/br/ufes/inf/nemo/chatclient/Chat.java




java -classpath bin:./src/javax.jms-api-2.0.1.jar:./src/imq-5.0.jar br.ufes.inf.nemo.chatclient.Chat

java -classpath bin:./src/javax.jms-api-2.0.1.jar:./src/imq-5.0.jar br.ufes.inf.nemo.chatclient.Chat


## Protocol Bufers
https://askubuntu.com/questions/1072683/how-can-i-install-protoc-on-ubuntu-16-04
```
sudo apt-get install autoconf automake libtool curl make g++ unzip
./configure
make
make check
sudo make install
sudo ldconfig # refresh shared library cache.
```

protoc -I=./protoc --java_out=./src ./protoc/teste.proto


mvn archetype:generate -DartifactId=trab -DgroupId=br.inf.ufes.ppd.Crack -DinteractiveMode=false -DarchetypeArtifactId=maven-archetype-quickstart
