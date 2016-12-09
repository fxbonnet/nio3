package com.github.nio3;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Writer that writes in the wrapped Writer aynchroneously using a SingleThreadExecutor.
 *
 */
public class AsyncWriter extends Writer {
	private final Writer writer;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	public AsyncWriter(Writer writer, char delimiter) {
		this.writer = writer;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		executorService.execute(() -> {
			try {
				writer.write(cbuf, off, len);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void flush() throws IOException {
		executorService.execute(() -> {
			try {
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void close() throws IOException {
		executorService.execute(() -> {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
