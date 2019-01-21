package semop;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import adts.StriverEvent;

public class CsvLeader implements ILeader<Double> {
	
	private BufferedReader br = null;
	private Double lastTime = null;
	private static final Double eps = 0.0001d;

	public CsvLeader(String filename) {
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
			line = br.readLine();
			if (line != null) {
			    String[] data = line.split(",");
			    Double ts = Double.valueOf(data[0]);
			    if (lastTime != null && ts <= lastTime)
			    		ts = lastTime + eps;
			    lastTime = ts;
			    return new StriverEvent(ts, Double.valueOf(data[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return StriverEvent.posOutsideEv;
	}
	
}
