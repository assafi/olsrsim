package log.sqlproxy;

public class SqlProxyException extends Exception {

	private static final long serialVersionUID = 1545079193911218064L;

	public SqlProxyException() {
		super("General error originating from SqlProxy object");
	}

	public SqlProxyException(String msg) {
		super(msg);
	}

	public SqlProxyException(Throwable e) {
		super(e);
	}

	public SqlProxyException(String msg, Throwable e) {
		super(msg, e);
	}
}
