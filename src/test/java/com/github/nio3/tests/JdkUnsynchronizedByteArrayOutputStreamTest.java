package com.github.nio3.tests;

import com.github.nio3.IOTest;
import com.github.nio3.jdkunsynchronized.JdkUnsynchronizedByteArrayOutputStream;
import com.github.nio3.util.ArrayUtil;

public class JdkUnsynchronizedByteArrayOutputStreamTest implements IOTest {
	private int size;
	private long iterations;
	private byte[] src;
	private JdkUnsynchronizedByteArrayOutputStream dest;

	@Override
	public void before(int size, long iterations) throws Exception {
		this.size = size;
		this.iterations = iterations;
		src = ArrayUtil.byteArray(size);
		this.dest = new JdkUnsynchronizedByteArrayOutputStream();
	}

	@Override
	public void run() throws Exception {
		for (int i = 0; i < iterations; i++) {
			dest.write(src);
		}
	}

	@Override
	public String getLabel() {
		return "Unsynchronized ByteArrayOutputStream byte[" + size + "]";
	}

}
