
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://github.com/jdelker/tmpbuild-maven-plugin/actions/workflows/ci.yml/badge.svg)](https://github.com/jdelker/tmpbuild-maven-plugin/actions/workflows/ci.yml)

[![GitHub release](https://img.shields.io/github/release/jdelker/tmpbuild-maven-plugin.svg?colorB=brightgreen)](https://github.com/jdelker/tmpbuild-maven-plugin/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jdelker.maven.plugins/tmpbuild-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jdelker.maven.plugins/tmpbuild-maven-plugin)
[![Javadocs](https://www.javadoc.io/badge/com.github.jdelker.maven.plugins/tmpbuild-maven-plugin.svg)](https://www.javadoc.io/doc/com.github.jdelker.maven.plugins/tmpbuild-maven-plugin)

A Maven plugin that provides the ability to transparently divert your build directory
to another location via symbolic links.

See the [CHANGELOG](https://github.com/jdelker/tmpbuild-maven-plugin/blob/master/CHANGELOG.md)
for change information.  

Basic Usage
-----

### Add the plugin to your project

Add the plugin to the build section of your pom's project :

```
<plugin>
  <groupId>com.github.jdelker.maven.plugins</groupId>
  <artifactId>tmpbuild-maven-plugin</artifactId>
  <version>1.0</version>
  <executions>
    <execution>
      <id>link</id>
      <goals>
        <goal>link</goal>
      </goals>
      <phase>initialize</phase>
    </execution>
    <execution>
      <id>clean</id>
      <goals>
        <goal>clean</goal>
      </goals>
      <phase>clean</phase>
    </execution>
  </executions>
</plugin>
```

Motivation
-----

This plugin was crafted for a partciular use-case, where the maven build directory 
(usually `./target`) should be placed on another filesystems, but without manifesting
any specific path in the POM.

A situation where this could come handy is, when the project is located on a slow
or write-sensitive device (e.g. flash-card). By using this plugin, the build directory
is diverted to a given location, without affecting the general functionality of
the project.

Any obvious attempt to achieve this by setting maven's `project.build.directory` 
property, fails unfortunately. Maven does not allow to alter this variable from 
either the command line or through non-project profiles.

Thus, the idea for this plugin, to transparantly divert the build directory through
a plugin.


Documentation
-----
Unless the plugin is configured explicitly for divertion, it behaves completely 
neutral and does not perform any action. This allows to add it as a general plugin
to some Super-POM, even if only some users are actually using it.

As soon as the property `tmpbuild.directory` is set, the plugin creates the project's
build directory at the given location, together with a symbolic link pointing there 
from the original directory.

The property can be set either through the maven command line, like `-Dtmpbuild.directory=/my/superfast/drive`,
or by defining a personal profile in `settings.xml` like so
```
<profile>
  <id>tmpbuild</id>
  <properties>
    <tmpbuild.directory>/my/superfast/drive</tmpbuild.directory>
  </properties>
</profile>
```
and activating that profile either automatically or via `-P tmpbuild`.

**IMPORTANT**
As symbolic links are used, this plugin won't work on Windows out-of-the-box.
Windows NTFS does actually have support for symbolic links, but they are tied to
some restrictions, which makes them hard to use.


Example
-----

Your maven project is located at `/my/slow/drive/myproject`.
The tmpbuild plugin has been added to the `pom.xml` as described above.

If you perform a build as usual, maven creates (and eventually cleans) the build
directory at `/my/slow/drive/myproject/target` - just as always.

Now you build the project again, but set ´tmpbuild.directory=/my/fast/drive`.
During project initialization, the build directory is created at
`/my/fast/drive/target` and linked to `/my/slow/drive/myproject/target`.
The project is then build as usual, but with all files being diverted to the 
fast drive.
