/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.brooklyn.entity.software.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.brooklyn.api.entity.EntitySpec;
import org.apache.brooklyn.api.location.Location;
import org.apache.brooklyn.location.jclouds.JcloudsLocation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class VanillaWindowsProcessWinrmStreamsLiveTest extends AbstractSoftwareProcessStreamsTest {
    private Location location;

    @BeforeMethod(alwaysRun=true)
    @Override
    public void setUp() throws Exception {
        super.setUp();

        Map<String, Object> config = ImmutableMap.<String, Object>builder()
                .put("inboundPorts", ImmutableList.of(5985, 3389))
                .put("osFamily", "windows")
                .put("displayName", "AWS Oregon (Windows)")
                .put("imageOwner", "801119661308")
                .put("imageNameRegex", "Windows_Server-2012-R2_RTM-English-64Bit-Base-.*")
                .put("hardwareId", "m3.medium")
                .put("checkRunning.command", "echo true")
                .put("useJcloudsSshInit", false)
                .build();
        location = ((JcloudsLocation)mgmt.getLocationRegistry().resolve("jclouds:aws-ec2:us-west-1", config)).obtain();
    }

    @Test(groups = "Live")
    @Override
    public void testGetsStreams() {
        VanillaWindowsProcess entity = app.createAndManageChild(EntitySpec.create(VanillaWindowsProcess.class)
                .configure(VanillaSoftwareProcess.PRE_INSTALL_COMMAND, "echo " + getCommands().get("pre-install-command"))
                .configure(VanillaSoftwareProcess.INSTALL_COMMAND, "echo " + getCommands().get("^install$"))
                .configure(VanillaSoftwareProcess.POST_INSTALL_COMMAND, "echo " + getCommands().get("post-install-command"))
                .configure(VanillaSoftwareProcess.CUSTOMIZE_COMMAND, "echo " + getCommands().get("^customize$"))
                .configure(VanillaSoftwareProcess.PRE_LAUNCH_COMMAND, "echo " + getCommands().get("pre-launch-command"))
                .configure(VanillaSoftwareProcess.LAUNCH_COMMAND, "echo " + getCommands().get("^launch$"))
                .configure(VanillaSoftwareProcess.POST_LAUNCH_COMMAND, "echo " + getCommands().get("post-launch-command"))
                .configure(VanillaSoftwareProcess.CHECK_RUNNING_COMMAND, "echo true"));
        app.start(ImmutableList.of(location));
        assertStreams(entity);
    }

    @Test(groups = "Live")
    public void testGetsStreamsPowerShell() {
        VanillaWindowsProcess entity = app.createAndManageChild(EntitySpec.create(VanillaWindowsProcess.class)
                .configure(VanillaWindowsProcess.PRE_INSTALL_POWERSHELL_COMMAND, "echo " + getCommands().get("pre-install-command"))
                .configure(VanillaWindowsProcess.INSTALL_POWERSHELL_COMMAND, "echo " + getCommands().get("^install$"))
                .configure(VanillaWindowsProcess.POST_INSTALL_POWERSHELL_COMMAND, "echo " + getCommands().get("post-install-command"))
                .configure(VanillaWindowsProcess.CUSTOMIZE_POWERSHELL_COMMAND, "echo " + getCommands().get("^customize$"))
                .configure(VanillaWindowsProcess.PRE_LAUNCH_POWERSHELL_COMMAND, "echo " + getCommands().get("pre-launch-command"))
                .configure(VanillaWindowsProcess.LAUNCH_POWERSHELL_COMMAND, "echo " + getCommands().get("^launch$"))
                .configure(VanillaWindowsProcess.POST_LAUNCH_POWERSHELL_COMMAND, "echo " + getCommands().get("post-launch-command"))
                .configure(VanillaWindowsProcess.CHECK_RUNNING_POWERSHELL_COMMAND, "echo true"));
        app.start(ImmutableList.of(location));
        assertStreams(entity);
    }

    @Override
    protected Map<String, String> getCommands() {
        return ImmutableMap.<String, String>builder()
                .put("pre-install-command", "myPreInstall")
                .put("^install$", "myInstall")
                .put("post-install-command", "pre install command output")
                .put("^customize$", "myCustomizing")
                .put("pre-launch-command", "pre launch command output")
                .put("^launch$", "myLaunch")
                .put("post-launch-command", "post launch command output")
                .build();
    }
}
