/**
 * Copyright (c) 2013, Groupon, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of GROUPON nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * Created with IntelliJ IDEA.
 * User: Dima Kovalenko (@dimacus) && Darko Marinov
 * Date: 5/10/13
 * Time: 4:06 PM
 */

package com.groupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PortChecker {

  public static String getPortInfo(String port) {
    StringBuilder command = new StringBuilder();

    command.append(getCommand());

    if (!port.equals("")) {
      command.append(getParameters());
      command.append(port);
    }

    return ExecuteCommand.execRuntime(command.toString(), true);
  }


  public static String getPidOnPort(String port) {

    Map status = JsonWrapper.parseJson(getPortInfo(port));
    List<String> standardOut = (List<String>) status.get("standard_out");
    if (OSChecker.isWindows()) {
      return "";
    } else {
      return getLinuxPid(standardOut);
    }
  }

  private static String getWindowsPid() {
    System.out.println("Implement me!!!  Port Checkier get windwos PID");
    System.exit(1);
    return "";
  }

  private static String getLinuxPid(List<String> status) {

    for (String line : status) {
      Matcher m = Pattern.compile("java\\s*(\\d*).*(\\(LISTEN\\))").matcher(line);
      if (m.find()) {
        return m.group(1);
      }
    }

    return "";
  }


  private static String getCommand() {
    if (OSChecker.isWindows()) {
      return "netstat -aon ";
    } else {
      return "lsof -i TCP";
    }
  }

  private static String getParameters() {
    if (OSChecker.isWindows()) {
      return " | findstr :";
    } else {
      return ":";
    }
  }

}
