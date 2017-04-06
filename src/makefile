server:
	cd src && javac *.java
	mv src/*.class bin
	cd bin && java ChatServer

client:
	cd bin && java ChatClient
    
clean:
	cd bin && rm *

ui:
	cd UI && cd src && javac *.java
	cd UI && cd src && java ChatRoomApp
