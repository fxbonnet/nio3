package com.github.nio3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public final class FastOutputStreamWriterWithCharArray extends Writer {
	private final static int DEFAULT_BUFFER_SIZE = 8192;
	private final OutputStream outputStream;
	private final int bufferSize;
	private final CharsetEncoder encoder;
	private final char[] charBuffer;
	private int cPos = 0;
	private final ByteBuffer byteBuffer;

	public FastOutputStreamWriterWithCharArray(OutputStream outputStream, Charset charset) {
		this(outputStream, charset, DEFAULT_BUFFER_SIZE);
	}

	public FastOutputStreamWriterWithCharArray(OutputStream outputStream, Charset charset, int bufferSize) {
		this.encoder = charset.newEncoder();
		this.bufferSize = bufferSize;
		this.charBuffer = new char[bufferSize];
		this.byteBuffer = ByteBuffer.allocate((int) (bufferSize * encoder.maxBytesPerChar()));
		this.outputStream = outputStream;
	}

	public FastOutputStreamWriterWithCharArray(String path, Charset charset) throws IOException {
		this(path, charset, DEFAULT_BUFFER_SIZE);
	}

	public FastOutputStreamWriterWithCharArray(final String path, Charset charset, int bufferSize) throws IOException {
		this(new FileOutputStream(path), charset, bufferSize);
	}

	@Override
	public final void write(char[] chars, int off, int len) throws IOException {
		int charsToWrite = writableChars(len);
		while (charsToWrite != 0) {
			copy(chars, off, charsToWrite);
			len = len - charsToWrite;
			flushBufferIfFull();
			off = off + charsToWrite;
			charsToWrite = writableChars(len);
		}
	}

	@Override
	public final void write(String chars, int off, int len) throws IOException {
		int charsToWrite = writableChars(len);
		while (charsToWrite != 0) {
			copy(chars, off, charsToWrite);
			len = len - charsToWrite;
			flushBufferIfFull();
			off = off + charsToWrite;
			charsToWrite = writableChars(len);
		}
	}

	@Override
	public final void write(String str) throws IOException {
		write(str, 0, str.length());
	}

	@Override
	public final void write(int c) throws IOException {
		charBuffer[cPos++] = (char) c;
		flushBufferIfFull();
	}

	@Override
	public final void write(char[] cbuf) throws IOException {
		write(cbuf, 0, cbuf.length);
	}

	@Override
	public final Writer append(CharSequence csq) throws IOException {
		return append(csq, 0, csq.length());
	}

	public final Writer append(String str) throws IOException {
		return append(str, 0, str.length());
	}

	@Override
	public final Writer append(CharSequence csq, int start, int end) throws IOException {
		for (int i = start; i < end; i++) {
			write(csq.charAt(i));
		}
		return this;
	}

	public final Writer append(String str, int start, int end) throws IOException {
		write(str, start, end - start);
		return this;
	}

	@Override
	public final void flush() throws IOException {
		flushBuffer();
		outputStream.flush();
	}

	@Override
	public final void close() throws IOException {
		if (this.outputStream != null)
			try (OutputStream os = outputStream) {
				flush();
			}
	}

	private final void copy(char[] chars, int off, int charsToWrite) {
		System.arraycopy(chars, off, charBuffer, cPos, charsToWrite);
		cPos += charsToWrite;
	}

	private final void copy(String chars, int off, int charsToWrite) throws IOException {
		chars.getChars(off, off + charsToWrite, charBuffer, cPos);
		cPos += charsToWrite;
	}

	private final int writableChars(int len) {
		int spaceLeftInBuffer = bufferSize - cPos;
		if (len > spaceLeftInBuffer)
			return spaceLeftInBuffer;
		else
			return len;
	}

	private final void write(char ch) throws IOException {
		charBuffer[cPos++] = ch;
		flushBufferIfFull();
	}

	private final void flushBuffer() throws IOException {
		encoder.encode(CharBuffer.wrap(charBuffer), byteBuffer, true);
		cPos = 0;
		outputStream.write(byteBuffer.array(), 0, byteBuffer.position());
		byteBuffer.clear();
	}

	private final void flushBufferIfFull() throws IOException {
		if (cPos == bufferSize)
			flushBuffer();
	}

}
