package com.github.nio3.tests;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

import com.github.nio3.FastOutputStreamWriterWithCharArrayAndByteChannel;
import com.github.nio3.IOTest;
import com.github.nio3.util.ArrayUtil;
import com.github.nio3.util.NullOutputStream;

public class FastOutputStreamWriterWithCharArrayAndByteChannelTest implements IOTest {
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
		try (Writer dest = new FastOutputStreamWriterWithCharArrayAndByteChannel(new NullOutputStream(),
				Charset.forName("UTF-8"))) {
			for (long i = 0; i < iterations; i++) {
				dest.write(src);
			}
		}
	}

	@Override
	public String getLabel() {
		return "FastOutputStreamWriterWithCharArrayAndByteChannel.write String[" + size + "]";
	}

}
