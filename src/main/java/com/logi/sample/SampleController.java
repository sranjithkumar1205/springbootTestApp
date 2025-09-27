package com.logi.sample;

import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
  @GetMapping("/containerInfo")
  public Map<String, String> containerInfo() {
    Map<String, String> info = new HashMap<>();
    String version = "1.0.5";

    // Get container ID from hostname
    String containerId = getHostName();

    // Get container name and status using Docker CLI (requires Docker socket
    // access)
    String containerName = getContainerName(containerId);
    String containerStatus = getContainerStatus(containerId);

    info.put("version", version);
    info.put("containerId", containerId);
    info.put("containerName", containerName);
    info.put("containerStatus", containerStatus);
    return info;
  }

  private String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (Exception e) {
      return "unknown";
    }
  }

  private String getContainerName(String containerId) {
    return execCommand("docker inspect --format='{{.Name}}' " + containerId).replace("/", "");
  }

  private String getContainerStatus(String containerId) {
    return execCommand("docker inspect --format='{{.State.Status}}' " + containerId);
  }

  private String execCommand(String command) {
    System.out.println("Running command: " + command);

    StringBuilder output = new StringBuilder();
    try {
      Process process = Runtime.getRuntime().exec(command);

      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line);
      }

      while ((line = errorReader.readLine()) != null) {
        System.err.println("ERROR: " + line); // Log errors for debugging
      }

      process.waitFor();
      return output.toString().trim();
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
  }
}
