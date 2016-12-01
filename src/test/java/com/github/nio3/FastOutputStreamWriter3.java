package com.github.nio3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FastOutputStreamWriter3 extends Writer {
	private final static int DEFAULT_BUFFER_SIZE = 32768;
	private final OutputStream outputStream;
	private final WritableByteChannel channel;
	private final int bufferSize;
	private final CharsetEncoder encoder;
	private final char[] charBuffer;
	private int cPos = 0;
	private final ByteBuffer byteBuffer;

	public FastOutputStreamWriter3(OutputStream outputStream, Charset charset) {
		this(outputStream, charset, DEFAULT_BUFFER_SIZE);
	}

	public FastOutputStreamWriter3(OutputStream outputStream, Charset charset, int bufferSize) {
		this(Channels.newChannel(outputStream), charset, bufferSize);
	}

	public FastOutputStreamWriter3(String path, Charset charset) throws IOException {
		this(path, charset, DEFAULT_BUFFER_SIZE);
	}

	public FastOutputStreamWriter3(final String path, Charset charset, int bufferSize) throws IOException {
		this(new WritableByteChannel() {
			private final AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(path),
					StandardOpenOption.WRITE);
			private long position = 0;

			@Override
			public final boolean isOpen() {
				return channel.isOpen();
			}

			@Override
			public final void close() throws IOException {
				channel.close();
			}

			@Override
			public final int write(ByteBuffer src) throws IOException {
				channel.write(src, position);
				int written = src.limit();
				position = position + written;
				return written;
			}
		}, charset, bufferSize);
	}

	public FastOutputStreamWriter3(WritableByteChannel channel, Charset charset, int bufferSize) {
		this.channel = channel;
		this.encoder = charset.newEncoder();
		this.bufferSize = bufferSize;
		this.charBuffer = new char[bufferSize];
		this.byteBuffer = ByteBuffer.allocate((int) (bufferSize * encoder.maxBytesPerChar()));
		this.outputStream = null;
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
		byteBuffer.flip();
		channel.write(byteBuffer);
	}

	private final void flushBufferIfFull() throws IOException {
		if (cPos == bufferSize)
			flushBuffer();
	}

}
