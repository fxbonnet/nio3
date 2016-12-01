package com.github.nio3;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class MultithreadCsvWriter implements Closeable {
	private final Writer writer;
	private final char delimiter;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	public MultithreadCsvWriter(Writer writer, char delimiter) {
		this.writer = writer;
		this.delimiter = delimiter;
	}

	public void writeRecord(final String[] values, boolean preserveSpaces) throws IOException {
		executorService.execute(() -> {
			try {
				int length = values.length - 1;
				for (int i = 0; i < length; i++) {
					write(values[i]);
					writer.write(delimiter);
				}
				write(values[length]);
				writer.write('\r');
				writer.write('\n');
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void write(String str) throws IOException {
		if (str != null)
			writer.write(str);
	}

	@Override
	public void close() throws IOException {
		try {
			executorService.shutdown();
			executorService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		writer.close();
	}

}
