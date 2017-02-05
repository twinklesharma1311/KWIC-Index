package se1;

import javax.swing.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.text.BadLocationException;

/**
 * Self contained auto-completing search panel SearchArea has a getText() method
 * which returns the text from the search box. It also contains a
 * setSearchTerms() method which allows the autocomplete search terms to be
 * added.
 */

public class SearchArea extends JPanel implements DocumentListener,
		ActionListener {
	private JScrollPane scrollDeal;
	private JTextArea searchBox;
	private JButton search = new JButton("Search");
	private static final String COMMIT_ACTION = "commit";

	private static enum Mode {
		INSERT, COMPLETION
	};

	private List<String> searchTerms;
	private Mode mode = Mode.INSERT;

	/**
	 * Constructor with no autocomplete terms.
	 */
	public SearchArea() {
		searchBox = new JTextArea();
		searchBox.setColumns(30);
		searchBox.setLineWrap(false);
		searchBox.setRows(2);
		searchBox.setWrapStyleWord(false);
		scrollDeal = new JScrollPane(searchBox);
		this.add(scrollDeal, BorderLayout.CENTER);
		this.setVisible(true);
		searchBox.getDocument().addDocumentListener(this);
		InputMap im = searchBox.getInputMap();
		ActionMap am = searchBox.getActionMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
		am.put(COMMIT_ACTION, new execute());
		searchTerms = new ArrayList<String>(1);
		searchTerms.add("-");
	}

	/**
	 * Constructor with specified autocomplete terms.
	 */
	public SearchArea(ArrayList<String> terms) {
		searchBox = new JTextArea();
		searchBox.setColumns(30);
		searchBox.setLineWrap(true);
		searchBox.setRows(2);
		searchBox.setWrapStyleWord(true);
		scrollDeal = new JScrollPane(searchBox);
		this.add(scrollDeal);
		this.setVisible(true);
		searchBox.getDocument().addDocumentListener(this);
		InputMap im = searchBox.getInputMap();
		ActionMap am = searchBox.getActionMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
		am.put(COMMIT_ACTION, new execute());
		searchTerms = terms;
	}

	public void actionPerformed(ActionEvent e) {

	}

	public void changedUpdate(DocumentEvent e) {

	}

	public void removeUpdate(DocumentEvent e) {

	}

	/**
	 * Captures the input the user has put in so far, and looks to see if it can
	 * complete it to a search term.
	 */
	public void insertUpdate(DocumentEvent e) 
	{
		if (e.getLength() != 1) {
			return;
		}
		int position = e.getOffset();
		String content = null;
		try {
			content = searchBox.getText(0, position + 1);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
		int count;
		for (count = position; count >= 0; count--) {
			if (!Character.isLetter(content.charAt(count))) {
				break;
			}
		}
		if (position - count < 2) {
			return;
		}
		String typedBit = content.substring(count + 1).toLowerCase();
		int n = Collections.binarySearch(searchTerms, typedBit);
		if (n < 0 && -n <= searchTerms.size()) {
			String match = searchTerms.get(-n - 1);
			if (match.startsWith(typedBit)) {
				String completion = match.substring(position - count);
				SwingUtilities.invokeLater(new completor(completion,
						position + 1));
			}
		} else {
			mode = Mode.INSERT;
		}
	}

	/**
	 * Returns the text in the JTextArea
	 * 
	 * @return The text from the JTextArea
	 */
	public String getText() {
		return searchBox.getText();
	}

	/**
	 * Sets the autofill search terms
	 */
	public void setSearchTerms(List<String> terms) {
		searchTerms = terms;
	}

	/**
	 * Completes the action specified
	 */
	private class completor implements Runnable {
		String completion;
		int position;

		completor(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}

		public void run() {
			searchBox.insert(completion, position);
			searchBox.setCaretPosition(position + completion.length());
			searchBox.moveCaretPosition(position);
			mode = Mode.COMPLETION;
		}
	}

	/**
	 * Commits the changes.
	 */
	private class execute extends AbstractAction {
		public void actionPerformed(ActionEvent ev) {
			if (mode == Mode.COMPLETION) {
				int position = searchBox.getSelectionEnd();
				searchBox.insert(" ", position);
				searchBox.setCaretPosition(position + 1);
				mode = Mode.INSERT;
			} else {
				searchBox.replaceSelection("\n");
			}
		}
	}

}