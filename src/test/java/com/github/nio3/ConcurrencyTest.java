package com.github.nio3;

import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ConcurrencyTest {

	@Test
	public void testFileOutputStream() throws Exception {
		byte[] aaa = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n"
				.getBytes();
		byte[] bbb = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\r\n"
				.getBytes();
		FileOutputStream out = new FileOutputStream("target/FileWriterTest.txt");
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 1000000; i++) {
			executorService.submit(() -> {
				try {
					out.write(aaa);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			executorService.submit(() -> {
				try {
					out.write(bbb);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);
		out.close();
	}
}
