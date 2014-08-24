package org.tmsframework.demo.web.action.checkcode;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.BlockGimpyRenderer;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import nl.captcha.gimpy.RippleGimpyRenderer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.text.producer.TextProducer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tmsframework.common.util.captcha.EasyCharTextProducer;
import org.tmsframework.common.util.captcha.FixedWordRenderer;

/**
 * 
 * @author fish
 * 
 */
@Controller
@RequestMapping("/checkcode/*")
public class CheckCodeAction {

	private static final List<Font> englishFonts = Arrays.asList(new Font(
			"Lucida Sans", Font.ITALIC, 55), new Font("SansSerif", Font.ITALIC,
			60));

	@RequestMapping
	public void simple(HttpServletResponse response) throws IOException {
		Captcha captcha = new Captcha.Builder(220, 80).addText(
				new EasyCharTextProducer(),
				new FixedWordRenderer(Color.black, englishFonts)).gimp(
				new RippleGimpyRenderer()).gimp(new BlockGimpyRenderer()).gimp(
				new DropShadowGimpyRenderer()).addBackground(
				new GradiatedBackgroundProducer()).addNoise(
				new CurvedLineNoiseProducer(Color.black, 1.8f)).addNoise(
				new CurvedLineNoiseProducer(Color.black, 1.3f)).build();
		response.setContentType("image/jpeg");
		OutputStream os = response.getOutputStream();
		ImageIO.write(captcha.getImage(), "JPEG", os);
	}

	@RequestMapping
	public void arithmetic(HttpServletResponse response) throws IOException {
		final Random random = new Random();
		final int one = 1 + random.nextInt(49);
		final int two = random.nextInt(49);

		Captcha captcha = new Captcha.Builder(220, 80).addText(
				new TextProducer() {
					public String getText() {
						if (random.nextBoolean()) {
							return new StringBuilder().append(one).append('+')
									.append(two).toString();
						} else {
							if (one > two) {
								return new StringBuilder().append(one).append(
										'-').append(two).toString();
							} else {
								return new StringBuilder().append(two).append(
										'-').append(one).toString();
							}
						}
					}
				}, new FixedWordRenderer(Color.black, englishFonts)).gimp(
				new RippleGimpyRenderer()).gimp(new DropShadowGimpyRenderer())
				.addNoise(new CurvedLineNoiseProducer(Color.black, 2.8f))
				.addNoise(new CurvedLineNoiseProducer(Color.black, 1.3f))
				.addBackground(new GradiatedBackgroundProducer()).build();
		response.setContentType("image/jpeg");
		OutputStream os = response.getOutputStream();
		ImageIO.write(captcha.getImage(), "JPEG", os);
	}

}
