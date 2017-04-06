server:
	cd src && javac -cp commons-io-2.5.jar *.java
	mv src/*.class bin
	cd bin && java -cp .:commons-io-2.5.jar ChatServer

client:
	cd bin && java -cp .:commons-io-2.5.jar ChatClient
    
clean:
	cd bin && rm *.class
	cd bin && rm -fr Uploads/

openui:
	cd UI && cd src && javac *.java
	cd UI && cd src && java ChatRoomApp