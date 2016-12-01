package com.github.nio3;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * A non-blocking non-synchronized writer that counts the number of characters
 * written and the time between open and close.
 *
 */
@NotThreadSafe
public class CountingWriter extends Writer {
	private long count = 0;
	private long start = System.currentTimeMillis();
	private long end = -1;

	@Override
	public void write(int c) throws IOException {
		count += 1;
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		count += len;
	}

	@Override
	public void close() {
		end = System.currentTimeMillis();
		System.out.println("Written characters: " + count + " time elapsed ms: " + (end - start));
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		count += len;
	}

	@Override
	public void flush() throws IOException {
	}

	public long getCount() {
		return count;
	}

	public long getDuration() {
		long end;
		if (this.end < 0)
			end = System.currentTimeMillis();
		else
			end = this.end;
		return end - start;
	}

	@Override
	public String toString() {
		return "Written characters: " + count + " time elapsed ms: " + getDuration();
	}

}
