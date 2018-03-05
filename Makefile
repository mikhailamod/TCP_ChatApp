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
	$(JAVAC) -cp .:./bin -d $(JCLASSDIR) $(JFLAGS) $*.java

CLASSES = src/

# Below: Class files I want the javadocs from
CLASSFILES = bin/
#Below: Java files I want the javadocs from
DOCCLASSES = src/

default: classes doc

classes: $(CLASSES:.java=.class)

doc: $(CLASSFILES)
	javadoc -cp .:./bin -d $(DOCPATH) $(DOCCLASSES)

clean:
	rm $(JCLASSDIR)*.class
