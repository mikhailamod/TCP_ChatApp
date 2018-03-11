# Makefile for Network Assignment

JAVAC=/usr/bin/javac
JVM = java
JFLAGS = -g
JCLASSDIR = bin/
JSRCDIR = src/
DOCPATH = doc/
JUNITCP = .bin/:junit.jar:hamcrest.jar

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) -cp .:./bin:./src -d $(JCLASSDIR) $(JFLAGS) $*.java

CLASSES = 	src/User.java \
			src/Message.java \
			src/ChatAppServer.java \
			src/ServerThread.java \
			src/ClientThread.java \
			src/ChatAppClient.java
			

# Below: Class files I want the javadocs from
CLASSFILES = bin/ChatAppServer.class
#Below: Java files I want the javadocs from
DOCCLASSES = src/ChatAppServer.java

default: change classes

change:
	@cd src/

classes: $(CLASSES:.java=.class)

doc: $(CLASSFILES)
	javadoc -cp .:./bin -d $(DOCPATH) $(DOCCLASSES)

clean:
	rm $(JCLASSDIR)*.class
