package com.clover.redis.gui.test;

import redis.clients.jedis.Jedis;

public class Test {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("127.0.0.1", 16379);
		jedis.auth("zhangdq");
		System.out.println(jedis.ping());
		System.out.println(jedis.getDB());
		System.out.println(jedis.configGet("databases").get(1));
	}
}
