package org.jeasy;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;

import java.util.HashMap;
import java.util.Map;

public class Test {

    @org.junit.Test
    public void t2() {
        FelEngine fel = new FelEngineImpl();
        FelContext ctx = fel.getContext();
        ctx.set("单价", 5000);
        ctx.set("数量", 12);
        ctx.set("运费", 7500);
        Expression exp = fel.compile("单价*数量+运费", ctx);
        Object result = exp.eval(ctx);
        System.out.println(result);
    }

    @org.junit.Test
    public void t3() {
        FelEngine fel = new FelEngineImpl();
        FelContext ctx = fel.getContext();
        Expression compile = fel.compile("user.name", ctx);
        User user = new User(1, "qqq", "中国北京");
        ctx.set("user", user);

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "qqq");
        ctx.set("map", map);
        // 调用user.getName()方法。
        System.out.println(compile.eval(ctx));
        // map.name会调用map.get("name");
        System.out.println(fel.eval("map.name"));
    }

    private static final class User {
        private int id;
        private String name;
        private String address;

        public User() {

        }

        public User(int id, String name, String address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
