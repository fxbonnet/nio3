package com.github.nio3;

import java.text.DecimalFormat;

import com.github.nio3.tests.ArrayCopyTest;
import com.github.nio3.tests.BufferedOutputStreamWriterTest;
import com.github.nio3.tests.ByteArrayOutputStreamInitCapacityTest;
import com.github.nio3.tests.ByteArrayOutputStreamTest;
import com.github.nio3.tests.FastOutputStreamWriterWithCharArrayAndByteChannelTest;
import com.github.nio3.tests.FastOutputStreamWriterWithCharArrayTest;
import com.github.nio3.tests.FastOutputStreamWriterWithCharBufferAndByteChannelTest;
import com.github.nio3.tests.FileOutputStreamTest;
import com.github.nio3.tests.ForLoopArrayCopyTest;
import com.github.nio3.tests.JdkUnsynchronizedByteArrayOutputStreamTest;
import com.github.nio3.tests.JdkUnsynchronizedOutputStreamWriterTest;
import com.github.nio3.tests.OutputStreamWriterTest;

public class IOTester {

	private static void run(IOTest test, int size) throws Exception {
		long iterations = 1000;
		long duration = 0;
		long start;
		long end = 0;
		while (duration < 100) {
			iterations = iterations * 3;
			test.before(size, iterations);
			start = System.currentTimeMillis();
			test.run();
			end = System.currentTimeMillis();
			duration = end - start;
		}
		float throughput = size * iterations / duration * 8 / 1000;
		DecimalFormat format = new DecimalFormat("#,##0");
		System.out.println(test.getLabel() + " -> " + format.format(throughput) + " MB/s");
	}

	private static void run(IOTest test) throws Exception {
		for (int i = 1; i <= 100; i = i * 10) {
			run(test, i);
		}
	}

	public static void main(String[] args) throws Exception {
		run(new ArrayCopyTest());
		run(new ForLoopArrayCopyTest());
		run(new FileOutputStreamTest());
		run(new ByteArrayOutputStreamTest());
		run(new ByteArrayOutputStreamInitCapacityTest());
		run(new JdkUnsynchronizedByteArrayOutputStreamTest());
		run(new OutputStreamWriterTest());
		run(new JdkUnsynchronizedOutputStreamWriterTest());
		run(new BufferedOutputStreamWriterTest());
		run(new FastOutputStreamWriterWithCharArrayTest());
		run(new FastOutputStreamWriterWithCharArrayAndByteChannelTest());
		run(new FastOutputStreamWriterWithCharBufferAndByteChannelTest());
	}

}
