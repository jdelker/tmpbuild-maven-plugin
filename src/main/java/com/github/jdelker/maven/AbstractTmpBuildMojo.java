package com.github.jdelker.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.apache.maven.plugin.AbstractMojo;

import java.io.File;
import java.nio.file.Path;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Abstract mojo for Tmp-Build-Plugin
 *
 */
public abstract class AbstractTmpBuildMojo extends AbstractMojo {

  /**
   * Project's build directory
   */
  @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
  protected File directory;

  @Parameter(property = "tmpbuild.directory")
  protected File linkDirectory;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  Path getTargetPath() {
    return linkDirectory == null ? null
            : linkDirectory.toPath()
                    .resolve(project.getGroupId())
                    .resolve(project.getArtifactId())
                    .resolve(project.getBasedir().toPath().relativize(directory.toPath()));
  }
}
