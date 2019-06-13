package com.practice.spring.jmx;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Luo Bao Ding
 * @since 2019/1/30
 */
@RestController
public class HelloController {
    private SampleBean sampleBean;

    public HelloController(SampleBean sampleBean) {
        this.sampleBean = sampleBean;
    }

    @RequestMapping("/hello")
    public String hello(HttpServletRequest request) throws InterruptedException {
        sampleBean.handleMessage(request.getRequestURI());
//        Thread.sleep(2000);
        return "successful";
    }
}
