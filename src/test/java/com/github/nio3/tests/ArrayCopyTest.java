package com.github.nio3.tests;

import com.github.nio3.IOTest;
import com.github.nio3.util.ArrayUtil;

public class ArrayCopyTest implements IOTest {
	private int size;
	private long iterations;
	private byte[] src;
	private byte[] dest;

	@Override
	public void before(int size, long iterations) {
		this.size = size;
		this.iterations = iterations;
		src = ArrayUtil.byteArray(size);
		dest = new byte[size];
	}

	@Override
	public void run() {
		for (long i = 0; i < iterations; i++) {
			System.arraycopy(src, 0, dest, 0, size);
		}
	}

	@Override
	public String getLabel() {
		return "System.arrayCopy byte[" + size + "]";
	}

}
