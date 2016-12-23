package com.github.nio3;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

import com.github.nio3.jdkunsynchronized.JdkUnsynchronizedByteArrayOutputStream;
import com.github.nio3.jdkunsynchronized.JdkUnsynchronizedOutputStreamWriter;
import com.github.nio3.util.ArrayUtil;
import com.github.nio3.util.NullOutputStream;

public class ThroughputTest {
	private final static char CHAR = 'a';
	private final static String STRING1 = "" + CHAR;
	private final static String STRING100;

	static {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100; i++)
			sb.append(CHAR);
		STRING100 = sb.toString();
	}

	private final static String STRING1000;

	static {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++)
			sb.append(CHAR);
		STRING1000 = sb.toString();
	}

	private final static String STRING10000;

	static {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10000; i++)
			sb.append(CHAR);
		STRING10000 = sb.toString();
	}

	private final static char[] CHAR100 = STRING100.toCharArray();
	private final static byte[] BYTE100 = STRING100.getBytes();
	private static final byte[] BYTE1000 = STRING1000.getBytes();
	private static final byte[] BYTE10000 = STRING10000.getBytes();
	private final static int INT = Character.getNumericValue(CHAR);
	private final static byte[] BYTE1 = "a".getBytes();

	@Test
	public void testByteArrayOutputStream() throws Exception {
		ThroughputTimer test = new ThroughputTimer("ByteArrayOutputStream 1 byte", 200000000).start();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < test.getSampleSize(); i++) {
			out.write(INT);
		}
		test.stop();
	}

	@Test
	public void testByteArrayOutputStreamCapacity() throws Exception {
		ThroughputTimer test = new ThroughputTimer("ByteArrayOutputStream(200000000) 1 byte", 200000000).start();
		ByteArrayOutputStream out = new ByteArrayOutputStream(200000000);
		for (int i = 0; i < test.getSampleSize(); i++) {
			out.write(INT);
		}
		test.stop();
	}

	@Test
	public void testUnsynchronizedByteArrayOutputStream() throws Exception {
		try (JdkUnsynchronizedByteArrayOutputStream out = new JdkUnsynchronizedByteArrayOutputStream()) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized ByteArrayOutputStream 1 byte", 500000000)
					.start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(INT);
			}
			test.stop();
		}
	}

	@Test
	public void testUnsynchronizedByteArrayOutputStreamCapacity() throws Exception {
		try (JdkUnsynchronizedByteArrayOutputStream out = new JdkUnsynchronizedByteArrayOutputStream(200000000)) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized ByteArrayOutputStream(200000000) 1 byte",
					500000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(INT);
			}
			test.stop();
		}
	}

	@Test
	public void testArrayCopy(int arraySize) throws Exception {
		long sampleSize = 10000000l * arraySize;
		byte[] src = ArrayUtil.byteArray(arraySize);
		byte[] dest = new byte[arraySize];
		ThroughputTimer test = new ThroughputTimer("System.arrayCopy byte[" + arraySize + "]", sampleSize).start();
		long iterations = sampleSize / arraySize;
		for (long i = 0; i < iterations; i++) {
			System.arraycopy(src, 0, dest, 0, arraySize);
		}
		test.stop();
	}

	@Test
	public void testFileCopy() throws Exception {
		int sampleSize = 1000000000;
		try (FileOutputStream out = new FileOutputStream("target/FileWriterTest.txt")) {
			for (int i = 0; i < sampleSize / BYTE100.length; i++) {
				out.write(BYTE100);
			}
		}
		ThroughputTimer test = new ThroughputTimer("File copy", sampleSize).start();
		Files.copy(Paths.get("target/FileWriterTest.txt"), Paths.get("target/FileWriterTest.txt"));
		test.stop();
	}

	@Test
	public void testFileOutputStream(int arraySize) throws Exception {
		byte[] src = ArrayUtil.byteArray(arraySize);
		int sampleSize = 100000000;
		long iterations = sampleSize / arraySize;
		try (FileOutputStream out = new FileOutputStream("target/FileWriterTest.txt")) {
			ThroughputTimer test = new ThroughputTimer("FileOutputStream byte[" + arraySize + "]", sampleSize).start();
			for (int i = 0; i < iterations; i++) {
				out.write(src);
			}
			test.stop();
		}
	}

	@Test
	public void testFileOutputStreamChannel1() throws Exception {
		try (FileOutputStream out = new FileOutputStream("target/FileWriterTest.txt");
				FileChannel fc = out.getChannel()) {
			ByteBuffer bf = ByteBuffer.wrap(BYTE1);
			ThroughputTimer test = new ThroughputTimer("FileOutputStream.getChannel() byte[1]", 2000000).start();
			for (int i = 0; i < test.getSampleSize() / BYTE1.length; i++) {
				fc.write(bf);
				bf.rewind();
			}
			test.stop();
		}
	}

	@Test
	public void testFileOutputStreamChannel100() throws Exception {
		try (FileOutputStream out = new FileOutputStream("target/FileWriterTest.txt");
				FileChannel fc = out.getChannel()) {
			ByteBuffer bf = ByteBuffer.wrap(BYTE100);
			ThroughputTimer test = new ThroughputTimer("FileOutputStream.getChannel() byte[100]", 2000000).start();
			for (int i = 0; i < test.getSampleSize() / BYTE100.length; i++) {
				fc.write(bf);
				bf.rewind();
			}
			test.stop();
		}
	}

	@Test
	public void testAsynchronousFileChannel1() throws Exception {
		try (AsynchronousFileChannel afc = AsynchronousFileChannel.open(Paths.get("target/FileWriterTest.txt"),
				StandardOpenOption.WRITE)) {
			ByteBuffer bf = ByteBuffer.wrap(BYTE1);
			long pos = 0l;
			ThroughputTimer test = new ThroughputTimer("AsynchronousFileChannel byte[1]", 2000000).start();
			for (int i = 0; i < test.getSampleSize() / BYTE1.length; i++) {
				afc.write(bf, pos);
				pos++;
				bf.rewind();
			}
			test.stop();
		}
	}

	@Test
	public void testAsynchronousFileChannel100() throws Exception {
		try (AsynchronousFileChannel afc = AsynchronousFileChannel.open(Paths.get("target/FileWriterTest.txt"),
				StandardOpenOption.WRITE)) {
			ByteBuffer bf = ByteBuffer.wrap(BYTE100);
			long pos = 0l;
			ThroughputTimer test = new ThroughputTimer("AsynchronousFileChannel byte[100]", 2000000).start();
			for (int i = 0; i < test.getSampleSize() / BYTE100.length; i++) {
				afc.write(bf, pos);
				pos++;
				bf.rewind();
			}
			test.stop();
		}
	}

	@Test
	public void testOutputStreamWriter1() throws Exception {
		try (OutputStreamWriter out = new OutputStreamWriter(new NullOutputStream())) {
			ThroughputTimer test = new ThroughputTimer("OutputStreamWriter char", 50000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(CHAR);
			}
			test.stop();
		}
	}

	@Test
	public void testOutputStreamWriter100() throws Exception {
		try (OutputStreamWriter out = new OutputStreamWriter(new NullOutputStream())) {
			ThroughputTimer test = new ThroughputTimer("OutputStreamWriter String 100", 5000000).start();
			for (int i = 0; i < test.getSampleSize() / 100; i++) {
				out.write(STRING100);
			}
			test.stop();
		}
	}

	@Test
	public void testUnsynchronizedOutputStreamWriter1() throws Exception {
		try (JdkUnsynchronizedOutputStreamWriter out = new JdkUnsynchronizedOutputStreamWriter(
				new NullOutputStream())) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized OutputStreamWriter char", 50000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(CHAR);
			}
			test.stop();
		}
	}

	@Test
	public void testUnsynchronizedOutputStreamWriter100() throws Exception {
		try (JdkUnsynchronizedOutputStreamWriter out = new JdkUnsynchronizedOutputStreamWriter(
				new NullOutputStream())) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized OutputStreamWriter String 100", 50000000)
					.start();
			for (int i = 0; i < test.getSampleSize() / 100; i++) {
				out.write(STRING100);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithCharArrayAndByteChannel1() throws Exception {
		try (FastOutputStreamWriterWithCharArrayAndByteChannel out = new FastOutputStreamWriterWithCharArrayAndByteChannel(
				new NullOutputStream(), Charset.forName("UTF-8"))) {
			ThroughputTimer test = new ThroughputTimer(
					"Unsynchronized OutputStreamWriter with ByteChannel with buffer[8192] char", 50000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(CHAR);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithCharArrayAndByteChannel100() throws Exception {
		try (FastOutputStreamWriterWithCharArrayAndByteChannel out = new FastOutputStreamWriterWithCharArrayAndByteChannel(
				new NullOutputStream(), Charset.forName("UTF-8"))) {
			ThroughputTimer test = new ThroughputTimer(
					"Unsynchronized OutputStreamWriter with ByteChannel buffer[8192] String 100", 50000000).start();
			for (int i = 0; i < test.getSampleSize() / 100; i++) {
				out.write(STRING100);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithBigCharArrayAndByteChannel1() throws Exception {
		try (FastOutputStreamWriterWithCharArrayAndByteChannel out = new FastOutputStreamWriterWithCharArrayAndByteChannel(
				new NullOutputStream(), Charset.forName("UTF-8"), 32768)) {
			ThroughputTimer test = new ThroughputTimer(
					"Unsynchronized OutputStreamWriter with ByteChannel with buffer[32768] char", 50000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(CHAR);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithBigCharArrayAndByteChannel100() throws Exception {
		try (FastOutputStreamWriterWithCharArrayAndByteChannel out = new FastOutputStreamWriterWithCharArrayAndByteChannel(
				new NullOutputStream(), Charset.forName("UTF-8"), 32768)) {
			ThroughputTimer test = new ThroughputTimer(
					"Unsynchronized OutputStreamWriter with ByteChannel with buffer[32768] String 100", 50000000)
							.start();
			for (int i = 0; i < test.getSampleSize() / 100; i++) {
				out.write(STRING100);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithCharArray1() throws Exception {
		// Less efficient than ByteChannel as an OutputStream.write writes bytes
		// one by one calling write(int) and ByteChannel does a direct copy
		// FileChannel could in theory do a direct copy but FileOutputStream has
		// a native writeBytes so we should have a look
		// Default Output method implementations are shitty
		// write(byte[])->write(int)
		try (FastOutputStreamWriterWithCharArray out = new FastOutputStreamWriterWithCharArray(new NullOutputStream(),
				Charset.forName("UTF-8"), 32768)) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized OutputStreamWriter with buffer[8192] char",
					50000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(CHAR);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithCharArray100() throws Exception {
		try (FastOutputStreamWriterWithCharArray out = new FastOutputStreamWriterWithCharArray(new NullOutputStream(),
				Charset.forName("UTF-8"))) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized OutputStreamWriter with buffer[8192] String 100",
					50000000).start();
			for (int i = 0; i < test.getSampleSize() / 100; i++) {
				out.write(STRING100);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithBigCharArray1() throws Exception {
		try (FastOutputStreamWriterWithCharArray out = new FastOutputStreamWriterWithCharArray(new NullOutputStream(),
				Charset.forName("UTF-8"), 32768)) {
			ThroughputTimer test = new ThroughputTimer("Unsynchronized OutputStreamWriter with buffer[32768] char",
					50000000).start();
			for (int i = 0; i < test.getSampleSize(); i++) {
				out.write(CHAR);
			}
			test.stop();
		}
	}

	@Test
	public void testFastOutputStreamWriterWithBigCharArray100() throws Exception {
		try (FastOutputStreamWriterWithCharArray out = new FastOutputStreamWriterWithCharArray(new NullOutputStream(),
				Charset.forName("UTF-8"), 32768)) {
			ThroughputTimer test = new ThroughputTimer(
					"Unsynchronized OutputStreamWriter with buffer[32768] String 100", 50000000).start();
			for (int i = 0; i < test.getSampleSize() / 100; i++) {
				out.write(STRING100);
			}
			test.stop();
		}
	}

	public static void main(String[] args) throws Exception {
		ThroughputTest test = new ThroughputTest();
		test.testByteArrayOutputStream();
		test.testByteArrayOutputStreamCapacity();
		test.testUnsynchronizedByteArrayOutputStream();
		test.testUnsynchronizedByteArrayOutputStreamCapacity();
		test.testArrayCopy(1);
		test.testArrayCopy(10);
		test.testArrayCopy(100);
		test.testArrayCopy(1000);
		test.testArrayCopy(10000);
		test.testArrayCopy(100000);
		test.testFileCopy();
		test.testFileOutputStream(1);
		test.testFileOutputStream(10);
		test.testFileOutputStream(100);
		test.testFileOutputStream(1000);
		test.testFileOutputStream(10000);
		test.testFileOutputStream(100000);
		test.testFileOutputStreamChannel1();
		test.testFileOutputStreamChannel100();
		test.testAsynchronousFileChannel1();
		test.testAsynchronousFileChannel100();
		test.testOutputStreamWriter1();
		test.testOutputStreamWriter100();
		test.testUnsynchronizedOutputStreamWriter1();
		test.testUnsynchronizedOutputStreamWriter100();
		test.testFastOutputStreamWriterWithCharArrayAndByteChannel1();
		test.testFastOutputStreamWriterWithBigCharArrayAndByteChannel1();
		test.testFastOutputStreamWriterWithCharArrayAndByteChannel100();
		test.testFastOutputStreamWriterWithBigCharArrayAndByteChannel100();
		test.testFastOutputStreamWriterWithCharArray1();
		test.testFastOutputStreamWriterWithBigCharArray1();
		test.testFastOutputStreamWriterWithCharArray100();
		test.testFastOutputStreamWriterWithBigCharArray100();
	}

}
