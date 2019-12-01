# Information_Security_suk117

TEL 2810 Programming Assignment

## Message Digest

##### Steps to Run
The message_digest.java file is run like every other java file.

##### Input and Output
The user is prompted for an input to hash. The output is a hash using SHA and MD5 of the user input.

## Various Cryptographic Techniques

### Authentication

##### Steps to Run
Run the ProtectedServer.java file first and then the ProtectedClient.java file.

##### Input and Output
The user is prompted for a username and a password. The output lets the user know if they have successfully logged in or not. The password is hardcoded into the Server.

### Signature

##### Steps to Run
Run the ElGamalBob.java file first and then the ElGamalAlice.java file.

##### Input and Output
Alice (Client) prints the signature of the hardcoded message. Bob (Server) prints if the signature of the message is valid or not.

### Encryption

##### Steps to Run
Run the CipherServer.java file first and then the CipherClient.java file.

##### Input and Output
The client prints the DES key generated and stores it in a text file "KeyStore.txt". The Server prints the decrypted plaintext.

### Public Key System

##### Steps to Run
Run the RSA_server.java file first and then the RSA_client.java file.

##### Input and Output
The client prints its Public-Private key pair, generated ciphertext and the signature. The server prints its Public-Private key pair, decrypted plaintext and if the signature is valid or not.

### X.509 Certificate

##### Steps to Run
Run the X509_Server.java first and then the X509_Client.java

##### Input and Output
The client prints the whole certificate, validity period and verifies the signature of the certificate. It also prints the message (unencrypted) and the ciphertext. The server prints the decrypted plaingtext.

### Questions

1. What are the limitations of using a self-signed certificate?  
Self-signed certificates are a risk because they aren't validated by any 3rd party CA.
Users other than yourself would not trust your certificate. They are also hard to maintian and revoke.
2. What are they useful for?  
Self-Signed certificates are cheaper to use than CA signed certificates and when using in a development environment or in a intranet.
They can also be used for personal sites if there are only a few visitors.

### Side Note

##### How to generate the keystore.jks and certificate
DISCLAIMER: Not required to execute for the assignement to run.  
Type the following commands in CMD.  
keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 365 -keysize 2048  
keytool -list -v -keystore keystore.jks  

This creates a keystore.jks file with a self-signed certificate named selfsigned.cer with validity of 356 days from the day of creation.
The second command checks if the first command has run properly.