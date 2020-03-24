package com.abc.invoke.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.abc.invoke.bean.User;
import com.abc.invoke.client.service.UserService;

public class Test {
   public static void main(String[] args) {
       ApplicationContext ac = new ClassPathXmlApplicationContext(
                       "classpath:application-context.xml");
       UserService service = (UserService)ac.getBean("userService");
       User u = service.getUserbyName("Alvis");
       System.out.println(u);
   }
}
