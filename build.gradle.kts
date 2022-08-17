plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.darrmirr"
version = project.property("project.version")!!

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("org.junit.jupiter:junit-jupiter-api:5.9.0")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    api("org.slf4j:slf4j-api:1.7.36")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    // https://mvnrepository.com/artifact/org.hamcrest/hamcrest-all
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    // https://mvnrepository.com/artifact/com.h2database/h2
    testImplementation("com.h2database:h2:2.1.214")
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j
    testImplementation("org.apache.logging.log4j:log4j:2.18.0")
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.18.0")
    // https://mvnrepository.com/artifact/org.mockito/mockito-core
    testImplementation("org.mockito:mockito-core:4.7.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            groupId = project.property("group").toString()
            artifactId = project.name
            version = project.property("project.version").toString()

            from(components["java"])
        }
    }
}

/**
 * There are two ways to install extension to maven project:
 *
 * 1. Execute command manually:
 *    - mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=libs/dbchange-1.0.0-SNAPSHOT.jar
 *
 * 2. Add to pom.xml:
 *    <plugin>
 *        <groupId>org.apache.maven.plugins</groupId>
 *        <artifactId>maven-install-plugin</artifactId>
 *        <executions>
 *           <execution>
 *               <id>install-dbchange</id>
 *               <phase>generate-sources</phase>
 *               <goals>
 *                   <goal>install-file</goal>
 *               </goals>
 *               <configuration>
 *                   <file>${basedir}/libs/dbchange-1.0.0-SNAPSHOT.jar</file>
 *               </configuration>
 *          </execution>
 *       </executions>
 *    </plugin>
 */
tasks.jar {
    into("META-INF/maven/$project.group/$project.name") {
        from (tasks.getByName("generatePomFileForDbchangePublication"))
        rename ("pom-default.xml", "pom.xml")
    }
}

