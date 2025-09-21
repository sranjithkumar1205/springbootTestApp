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
  @GetMapping("/hello")
  public Map<String, String> hello() {
    Map<String, String> info = new HashMap<>();
    String version = "0.0.1";

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
    try {
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      return reader.readLine();
    } catch (IOException e) {
      return "error";
    }
  }
}
