JFLAGS = -d .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        src/fibheap/Node.java \
        src/fibheap/FibonacciHeap.java \
        src/fibheap/FibonacciHeapImpl.java \
        src/fibheap.KeywordCounter.java

default: classes
	@echo Finished compiling
	@echo Run using - "java fibheap.KeywordCounter <input_file>"

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	rm -rf fibheap/

