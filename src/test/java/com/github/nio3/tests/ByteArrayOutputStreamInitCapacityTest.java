package com.github.nio3.tests;

import java.io.ByteArrayOutputStream;

import com.github.nio3.IOTest;
import com.github.nio3.util.ArrayUtil;

public class ByteArrayOutputStreamInitCapacityTest implements IOTest {
	private int size;
	private long iterations;
	private long initialSize;
	private byte[] src;
	private ByteArrayOutputStream dest;

	@Override
	public void before(int size, long iterations) throws Exception {
		this.size = size;
		this.iterations = iterations;
		src = ArrayUtil.byteArray(size);
		initialSize = size * iterations;
		if (initialSize > (long) Integer.MAX_VALUE)
			initialSize = (long) Integer.MAX_VALUE;
		this.dest = new ByteArrayOutputStream((int) initialSize);
	}

	@Override
	public void run() throws Exception {
		for (int i = 0; i < iterations; i++) {
			dest.write(src);
		}
	}

	@Override
	public String getLabel() {
		return "ByteArrayOutputStream(" + initialSize + ") byte[" + size + "]";
	}

}
