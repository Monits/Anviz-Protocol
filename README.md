# Anviz TCP Protocol

This library allows you to communicate with your Anviz device from Java. Right now it supports a limited set of features, but is easily extensible to suit your needs.

# Adding the dependency

We use [Maven](http://maven.apache.org/) for building & distributing our libraries. You're welcome to use our Maven repositories, or build your own .jar.

To use our Maven repos just add:

    <repositories>
        <repository>
            <id>monits-snapshots</id>
            <url>http://nexus.monits.com/content/repositories/oss-snapshots/</url>
            <name>Monits Snapshots</name>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.monits</groupId>
            <artifactId>anviz-protocol</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

To build a .jar from source:

>
> mvn clean install
>

# Usage

Just check out the `TimeKeeper` class, it doesn't hold any secrets.

# Contributing

We encourage you to contribute to this project! 

We are also looking forward to your bug reports, feature requests and questions.

# Copyright and License

Copyright 2012 Monits.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License at: 

http://www.apache.org/licenses/LICENSE-2.0

