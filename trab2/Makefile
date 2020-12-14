JAR_FILES=./libs/javax.jms-api-2.0.1.jar:./libs/imq-5.0.jar:protobuf-java-4.0.0-rc-2.jar:./libs/protobuf-gradle-plugin-0.8.14.jar

all: compila

compila:
	javac  -d bin -sourcepath src -classpath $(JAR_FILES)  @sources.txt

exec:
	java -classpath bin:./libs/javax.jms-api-2.0.1.jar:./libs/imq-5.0.jar br.ufes.inf.nemo.chatclient.Chat

master:
	java -classpath bin br.inf.ufes.ppd.Crack.Server.MestreServer

slave:
	java -classpath bin br.inf.ufes.ppd.Crack.Client.EscravoClient

client:
	java -classpath bin br.inf.ufes.ppd.Crack.Client.Cliente /home/ppd/trab1/text.cipher Objetivos

cipher:
	javac -d bin -sourcepath src ./src/br/inf/ufes/ppd/Encrypt.java
	java -classpath bin br.inf.ufes.ppd.Encrypt Objetivos /home/ppd/trab1/text

decrypt:
	javac -d bin -sourcepath src ./src/br/inf/ufes/ppd/Decrypt.java
	java -classpath bin br.inf.ufes.ppd.Decrypt none /home/ppd/trab1/text.cipher