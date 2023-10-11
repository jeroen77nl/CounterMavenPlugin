Gebaseerd op https://www.baeldung.com/maven-plugin

Builden: 
```
> mvn clean install
```

Testen vanaf command line:
```
> ./mvnw com.baeldung:counter-maven-plugin:0.0.1-SNAPSHOT:dependency-counter
```

Gebruiken vanuit een "gewoon" maven project.
Voeg dit aan de pom van het project toe:

<build>
    <plugins>
        <plugin>
            <groupId>com.baeldung</groupId>
            <artifactId>counter-maven-plugin</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <executions>
                <execution>
                    <goals>
                        <goal>dependency-counter</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <scope>test</scope>
            </configuration>
        </plugin>
    </plugins>
</build>

De waarde test onder <configuration><scope> is een parameter die aan de plugin wordt meegegeven.
In dit geval is die bedoeld om te filteren op test dependencies.
De java code in de plugin bevat deze code om die waarde als een stringwaarde op te kunnen pakken:
```java
    @Parameter(property = "scope")
    String scope;
```