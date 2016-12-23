package com.github.nio3;

public interface IOTest {
	public void before(int size, long iterations) throws Exception;

	public void run() throws Exception;

	default public void after() throws Exception {
	};

	public String getLabel();
}
