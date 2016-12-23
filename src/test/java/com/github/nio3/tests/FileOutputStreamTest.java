package com.github.nio3.tests;

import java.io.FileOutputStream;

import com.github.nio3.IOTest;
import com.github.nio3.util.ArrayUtil;

public class FileOutputStreamTest implements IOTest {
	private int size;
	private long iterations;
	private byte[] src;
	private FileOutputStream out;

	@Override
	public void before(int size, long iterations) throws Exception {
		this.size = size;
		this.iterations = iterations;
		src = ArrayUtil.byteArray(size);
		out = new FileOutputStream("target/FileWriterTest.txt");
	}

	@Override
	public void run() throws Exception {
		for (int i = 0; i < iterations; i++) {
			out.write(src);
		}
	}

	@Override
	public void after() throws Exception {
		out.close();
	}

	@Override
	public String getLabel() {
		return "FileOutputStream byte[" + size + "]";
	}

}
