import javax.swing.*;
import java.awt.*;



public class TextEditor extends JFrame {

    private String currentFile = "Untitled";

    /**
     * This is the variable that keeps track of weather the text was modified
     *
     */
    private boolean changed = false;

    private final Action New;
    private final Action Save;
    private final Action SaveAs;

    private JTextArea editArea = new JTextArea(20,120);

    private JTextArea terminalArea;

    private JMenuBar menuBar;
    private JToolBar toolBar;

    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;

    private boolean isRecording;
    private JFileChooser fileChooser;


    public TextEditor(){


        terminalArea =  new JTextArea(20, 120);

        editArea.setFont(new Font("Serif", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(editArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Monospace", Font.PLAIN, 20));


        editMenu = new JMenu("Edit");
        editMenu.setFont(new Font("Monospace", Font.PLAIN, 20));


        helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Monospace", Font.PLAIN, 20));


        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.setBorderPainted(false);
        menuBar.setFont(new Font("Serif", Font.PLAIN, 20));


        fileChooser = new JFileChooser();

        New = null;

        Save = null;
        SaveAs = null;


        fileMenu.add(New);
        fileMenu.add(Save);
        fileMenu.add(SaveAs);


        toolBar = new JToolBar("Sample Button");
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        toolBar.add(button);
        add(editArea);
        add(terminalArea, BorderLayout.SOUTH);
        add(toolBar, BorderLayout.NORTH);
        setJMenuBar(menuBar);
        setTitle(currentFile);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }


}
