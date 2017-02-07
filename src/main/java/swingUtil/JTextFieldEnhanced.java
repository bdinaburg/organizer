package swingUtil;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class JTextFieldEnhanced extends JTextField{
	/**
	 * 
	 */
	private static final long serialVersionUID = -841286593557133530L;

	public JTextFieldEnhanced()
	{
		super();
		JTextFieldEnhanced self = this;
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				self.selectAll();
			}
		});
	}
}
