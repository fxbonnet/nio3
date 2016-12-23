package com.github.nio3;

import java.text.DecimalFormat;

public class ThroughputTimer {
	private final String label;
	private final long sampleSize;
	private long start;
	private long end;

	public ThroughputTimer(String label, long sampleSize) {
		this.label = label;
		this.sampleSize = sampleSize;
	}

	public ThroughputTimer start() {
		start = System.currentTimeMillis();
		return this;
	}

	public ThroughputTimer stop() {
		end = System.currentTimeMillis();
		long duration = end - start;
		float throughput = sampleSize / duration * 8 / 1000;
		DecimalFormat format = new DecimalFormat("#,##0");
		System.out.println(label + " -> " + format.format(throughput) + " MB/s");
		return this;
	}

	public long getSampleSize() {
		return sampleSize;
	}

}
