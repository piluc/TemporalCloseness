import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BackwardBFS {
	class Interval {
		int r, l1, s1, l2, s2;

		Interval(int r, int l1, int s1, int l2, int s2) {
			this.r = r;
			this.l1 = l1;
			this.s1 = s1;
			this.l2 = l2;
			this.s2 = s2;
		}

		int[] getLast() {
			int[] result = { l1, s1 };
			if (l2 < l1) {
				result[0] = l2;
				result[1] = s2;
			}
			return result;
		}

		int[] max() {
			int[] result = { l1, s1 };
			if (s2 > s1) {
				result[0] = l2;
				result[1] = s2;
			}
			return result;
		}

		int[] min() {
			int[] result = { l1, s1 };
			if (s2 < s1) {
				result[0] = l2;
				result[1] = s2;
			}
			return result;
		}

		void set(int r, int l1, int s1, int l2, int s2) {
			this.r = r;
			this.l1 = l1;
			this.s1 = s1;
			this.l2 = l2;
			this.s2 = s2;
		}

		double getClosenessContribution() {
			double delta = 0.0;
			if (s1 < Integer.MAX_VALUE && s2 < Integer.MAX_VALUE) {
				if (r <= tomega + 1) {
					if (s1 < s2) {
						delta = Math.log(1.0 * (r - s1 + 1) / (r - s2 + 1));
					} else if (s1 > s2) {
						delta = Math.log(1.0 * (r - s2 + 1) / (r - s1 + 1));
					}
				}
			}
			return delta;
		}

		void setMax(int l, int r, int s) {
			this.r = r;
			if (s1 > s2) {
				l1 = l;
				s1 = s;
			} else {
				l2 = l;
				s2 = s;
			}
		}

		void setMin(int l) {
			if (s1 < s2) {
				l1 = l;
			} else {
				l2 = l;
			}
		}

		public String toString() {
			return "{[" + l1 + "," + r + ")," + s1 + "}, {[" + l2 + "," + r + ")," + s2 + "}";
		}
	}

	private int n;
	private int s;
	private int talpha;
	private int tomega;
	private String file;
	private String currentLine;
	private BufferedReader br;
	private Interval[] nodeInterval;
	private int[] numberIntervals;

	public BackwardBFS(int n, int talpha2, int tomega2, String file, int s) {
		this.n = n;
		this.talpha = talpha2;
		this.tomega = tomega2;
		this.file = file;
		this.nodeInterval = new Interval[n];
		this.s = s;
	}

	void init() {
		for (int i = 0; i < n; i++) {
			nodeInterval[i] = new Interval(tomega + 2, tomega + 1, Integer.MAX_VALUE, tomega + 1, Integer.MAX_VALUE);
		}
		numberIntervals = new int[n];
	}

	void printIntervals() {
		for (int i = 0; i < n; i++) {
			System.out.println(i + ": " + nodeInterval[i]);
		}
	}

	double[] bfsWithCloseness(boolean directed) {
		double[] closeness = new double[n];
		try {
			currentLine = br.readLine();
			while (currentLine != null) {
				String[] edge = currentLine.split(" ");
				int t = Integer.parseInt(edge[2]);
				if (t >= talpha && t <= tomega) {
					nodeInterval[s].set(t + 1, t, t, t, t);
					int u = Integer.parseInt(edge[0]);
					int v = Integer.parseInt(edge[1]);
					int[] rtu = nodeInterval[u].min();
					int[] _rtu = nodeInterval[u].max();
					int[] rtv = nodeInterval[v].min();
					int[] _rtv = nodeInterval[v].max();
					if (rtu[1] > t && rtv[1] > t) {
						if (!directed && rtu[0] < rtv[0]) {
							closeness[v] = closeness[v] + nodeInterval[v].getClosenessContribution();
							nodeInterval[v].setMax(rtu[0], rtv[0], t);
							numberIntervals[v] = numberIntervals[v] + 1;
						} else if (rtu[0] > rtv[0]) {
							closeness[u] = closeness[u] + nodeInterval[u].getClosenessContribution();
							nodeInterval[u].setMax(rtv[0], rtu[0], t);
							numberIntervals[u] = numberIntervals[u] + 1;
						}
					} else if (rtu[1] > t && rtv[1] == t) {
						if (!directed && rtu[0] < rtv[0]) {
							nodeInterval[v].setMin(rtu[0]);
						} else if (rtu[0] > _rtv[0]) {
							closeness[u] = closeness[u] + nodeInterval[u].getClosenessContribution();
							nodeInterval[u].setMax(_rtv[0], rtu[0], t);
							numberIntervals[u] = numberIntervals[u] + 1;
						}
					} else if (rtu[1] == t && rtv[1] > t) {
						if (rtv[0] < rtu[0]) {
							nodeInterval[u].setMin(rtv[0]);
						} else if (!directed && rtv[0] > _rtu[0]) {
							closeness[v] = closeness[v] + nodeInterval[v].getClosenessContribution();
							nodeInterval[v].setMax(_rtu[0], rtv[0], t);
							numberIntervals[v] = numberIntervals[v] + 1;
						}
					} else if (rtu[1] == t && rtv[1] == t) {
						if (rtu[0] > _rtv[0]) {
							nodeInterval[u].setMin(_rtv[0]);
						} else if (!directed && rtv[0] > _rtu[0]) {
							nodeInterval[v].setMin(_rtu[0]);
						}
					}
				}
				currentLine = br.readLine();
			}
			for (int u = 0; u < n; u++) {
				if (u != s) {
					closeness[u] = closeness[u] + nodeInterval[u].getClosenessContribution();
					int[] last = nodeInterval[u].getLast();
					if (last[0] > talpha && last[1] < Integer.MAX_VALUE) {
						closeness[u] = closeness[u] + Math.log(1.0 * (last[0] - talpha + 1) / (last[0] - last[1] + 1));
						numberIntervals[u] = numberIntervals[u] + 1;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return closeness;
	}

	public double[] run(boolean directed) {
		try {
			br = new BufferedReader(new FileReader(file));
			init();
			double[] closeness = bfsWithCloseness(directed);
			br.close();
			return closeness;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}