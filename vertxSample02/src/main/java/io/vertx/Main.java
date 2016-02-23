package io.vertx;

import io.vertx.core.Vertx;

public class Main {

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();

		vertx.deployVerticle("io.vertx.MyRestVerticle");
		System.out.println("verticle deployed");
	}
}
