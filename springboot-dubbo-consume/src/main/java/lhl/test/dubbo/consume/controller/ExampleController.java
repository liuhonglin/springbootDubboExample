package lhl.test.dubbo.consume.controller;

import lhl.test.dubbo.api.example.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HL.L on 2017/6/3.
 */
@RestController
@RequestMapping("example")
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @RequestMapping("hello")
    public String hello(@RequestParam String name) {
        return exampleService.hello(name);
    }
}
