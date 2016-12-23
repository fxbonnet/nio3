package com.github.nio3.tests;

import java.io.ByteArrayOutputStream;

import com.github.nio3.IOTest;
import com.github.nio3.util.ArrayUtil;

public class ByteArrayOutputStreamTest implements IOTest {
	private int size;
	private long iterations;
	private byte[] src;
	private ByteArrayOutputStream dest;

	@Override
	public void before(int size, long iterations) throws Exception {
		this.size = size;
		this.iterations = iterations;
		src = ArrayUtil.byteArray(size);
		this.dest = new ByteArrayOutputStream();
	}

	@Override
	public void run() throws Exception {
		for (int i = 0; i < iterations; i++) {
			dest.write(src);
		}
	}

	@Override
	public String getLabel() {
		return "ByteArrayOutputStream byte[" + size + "]";
	}

}
