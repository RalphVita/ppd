JAR_FILES=./libs/imq-5.1.jar:./libs/imqxm-5.1.jar:./libs/javax.jms-api-2.0.1.jar:./libs/jaxm-api-5.1.jar:./libs/protobuf-java-3.11.0.jar:./libs_/imq-5.0.jar
JAVA_PARANS=-Djava.rmi.server.hostname=localhost

all: compila

compila:
	javac  -d bin -sourcepath src -classpath $(JAR_FILES)  @sources.txt

master:
	java $(JAVA_PARANS) -classpath bin:$(JAR_FILES) br.inf.ufes.ppd.Crack.Server.MestreServer

slave:
	java $(JAVA_PARANS) -classpath bin:$(JAR_FILES) br.inf.ufes.ppd.Crack.Client.EscravoClient

client:
	java $(JAVA_PARANS) -classpath bin:$(JAR_FILES) br.inf.ufes.ppd.Crack.Client.Cliente 1

cipher:
	javac -d bin -sourcepath src -classpath $(JAR_FILES) ./src/br/inf/ufes/ppd/Encrypt.java
	java $(JAVA_PARANS) -classpath bin:$(JAR_FILES) br.inf.ufes.ppd.Encrypt Objetivos /home/ppd/trab1/text

decrypt:
	javac -d bin -sourcepath src -classpath $(JAR_FILES) ./src/br/inf/ufes/ppd/Decrypt.java
	java $(JAVA_PARANS) -classpath bin:$(JAR_FILES) br.inf.ufes.ppd.Decrypt none /home/ppd/trab1/text.cipher

testa:
	java $(JAVA_PARANS) -classpath bin:$(JAR_FILES) br.inf.ufes.ppd.Crack.Test.ClienteTest

clean:
	rm -f  *.msg *.cipher bin/*.class *.iml