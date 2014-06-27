package net.juweitang.tms.common.web.contain;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.juweitang.tms.common.profiler.TimeProfiler;
import net.juweitang.tms.common.web.velocity.eventhandler.DirectOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.tools.view.context.ViewContext;


/**
 * 
 * @author sam.zhang
 * 
 */

public class Contain {
	private static final Log log = LogFactory.getLog(Contain.class);

	/**
	 * 锟斤拷锟斤拷锟斤拷request锟斤拷,contain锟斤拷锟斤拷锟矫的憋拷锟絢ey
	 */
	private static final String ContainFlagKey = "iContain";

	protected ViewContext viewContext;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private static final int recursiveLevel = 20;

	private static final ThreadLocal<VaryInt> count = new ThreadLocal<VaryInt>();// 锟捷癸拷锟斤拷眉锟斤拷锟斤拷锟�

	public Contain() {
		super();
	}

	public void init(Object obj) {
		if (!(obj instanceof ViewContext)) {
			throw new IllegalArgumentException(
					"Tool can only be initialized with a ViewContext");
		}
		viewContext = (ViewContext) obj;
		this.request = viewContext.getRequest();
		this.response = viewContext.getResponse();
	}

	public ControlRender setTemplate(String controlName) {
		if (controlName == null) {
			throw new NullPointerException(
					"control template name can't be null.");
		}
		return new ControlRender(controlName);
	}

	public ControlRender get(String controlName) {
		return setTemplate(controlName);
	}

	public class ControlRender implements DirectOutput {

		private String controlName;

		private Set<ContainParameter> parameters = new HashSet<ContainParameter>();

		public ControlRender(String controlName) {
			this.controlName = controlName;
		}

		public ControlRender put(String key, Object value) {
			// 锟斤拷锟斤拷request锟斤拷锟较碉拷value锟斤拷锟斤拷锟斤拷染锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�也锟斤拷锟斤拷说锟斤拷锟斤拷锟捷革拷contain锟侥诧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絚ontain锟斤拷invoke
			parameters.add(new ContainParameter(Contain.this.request, key,
					value));
			return this;
		}

		public ControlRender setParameter(String key, Object value) {
			return this.put(key, value);
		}

		@Override
		public String toString() {
			if (enter()) {
				log.error("contain recursive invoked,so exist.");
				return "";
			}
			parameters.add(new ContainParameter(Contain.this.request,
					ContainFlagKey, Boolean.TRUE));
			ResponseWrapper responseWrapper = new ResponseWrapper(
					Contain.this.response);
			if (TimeProfiler.isProfileEnable()) {
				TimeProfiler.beginTask("execute contain :" + controlName);
			}
			try {
				Contain.this.request.getRequestDispatcher(controlName).include(
						Contain.this.request, responseWrapper);
				String back = responseWrapper.sw.toString();
				if (this.parameters != null) {
					for (ContainParameter cp : this.parameters) {
						cp.recover(Contain.this.request);
					}
				}
				return back;
			} catch (ServletException e) {
				if (log.isErrorEnabled()) {
					log.error("error in control render.", e);
				}
				return e.getMessage();
			} catch (IOException e) {
				if (log.isErrorEnabled()) {
					log.error("error in control render.", e);
				}
				return e.getMessage();
			} finally {
				if (TimeProfiler.isProfileEnable()) {
					TimeProfiler.endTask();
				}
				leave();
			}
		}

		/**
		 * 锟斤拷锟斤拷锟斤拷一锟斤拷,锟斤拷锟斤拷锟角否超癸拷莨锟斤拷锟�
		 * 
		 * @return
		 */
		private boolean enter() {
			VaryInt vi = count.get();
			if (vi == null) {
				vi = new VaryInt();
				vi.add();
				count.set(vi);
				return false;
			}
			int now = vi.add();
			if (now >= recursiveLevel) {
				vi.i = 0;
				return true;
			}
			return false;
		}

		private void leave() {
			VaryInt vi = count.get();
			if (vi == null) {// 锟斤拷锟杰帮拷?
				count.set(new VaryInt());
				return;
			}
			vi.sub();
		}
	}

	private static class ContainParameter {
		private String key;
		private Object original;

		public ContainParameter(HttpServletRequest request, String key,
				Object newValue) {
			this.key = key;
			this.original = request.getAttribute(key);
			request.setAttribute(key, newValue);
		}

		public void recover(HttpServletRequest request) {
			request.setAttribute(this.key, this.original);
		}

	}

	private class ResponseWrapper extends HttpServletResponseWrapper {
		public StringWriter sw = new StringWriter();

		public ResponseWrapper(HttpServletResponse r) {
			super(r);
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return new ServletOutputStream() {
				@Override
				public void write(int b) throws IOException {
					sw.write(b);
				}
			};
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return new PrintWriter(sw);
		}

		@Override
		public void reset() {
		}

		@Override
		public void resetBuffer() {
		}

		@Override
		public boolean isCommitted() {
			return false;
		}

		@Override
		public void flushBuffer() throws IOException {

		}

		@Override
		public void setBufferSize(int arg0) {

		}

		@Override
		public void setCharacterEncoding(String arg0) {

		}

		@Override
		public void setContentLength(int arg0) {

		}

		@Override
		public void setLocale(Locale arg0) {

		}

		@Override
		public void addDateHeader(String arg0, long arg1) {

		}

		@Override
		public void addHeader(String arg0, String arg1) {

		}

		@Override
		public void addIntHeader(String arg0, int arg1) {

		}

		@Override
		public void setDateHeader(String arg0, long arg1) {

		}

		@Override
		public void setHeader(String arg0, String arg1) {

		}

		@Override
		public void setIntHeader(String arg0, int arg1) {

		}

		@Override
		public void setStatus(int arg0, String arg1) {

		}

		@Override
		public void setStatus(int arg0) {

		}
	}

	private static final class VaryInt {
		int i = 0;

		public int add() {
			i++;
			return i;
		}

		public int sub() {
			i--;
			return i;
		}
	}
}
