all: compila

compila:
	javac -d bin -sourcepath src @sources.txt

master:
	java -classpath bin br.inf.ufes.ppd.Crack.Server.MestreServer

slave:
	java -classpath bin br.inf.ufes.ppd.Crack.Client.EscravoClient

client:
	java -classpath bin br.inf.ufes.ppd.Crack.Client.Cliente /home/ppd/trab1/text.cipher Objetivos

cipher:
	javac -d bin -sourcepath src ./src/br/inf/ufes/ppd/Encrypt.java
	java -classpath bin br.inf.ufes.ppd.Encrypt none /home/ppd/trab1/text

decrypt:
	javac -d bin -sourcepath src ./src/br/inf/ufes/ppd/Decrypt.java
	java -classpath bin br.inf.ufes.ppd.Decrypt none /home/ppd/trab1/text.cipher

testa:
	java -classpath bin br.inf.ufes.ppd.Crack.Test.ClienteTest

clean:
	rm -f  *.msg *.cipher bin/*.class *.iml