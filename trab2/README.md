# Trabalho 2 - Ataque de dicionário - JMS

## Docker
```
docker-compose up
```

## Container Java8 e Bluefish
```
docker-compose exec ppd bash -c 'cd /home/ppd/trab2; exec bash'
docker-compose exec ppd-glassfish bash -c 'cd /home/ppd/trab2; exec bash'

```

## Recursos de DEV
### Gerar Source.txt
```
ataque-dicionario-jms-WandersonRalph.zip
```

### Bluefish
```
 asadmin add-resources flie.xml
 asadmin list-jms-resources
```

### Protoc
```
protoc -I=./proto --java_out=./src ./proto/Guess.proto
protoc -I=./proto --java_out=./src ./proto/SubAttack.proto
```

### Ip de Container Docker
```
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ppd-glassfish
```

### Protocol Bufers
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
