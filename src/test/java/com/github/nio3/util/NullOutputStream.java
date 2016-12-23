package com.github.nio3.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

	@Override
	public void write(int arg0) throws IOException {
		// Do nothing
	}

	@Override
	public void close() throws IOException {
		// Do nothing
	}

	@Override
	public void flush() throws IOException {
		// Do nothing
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// Do nothing
	}

	@Override
	public void write(byte[] b) throws IOException {
		// TODO Auto-generated method stub
		super.write(b);
	}

}
