public class Main {
	public static double[] exactCloseness(int n, int talpha, int tomega, String fileIncreasing, boolean isUndirected) {
		double closeness[] = new double[n];
		for (int source = 0; source < n; source++) {
			ForwardBFS fbfs = new ForwardBFS(n, talpha, tomega, fileIncreasing, source);
			closeness[source] = fbfs.run(!isUndirected) / (((double) (n - 1)) * (tomega - talpha));
		}
		return closeness;
	}

	public static double getExactCloseness(int n, int talpha, int tomega, String fileIncreasing, boolean isUndirected,
			int source) {
		ForwardBFS fbfs = new ForwardBFS(n, talpha, tomega, fileIncreasing, source);
		double closeness = fbfs.run(!isUndirected) / (((double) (n - 1)) * (tomega - talpha));
		return closeness;
	}

	public static double[] estimateCloseness(int n, int talpha, int tomega, String fileDecreasing, int sampleSize,
			boolean isUndirected) {
		double closenessEstimate[] = new double[n];
		for (int k = 0; k < sampleSize; k++) {
//			int d = (int) (Math.random() * n);
			int d = k;
			BackwardBFS bbfs = new BackwardBFS(n, talpha, tomega, fileDecreasing, d);
			double[] est = bbfs.run(!isUndirected);
			for (int source = 0; source < n; source++) {
				if (d != source) {
					closenessEstimate[source] += est[source];
				}
			}
		}
		double scaleFactor = n / (((double) (n - 1)) * sampleSize * (tomega - talpha));
		for (int source = 0; source < n; source++) {
			closenessEstimate[source] = closenessEstimate[source] * scaleFactor;
		}
		return closenessEstimate;
	}

	public static void main(String[] args) throws Exception {
		TemporalGraphProperties tgp = new TemporalGraphProperties(args[0]);
		boolean isUndirected = Boolean.parseBoolean(args[1]);
		System.out.println("File: " + args[0] + " (" + (isUndirected ? "undirected" : "directed") + " graph)");
		System.out.println(tgp);
		if (args[2].equals("E")) {
			if (args.length == 3) {
				System.out.println("Temporal closeness values in [" + tgp.minTime + "," + tgp.maxTime + "]");
				double[] closeness = exactCloseness(tgp.nodes, (int) tgp.minTime, (int) tgp.maxTime, args[0],
						isUndirected);
				for (int u = 0; u < tgp.nodes; u++) {
					System.out.println("Node " + u + ": " + closeness[u]);
				}
			} else if (args.length == 4) {
				System.out.println("Node " + Integer.parseInt(args[3]) + ": " + getExactCloseness(tgp.nodes,
						(int) tgp.minTime, (int) tgp.maxTime, args[0], isUndirected, Integer.parseInt(args[3])));
			}
		} else if (args[2].equals("A")) {
			System.out.println("Approximate temporal closeness values in [" + tgp.minTime + "," + tgp.maxTime + "]");
			double[] closeness = estimateCloseness(tgp.nodes, (int) tgp.minTime, (int) tgp.maxTime, args[0],
					Integer.parseInt(args[3]), isUndirected);
			for (int u = 0; u < tgp.nodes; u++) {
				System.out.println("Node " + u + ": " + closeness[u]);
			}
		}
	}
}
