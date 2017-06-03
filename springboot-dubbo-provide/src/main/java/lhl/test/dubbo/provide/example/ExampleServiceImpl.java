package lhl.test.dubbo.provide.example;

import lhl.test.dubbo.api.example.ExampleService;
import org.springframework.stereotype.Service;

/**
 * Created by HL.L on 2017/6/3.
 */
@Service(value="exampleServiceImpl")
public class ExampleServiceImpl implements ExampleService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
