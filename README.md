[![Build Status](https://travis-ci.org/difi/asic.svg?branch=master)](https://travis-ci.org/difi/asic)
[![CodeCov](https://codecov.io/gh/difi/asic/branch/master/graph/badge.svg)](https://codecov.io/gh/difi/asic)
[![Maven Central](https://img.shields.io/maven-central/v/no.difi.commons/commons-asic.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22no.difi.commons%22%20AND%20a%3A%22commons-asic%22)

# Associated Signature Container (ASiC)

An ASiC file is simply a ZIP archive created according to some rules set forth in the specifications. 

The benefits of using containers for message transfer are:
* all files are kept together as a single collection.
* very efficient with regards to space.
* due to the compressed format, communication bandwidth is utilized better
* message integrity is provided, using message digests and signatures.
* confidentiality is provided by encryption using AES-256 in GCM mode

This component provides an easy-to-use factory for creating ASiC-E containers.

Conformance is claimed according to 7.2.1 (TBA) and 7.2.2 in
[ETSI TS 102 918 V1.3.1](http://webapp.etsi.org/workprogram/Report_WorkItem.asp?WKI_ID=42455).

## Documentation

* [Conformance](doc/conformance.md)
* [Creating an ASiC-E container manually using OpenSSL](doc/openssl.md)

## Maven

```xml
<dependency>
	<groupId>no.difi.commons</groupId>
	<artifactId>commons-asic</artifactId>
	<version>0.9.2</version>
</dependency>
```


## What does it look like?

In general the archive looks something like depicted below 

```
asic-container.asice: 
   |
   +-- mimetype
   |
   +-- bii-envelope.xml
   |
   +-- bii-document.xml
   |
   +-- META-INF/
          |
          + asicmanifest.xml
          |
          + signature.p7s   
   
```

Consult the [AsicCadesContainerWriterTest](src/test/java/no/difi/asic/AsicWriterTest.java) for sample usage.
Here is a rough sketch on how to do it:
```java
// Creates an ASiC archive after which every entry is read back from the archive.

// Name of the file to hold the the ASiC archive
File archiveOutputFile = new File(System.getProperty("java.io.tmpdir"), "asic-sample-default.zip");

// Creates an AsicWriterFactory with default signature method
AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();

// Creates the actual container with all the data objects (files) and signs it.
AsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutputFile)
        // Adds an ordinary file, using the file name as the entry name
        .add(biiEnvelopeFile)
                // Adds another file, explicitly naming the entry and specifying the MIME type
        .add(biiMessageFile, BII_MESSAGE_XML, MimeType.forString("application/xml"))
                // Signing the contents of the archive, closes it for further changes.
        .sign(keystoreFile, TestUtil.keyStorePassword(), TestUtil.privateKeyPassword());

// Opens the generated archive and reads each entry
AsicReader asicReader = AsicReaderFactory.newFactory().open(archiveOutputFile);

String entryName;

// Iterates over each entry and writes the contents into a file having same name as the entry
while ((entryName = asicReader.getNextFile()) != null) {
    log.debug("Read entry " + entryName);
    
    // Creates file with same name as entry
    File file = new File(entryName);
    // Ensures we don't overwrite anything
    if (file.exists()) {
        throw new IllegalStateException("File already exists");
    }
    asicReader.writeFile(file);
    
    // Removes file immediately, since this is just a test 
    file.delete();  
}
asicReader.close(); 
```


## Security

This library validate signatures, but does not validate the certificate. It's up to the implementer using the library
to choose if and how to validate certificates. Certificate(s) used for validation is exposed by the library.
