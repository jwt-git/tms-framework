package net.juweitang.tms.common.web.adapter.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.FileCopyUtils;

/**
 * Implementation of {@link HttpMessageConverter} that can read and write
 * strings.
 * 
 * <p>
 * By default, this converter supports all media types (
 * <code>&#42;&#47;&#42;</code>), and writes with a {@code Content-Type} of
 * {@code text/plain}. This can be overridden by setting the
 * {@link #setSupportedMediaTypes(java.util.List) supportedMediaTypes} property.
 * 
 * ȱʡ����ĳ�UTF-8���ҿ��Կ�������
 * 
 * @author Arjen Poutsma
 * @author sam.zhang
 * @since 3.0
 */
public class StringHttpMessageConverter extends
		AbstractHttpMessageConverter<String> implements InitializingBean {

	public Charset defaultCharset;

	private List<Charset> availableCharsets;

	private boolean writeAcceptCharset = true;

	public StringHttpMessageConverter() {
		super();
	}

	/**
	 * Indicates whether the {@code Accept-Charset} should be written to any
	 * outgoing request.
	 * <p>
	 * Default is {@code true}.
	 */
	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}

	@Override
	protected String readInternal(Class clazz, HttpInputMessage inputMessage)
			throws IOException {
		MediaType contentType = inputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType
				.getCharSet() : defaultCharset;
		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage
				.getBody(), charset));
	}

	@Override
	protected Long getContentLength(String s, MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			try {
				return (long) s.getBytes(charset.name()).length;
			} catch (UnsupportedEncodingException ex) {
				// should not occur
				throw new InternalError(ex.getMessage());
			}
		} else {
			return null;
		}
	}

	@Override
	protected void writeInternal(String s, HttpOutputMessage outputMessage)
			throws IOException {
		if (writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		MediaType contentType = outputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType
				.getCharSet() : defaultCharset;
		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(),
				charset));
	}

	/**
	 * Return the list of supported {@link Charset}.
	 * 
	 * <p>
	 * By default, returns {@link Charset#availableCharsets()}. Can be
	 * overridden in subclasses.
	 * 
	 * @return the list of accepted charsets
	 */
	protected List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}


	public void afterPropertiesSet() throws Exception {
		if(this.defaultCharset == null){
			defaultCharset = Charset.forName("UTF-8");
		}
		setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain",
				defaultCharset), MediaType.ALL));
		this.availableCharsets = new ArrayList<Charset>(Charset
				.availableCharsets().values());

	}

	public void setDefaultCharset(String charset) {
		this.defaultCharset = Charset.forName(charset);;
	}

}
