package com.github.nio3;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WriterTest {
	private static final String PATH = "target/test.txt";
	private final Charset UTF8 = Charset.forName("UTF-8");
	private Writer writer;
	private OutputStream fileOutputStream;
	private String tested;
	private final long ITERATIONS = 10000000000l;
	private long start;
	private String test;

	@Before
	public void before() throws FileNotFoundException {
		fileOutputStream = new FileOutputStream(PATH);
		// fileOutputStream = new NullOutputStream();
		start = System.currentTimeMillis();
	}

	@After
	public void after() throws IOException {
		writer.close();
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println(test);
		System.out.println(tested);
		System.out.println("Duration: " + duration + " ms");
		System.out.println();
	}

	private void test1char() throws IOException {
		test = "Writing " + ITERATIONS + " times a char";
		for (long i = 0; i < ITERATIONS; i++)
			writer.write('a');
	}

	private void test1charString() throws IOException {
		test = "Writing " + ITERATIONS + " times a 1 character String";
		for (long i = 0; i < ITERATIONS; i++)
			writer.write("a");
	}

	private void test100charString() throws IOException {
		String string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		long iterations = ITERATIONS / 100;
		test = "Writing " + iterations + " times a 100 characters String";
		for (int i = 0; i < iterations; i++)
			writer.write(string);
	}

	@Test
	@Ignore
	public void test00() throws IOException {
		tested = "new OutputStreamWriter(fileOutputStream, UTF8)";
		writer = new OutputStreamWriter(fileOutputStream, UTF8);
		test1char();
	}

	@Test
	@Ignore
	public void test01() throws IOException {
		tested = "new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8))";
		writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8));
		test1char();
	}

	@Test
	public void test02() throws IOException {
		tested = "new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8), 32768)";
		writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8), 32768);
		test1char();
	}

	@Test
	@Ignore
	public void test03() throws IOException {
		tested = "new OutputStreamWriter(fileOutputStream, UTF8)";
		writer = new OutputStreamWriter(fileOutputStream, UTF8);
		test1charString();
	}

	@Test
	@Ignore
	public void test04() throws IOException {
		tested = "new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8))";
		writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8));
		test1charString();
	}

	@Test
	public void test05() throws IOException {
		tested = "new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8), 32768)";
		writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8), 32768);
		test1charString();
	}

	@Test
	@Ignore
	public void test06() throws IOException {
		tested = "new OutputStreamWriter(fileOutputStream, UTF8)";
		writer = new OutputStreamWriter(fileOutputStream, UTF8);
		test100charString();
	}

	@Test
	@Ignore
	public void test07() throws IOException {
		tested = "new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8))";
		writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8));
		test100charString();
	}

	@Test
	public void test08() throws IOException {
		tested = "new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8), 32768)";
		writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, UTF8), 32768);
		test100charString();
	}

	@Test
	public void test09() throws IOException {
		tested = "new FastOutputStreamWriter(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter(fileOutputStream, UTF8);
		test1char();
	}

	@Test
	public void test10() throws IOException {
		tested = "new FastOutputStreamWriter(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter(fileOutputStream, UTF8);
		test1charString();
	}

	@Test
	public void test11() throws IOException {
		tested = "new FastOutputStreamWriter(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter(fileOutputStream, UTF8);
		test100charString();
	}

	@Test
	public void test12() throws IOException {
		tested = "Files.newBufferedWriter";
		fileOutputStream.close();
		writer = Files.newBufferedWriter(Paths.get(PATH), UTF8);
		test1char();
	}

	@Test
	public void test13() throws IOException {
		tested = "Files.newBufferedWriter";
		fileOutputStream.close();
		writer = Files.newBufferedWriter(Paths.get(PATH), UTF8);
		test1charString();
	}

	@Test
	public void test14() throws IOException {
		tested = "Files.newBufferedWriter";
		fileOutputStream.close();
		writer = Files.newBufferedWriter(Paths.get(PATH), UTF8);
		test100charString();
	}

	@Test
	public void test15() throws IOException {
		tested = "new FastOutputStreamWriter3(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter3(fileOutputStream, UTF8);
		test1char();
	}

	@Test
	public void test16() throws IOException {
		tested = "new FastOutputStreamWriter3(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter3(fileOutputStream, UTF8);
		test1charString();
	}

	@Test
	public void test17() throws IOException {
		tested = "new FastOutputStreamWriter3(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter3(fileOutputStream, UTF8);
		test100charString();
	}

	@Test
	public void test18() throws Exception {
		tested = "new FastOutputStreamWriter3(fileOutputStream, UTF8)";
		writer = new FastOutputStreamWriter3(PATH, UTF8);
		test100charString();
		//Thread.sleep(1000);
	}

}
