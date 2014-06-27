package net.juweitang.tms.common.util.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.captcha.text.renderer.WordRenderer;

public class FixedWordRenderer implements WordRenderer {
	// The text will be rendered 25%/5% of the image height/width from the X and
	// Y axes
	private static final double YOFFSET = 0.25;
	private static final double XOFFSET = 0.15;
	private static final int charDistance = 2;
	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final List<Font> DEFAULT_FONTS = new ArrayList<Font>();

	private final Color _color;
	private final List<Font> _fonts;

	static {
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 40));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 40));
	}

	/**
	 * Will render the characters in black and in either 40pt Arial or Courier.
	 */
	public FixedWordRenderer() {
		this(DEFAULT_COLOR, DEFAULT_FONTS);
	}

	public FixedWordRenderer(Color color, List<Font> fonts) {
		_color = color != null ? color : DEFAULT_COLOR;
		_fonts = fonts != null ? fonts : DEFAULT_FONTS;
	}

	/**
	 * Render a word onto a BufferedImage.
	 * 
	 * @param word
	 *            The word to be rendered.
	 * @param bi
	 *            The BufferedImage onto which the word will be painted on to
	 */
	public void render(String word, BufferedImage image) {
		Random random = new Random();
		Graphics2D g = image.createGraphics();

		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g.setRenderingHints(hints);

		g.setColor(_color);
		FontRenderContext frc = g.getFontRenderContext();
		int startPosX = (int) Math.round(image.getWidth() * XOFFSET);
		int startPosY = image.getHeight()
				- (int) Math.round(image.getHeight() * YOFFSET);
		char[] wc = word.toCharArray();
		for (char element : wc) {
			char[] itchar = new char[] { element };
			int choiceFont = random.nextInt(_fonts.size());
			Font itFont = _fonts.get(choiceFont);
			g.setFont(itFont);

			GlyphVector gv = itFont.createGlyphVector(frc, itchar);
			double charWitdth = gv.getVisualBounds().getWidth();

			g.drawChars(itchar, 0, itchar.length, startPosX, startPosY);
			startPosX = startPosX + (int) charWitdth
					- random.nextInt(charDistance)-2;
		}
	}

}
