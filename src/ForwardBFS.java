import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ForwardBFS {
	class Interval {
		int l, r1, t1, r2, t2;

		Interval(int l, int r1, int t1, int r2, int t2) {
			this.l = l;
			this.r1 = r1;
			this.t1 = t1;
			this.r2 = r2;
			this.t2 = t2;
		}

		int[] max() {
			int[] r = { r1, t1 };
			if (t2 > t1) {
				r[0] = r2;
				r[1] = t2;
			}
			return r;
		}

		int[] min() {
			int[] r = { r1, t1 };
			if (t2 < t1) {
				r[0] = r2;
				r[1] = t2;
			}
			return r;
		}

		void set(int l, int r1, int t1, int r2, int t2) {
			this.l = l;
			this.r1 = r1;
			this.t1 = t1;
			this.r2 = r2;
			this.t2 = t2;
		}

		double getClosenessContribution() {
			double delta = 0.0;
			if (l >= 0) {
				if (t1 > t2) {
					delta = Math.log(1.0 * (t1 - Math.max(talpha, l) + 1) / (t1 - r1 + 1));
				} else if (t1 < t2) {
					delta = Math.log(1.0 * (t2 - Math.max(talpha, l) + 1) / (t2 - r2 + 1));
				}
			}
			return delta;
		}

		void setMin(int l, int r, int t) {
			this.l = l;
			if (t1 < t2) {
				r1 = r;
				t1 = t;
			} else {
				r2 = r;
				t2 = t;
			}
		}

		void setMax(int r) {
			if (t1 > t2) {
				r1 = r;
			} else {
				r2 = r;
			}
		}

		public String toString() {
			return "{[" + l + "," + r1 + ")," + t1 + "}, {[" + l + "," + r2 + ")," + t2 + "}";
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

	public ForwardBFS(int n, int t0, int t1, String file, int s) {
		this.n = n;
		this.talpha = t0;
		this.tomega = t1;
		this.file = file;
		this.nodeInterval = new Interval[n];
		this.s = s;
	}

	void init() {
		for (int i = 0; i < n; i++) {
			nodeInterval[i] = new Interval(talpha - 2, talpha - 1, Integer.MIN_VALUE, talpha - 1, Integer.MIN_VALUE);
		}
		numberIntervals = new int[n];
	}

	void printIntervals() {
		for (int i = 0; i < n; i++) {
			System.out.println(i + ": " + nodeInterval[i]);
		}
	}

	double bfsWithCloseness(boolean directed) {
		double closeness = 0;
		try {
			currentLine = br.readLine();
			while (currentLine != null) {
				String[] edge = currentLine.split(" ");
				int t = Integer.parseInt(edge[2]);
				if (t >= talpha && t <= tomega) {
					nodeInterval[s].set(t - 1, t, t - 1, t, t - 1);
					int u = Integer.parseInt(edge[0]);
					int v = Integer.parseInt(edge[1]);
					int[] rtu = nodeInterval[u].max();
					int[] _rtu = nodeInterval[u].min();
					int[] rtv = nodeInterval[v].max();
					int[] _rtv = nodeInterval[v].min();
					if (rtu[1] < t && rtv[1] < t) {
						if (rtu[0] > rtv[0]) {
							closeness = closeness + nodeInterval[v].getClosenessContribution();
							nodeInterval[v].setMin(rtv[0], rtu[0], t);
							numberIntervals[v] = numberIntervals[v] + 1;
						} else if (!directed && rtu[0] < rtv[0]) {
							closeness = closeness + nodeInterval[u].getClosenessContribution();
							nodeInterval[u].setMin(rtu[0], rtv[0], t);
							numberIntervals[u] = numberIntervals[u] + 1;
						}
					} else if (rtu[1] < t && rtv[1] == t) {
						if (rtu[0] > rtv[0]) {
							nodeInterval[v].setMax(rtu[0]);
						} else if (!directed && rtu[0] < _rtv[0]) {
							closeness = closeness + nodeInterval[u].getClosenessContribution();
							nodeInterval[u].setMin(rtu[0], _rtv[0], t);
							numberIntervals[u] = numberIntervals[u] + 1;
						}
					} else if (rtu[1] == t && rtv[1] < t) {
						if (!directed && rtv[0] > rtu[0]) {
							nodeInterval[u].setMax(rtv[0]);
						} else if (rtv[0] < _rtu[0]) {
							closeness = closeness + nodeInterval[v].getClosenessContribution();
							nodeInterval[v].setMin(rtv[0], _rtu[0], t);
							numberIntervals[v] = numberIntervals[v] + 1;
						}
					} else if (rtu[1] == t && rtv[1] == t) {
						if (!directed && rtu[0] < _rtv[0]) {
							nodeInterval[u].setMax(_rtv[0]);
						} else if (rtv[0] < _rtu[0]) {
							nodeInterval[v].setMax(_rtu[0]);
						}
					}
				}
				currentLine = br.readLine();
			}
			for (int u = 0; u < n; u++) {
				if (u != s) {
					closeness = closeness + nodeInterval[u].getClosenessContribution();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return closeness;
	}

	public double run(boolean directed) {
		try {
			br = new BufferedReader(new FileReader(file));
			init();
			double closeness = bfsWithCloseness(directed);
			br.close();
			return closeness;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
