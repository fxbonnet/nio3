package com.github.nio3.tests;

import java.io.IOException;

import com.github.nio3.IOTest;
import com.github.nio3.jdkunsynchronized.JdkUnsynchronizedOutputStreamWriter;
import com.github.nio3.util.ArrayUtil;
import com.github.nio3.util.NullOutputStream;

public class JdkUnsynchronizedOutputStreamWriterTest implements IOTest {
	private int size;
	private long iterations;
	private String src;

	@Override
	public void before(int size, long iterations) {
		this.size = size;
		this.iterations = iterations;
		src = ArrayUtil.string(size);
	}

	@Override
	public void run() throws IOException {
		try (JdkUnsynchronizedOutputStreamWriter dest = new JdkUnsynchronizedOutputStreamWriter(
				new NullOutputStream())) {
			for (long i = 0; i < iterations; i++) {
				dest.write(src);
			}
		}
	}

	@Override
	public String getLabel() {
		return "OutputStreamWriter.write String[" + size + "]";
	}

}
