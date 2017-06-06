package lhl.test.dubbo.consume.controller;

import lhl.test.dubbo.api.example.ExampleService;
import lhl.test.dubbo.consume.utils.InternetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by HL.L on 2017/6/3.
 */
@RestController
@RequestMapping("example")
public class ExampleController {

    private Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @Autowired
    private ExampleService exampleService;

    @RequestMapping("hello")
    public String hello(@RequestParam String name, HttpServletRequest request) {
        String ip = InternetUtil.getIP(request);
        logger.info(ip);
        try {
            if ( InternetUtil.acquire(ip, "1") ) {
                return exampleService.hello(name);
            } else {
                return "limited.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "limited.";
    }


}
