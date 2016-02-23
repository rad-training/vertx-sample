package io.vertx.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Whisky {
	private static final AtomicInteger COUNTER = new AtomicInteger();
	private int id;
	private String name;
	private String origin;

	public Whisky() {

	}

	public Whisky(String name, String origin) {
		super();
		this.id = COUNTER.incrementAndGet();
		this.name = name;
		this.origin = origin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getId() {
		return id;
	}

}
