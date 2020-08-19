import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TemporalGraphProperties {
	public int nodes;
	public long minTime;
	public long maxTime;
	public int edges;
	public HashMap<Integer, Integer> timeOccurrences;

	public TemporalGraphProperties(String fileIn) throws IOException {
		timeOccurrences = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(fileIn));
		String line = br.readLine();
		edges = 0;
		int maxnode = -1;
		minTime = Long.MAX_VALUE;
		maxTime = Long.MIN_VALUE;
		while (line != null && line.length() > 0) {
			edges++;
			String[] ll = line.split(" ");
			if (ll.length > 3)
				new RuntimeException("Line " + line + " with more than three fields");
			int a = Integer.parseInt(ll[0]);
			int b = Integer.parseInt(ll[1]);
			int c = Integer.parseInt(ll[2]);
			if (c > maxTime)
				maxTime = c;
			if (c < minTime)
				minTime = c;
			if (a > maxnode)
				maxnode = a;
			if (b > maxnode)
				maxnode = b;
			if (!timeOccurrences.containsKey(c)) {
				timeOccurrences.put(c, 1);
			} else {
				int o = timeOccurrences.get(c);
				timeOccurrences.put(c, o + 1);
			}
			line = br.readLine();
		}
		nodes = maxnode + 1;
		br.close();
	}

	public String toString() {
		return "Nodes: " + nodes + "\nEdges: " + edges + "\nMin Time: " + minTime + "\nMax Time: " + maxTime
				+ "\nTime instants: " + timeOccurrences.size();
	}
}
