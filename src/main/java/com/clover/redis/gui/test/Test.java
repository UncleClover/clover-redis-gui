package com.clover.redis.gui.test;

import java.util.Set;

import redis.clients.jedis.Jedis;

public class Test {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("127.0.0.1", 16379);
		jedis.auth("zhangdq");
//		System.out.println(jedis.ping());
//		System.out.println(jedis.getDB());
//		System.out.println(jedis.configGet("databases").get(1));
		
		jedis.select(0);
		Set<String> keys = jedis.keys("*");
//		jedis.type("name");
		System.out.println(keys);
//		
//		System.out.println(jedis.hlen("name"));
		
		
		// String
		System.out.println("String = " + jedis.get("name"));
		
		// hash
		System.out.println("hash = " + jedis.hgetAll("user"));
		
		// list
		System.out.println("list = " + jedis.lrange("car", 0, 10));
		
		// set
		System.out.println("set = " + jedis.smembers("plant"));
	}
}
