package semop;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import adts.StriverEvent;

public class CsvLeader implements ILeader<Double> {
	
	private BufferedReader br = null;
	private Double lastTime = null;
	private String filename;
	private Double offset=0d;
	private int processedEvents=0;
	private static final Double eps = 0.0001d;

	public CsvLeader(String filename) {
		this.filename = filename;
        try {
			this.br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public StriverEvent getNext() {
		String line;
		try {
			while (true) {
				line = br.readLine();
				if (line != null) {
					String[] data = line.split(",");
					Double ts = Double.valueOf(data[0]) + offset;
					if (lastTime != null && ts <= lastTime)
						ts = lastTime + eps;
					lastTime = ts;
					processedEvents++;
					return new StriverEvent(ts, Double.valueOf(data[1]));
				} else {
					this.offset = lastTime;
					this.br = new BufferedReader(new FileReader(filename));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return StriverEvent.posOutsideEv;
	}
	public int processedEvents() {
		return processedEvents;
	}
	
}
