plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.30.0"
}

group = "io.github.darrmirr"
version = project.property("project.version")!!

repositories {
    mavenLocal()
    mavenCentral()
}

// versions
val h2Version = "2.1.214"
val hamcrestVersion = "1.3"
val junitVersion = "5.9.0"
val log4jVersion = "2.18.0"
val mockitoVersion = "4.7.0"
val sl4jVersion = "1.7.36"

dependencies {
    api("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    api("org.slf4j:slf4j-api:$sl4jVersion")

    // test scope
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    // https://mvnrepository.com/artifact/org.hamcrest/hamcrest-all
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    // https://mvnrepository.com/artifact/com.h2database/h2
    testImplementation("com.h2database:h2:$h2Version")
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j
    testImplementation("org.apache.logging.log4j:log4j:$log4jVersion")
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    // https://mvnrepository.com/artifact/org.mockito/mockito-core
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
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

// for deployment to Maven Central
publishing {
    publications {
        create<MavenPublication>(project.name) {
            groupId = project.property("group").toString()
            artifactId = project.name
            version = project.property("project.version").toString()

            from(components["java"])

            pom {
                packaging = "jar"
                name.set(project.name)
                url.set(project.property("project.url").toString())
                description.set("Easy and declarative way to execute sql queries in JUnit tests.")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/DarrMirr/dbchange/blob/main/LICENSE")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/DarrMirr/dbchange.git")
                    developerConnection.set("scm:git@github.com:DarrMirr/dbchange.git")
                    url.set("https://github.com/DarrMirr/dbchange")
                }

                developers {
                    developer {
                        id.set("DarrMirr")
                        name.set("Darr Mirr (Vladimir S. Polukeev)")
                        email.set("polukeev.v.s@yandex.ru")
                    }
                }
            }
        }
        repositories {
            maven {
                val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
                credentials {
                    username = project.properties["ossrhUsername"].toString()
                    password = project.properties["ossrhPassword"].toString()
                }
            }
        }
    }
}

// for deployment to Maven Central
// Point to notice: it is mandatory that signing task follow to publishing one
signing {
    sign(publishing.publications[project.name])
}

// for deployment to Maven Central
// after publishing to Sonatype repository it is required to release artifact from private part of repository to publish one
nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    username = project.properties["ossrhUsername"].toString()
    password = project.properties["ossrhPassword"].toString()
}

/**
 * There are two ways to manually install extension to maven project:
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
 *                   <file>${basedir}/libs/dbchange-1.0.0.jar</file>
 *               </configuration>
 *          </execution>
 *       </executions>
 *    </plugin>
 */
tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-URL" to project.property("project.url"),
                "Implementation-Vendor" to project.property("project.developers")))
    }
    // for maven install plugin
    into("META-INF/maven/$project.group/$project.name") {
        from(tasks.getByName("generatePomFileForDbchangePublication"))
        rename("pom-default.xml", "pom.xml")
    }
}