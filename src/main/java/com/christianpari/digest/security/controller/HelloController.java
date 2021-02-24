package com.christianpari.digest.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/hello/{name}")
  public String sayHello(
    @PathVariable(required = false) String name
  ) {
    return "Hello " + name;
  }

  @GetMapping("/admin/hello/{name}")
  public String sayAdminHello(
    @PathVariable(required = false) String name
  ) {
    return "Hello Admin " + name;
  }
}
