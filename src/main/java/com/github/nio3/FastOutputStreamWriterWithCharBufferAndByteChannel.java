package com.github.nio3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public final class FastOutputStreamWriterWithCharBufferAndByteChannel extends Writer {
	private final static int DEFAULT_BUFFER_SIZE = 32768;
	private final OutputStream outputStream;
	private final WritableByteChannel channel;
	private final int bufferSize;
	private final CharsetEncoder encoder;
	private final CharBuffer charBuffer;
	private final ByteBuffer byteBuffer;

	public FastOutputStreamWriterWithCharBufferAndByteChannel(OutputStream outputStream, Charset charset) {
		this(outputStream, charset, DEFAULT_BUFFER_SIZE);
	}

	public FastOutputStreamWriterWithCharBufferAndByteChannel(OutputStream outputStream, Charset charset, int bufferSize) {
		this.outputStream = outputStream;
		this.encoder = charset.newEncoder();
		this.bufferSize = bufferSize;
		this.charBuffer = CharBuffer.allocate(bufferSize);
		// this.byteBuffer = ByteBuffer.allocateDirect((int) (bufferSize *
		// encoder.maxBytesPerChar()));
		this.byteBuffer = ByteBuffer.allocate((int) (bufferSize * encoder.maxBytesPerChar()));
		this.channel = Channels.newChannel(outputStream);

	}

	@Override
	public final void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < len + off; i++) {
			write(cbuf[i]);
		}
	}

	@Override
	public final void write(String str) throws IOException {
		write(str, 0, str.length());
	}

	private final void write(char ch) throws IOException {
		charBuffer.append(ch);
		if (charBuffer.position() == bufferSize)
			flushBuffer();
	}

	@Override
	public final void flush() throws IOException {
		flushBuffer();
		outputStream.flush();
	}

	@Override
	public final void close() throws IOException {
		try (OutputStream os = outputStream) {
			flush();
		}
	}

	private final void flushBuffer() throws IOException {
		charBuffer.flip();
		encoder.encode(charBuffer, byteBuffer, true);
		byteBuffer.flip();
		channel.write(byteBuffer);
		byteBuffer.clear();
		charBuffer.clear();
	}

	@Override
	public final void write(int c) throws IOException {
		write((char) c);
	}

	@Override
	public final void write(char[] cbuf) throws IOException {
		write(cbuf, 0, cbuf.length);
	}

	@Override
	public final void write(String str, int off, int len) throws IOException {
		// TODO use String.getChars and write directly to the buffer
		for (int i = off; i < len + off; i++) {
			write(str.charAt(i));
		}
	}

	@Override
	public final Writer append(CharSequence csq) throws IOException {
		return append(csq, 0, csq.length());
	}

	@Override
	public final Writer append(CharSequence csq, int start, int end) throws IOException {
		for (int i = start; i < end; i++) {
			write(csq.charAt(i));
		}
		return this;
	}

	@Override
	public Writer append(char c) throws IOException {
		write(c);
		return this;
	}

}
